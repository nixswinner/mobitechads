package com.ads.mobitechadslib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;

import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ads.mobitechadslib.model.Ads;
import com.ads.mobitechadslib.model.AdsResult;
import com.ads.mobitechadslib.model.PublicIP;
import com.ads.mobitechadslib.model.UserLoc;
import com.ads.mobitechadslib.other.AppLocationByIp;
import com.ads.mobitechadslib.other.AppUsageDetails;
import com.ads.mobitechadslib.other.Const;
import com.ads.mobitechadslib.util.SaveSharedPreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobiAdBanner extends  androidx.appcompat.widget.AppCompatImageView
        implements View.OnClickListener {
    public MobiAdBanner(Context context) {
        super(context);
        init(null);
    }
    private Ads BannerAdsResult = null;
    private Ads adsBannerItem;
    public Context context;
    String countryCode = "";
    public static UserLoc userLoc=null;
    public MobiAdBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public MobiAdBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    public  void  init(@Nullable AttributeSet atr){
    }

    public void viewBannerAd(String ad_url_redirect){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(ad_url_redirect));
        context.startActivity(i);
    }

    public void showAd(Context context,String banner_ad_url){
       try {
           Glide.with(context)
                   .asBitmap()
                   .load(banner_ad_url)
                   .into(new BitmapImageViewTarget(this) {
                       @Override
                       protected void setResource(Bitmap resource) {
                           super.setResource(resource);
                       }
                   });
           Log.i("Mobitech Banner Ad ","Loaded Successfully");
       }catch (Exception e){
       }
    }

    @Override
    public void onClick(View v) {
        if(adsBannerItem.getAd_urlandroid()!=null){
            viewBannerAd(adsBannerItem.getAd_urlandroid());
        }else {
            Log.e("Mobitech Banner ","On Click No ad redirect url");
        }
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        Glide.with(this.getContext()).load(background).into(this);
    }

    //has refresh rate in
    public void getBannerAds(Context mcontext, final String applicationId,final String categoryId,
                             int refreshRate){
        context = mcontext;
        fetchBannerAds(applicationId,categoryId);
        final Handler handler = new Handler();
        final int delay = (refreshRate*60000); //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                Log.e("Mobitech Banner ","Refreshing Banner ...");
                fetchBannerAds(applicationId,categoryId);
                handler.postDelayed(this, delay);
            }
        }, delay);
        AppUsageDetails.getInstance(context,applicationId);
    }
    //no refresh rate
    public void getBannerAds(Context mcontext,String applicationId,String categoryId){
        context = mcontext;
        fetchBannerAds(applicationId,categoryId);
    }
    //fetch banner ads
    private void fetchBannerAds(final String applicationId,
                                final String categoryId){

        //String device_ip = new GetIpAddressUtil().getIPAddress(true);
        //Log.e("MY IP ",""+new GetIpAddressUtil().getMyIpAddress(context));

        ApiService.ApiServicePublicIPAddress.Companion.create()
                .getMyPublicIPAddress("json")
                .enqueue(new Callback<PublicIP>() {
                    @Override
                    public void onResponse(Call<PublicIP> call, Response<PublicIP> response) {
                        if (response.isSuccessful()){
                            getAdsForARegion(applicationId,categoryId,response.body().getIp());
                            Log.e("Public IP","==="+response.body().getIp());
                        }
                    }
                    @Override
                    public void onFailure(Call<PublicIP> call, Throwable t) {
                        Log.e("Mobitech Ads Error",
                                "You do not have an active Internet connection");
                    }
                });

    }
    private void getAdsForARegion(final String applicationId,
                                  final String categoryId,
                                  String device_ip){
        if(SaveSharedPreference.getIpAddress(context).equals(device_ip)){
            new Thread()
            {
                @Override
                public void run() {
                    super.run();
                    BannerAdsResult=getTheBannerAds(applicationId,categoryId,
                            SaveSharedPreference.getCountryCode(context));
                    handler.sendEmptyMessage(0);
                }
            }.start();
        }else {
            AppLocationByIp.getInstance(context).getAppLocViaIp(device_ip)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<UserLoc>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }
                        @Override
                        public void onNext(UserLoc userLoc) {
                            SaveSharedPreference.setIpAddress(context,userLoc.getIp());
                            SaveSharedPreference.setCountryCode(context,userLoc.getCountry_code());
                            SaveSharedPreference.setCountryName(context,userLoc.getCountry_name());
                            SaveSharedPreference.setRegionName(context,userLoc.getRegion_name());
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    BannerAdsResult=getTheBannerAds(applicationId,categoryId,
                                            SaveSharedPreference.getCountryCode(context));
                                    handler.sendEmptyMessage(0);
                                }
                            }.start();
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error ",""+e.getMessage());
                            //fetchBannerAds(applicationId,categoryId);
                        }
                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }
    Handler handler = new Handler(Looper.myLooper())
    {
        @Override
        public void handleMessage(Message msg) {
            //Collections.shuffle(BannerAdsResult);
            if (BannerAdsResult!=null){
               if (BannerAdsResult.getAd_urlandroid()!=null){
                    showAd(context,BannerAdsResult.getAd_upload());
                   adsBannerItem = BannerAdsResult;
                }else {
                   Log.e("Mobitech Banner Ad ","Failed to Load");
               }
            }

        }
    };
    public static Ads getTheBannerAds(String applicationId,String categoryId,String countryCode){
        Ads bannerAds=null;
        try {

            AdsResult response =ApiService.Companion
                    .create().getAds(categoryId,applicationId,countryCode)
                    .execute().body();
            if (response.getData()!=null){
                Log.i("Mobitech Banner ","available");
                bannerAds = response.getData();
            }else {
                Log.e("Mobitech Banner ","No ads available");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bannerAds;
    }
    public static void getUserLocationInfo(){
        ApiService.ApiServiceIpAdress.Companion
                .create().getUserLocationUsingIp(new GetIpAddressUtil().getIPAddress(true),
                new Const().ip_stack_api_key)
                .enqueue(new Callback<UserLoc>() {
                    @Override
                    public void onResponse(Call<UserLoc> call, Response<UserLoc> response) {
                        if (response.isSuccessful()){
                              userLoc= response.body();
                        }else{
                            Log.e("Location Response Error",",,,,"+response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<UserLoc> call, Throwable t) {
                        Log.e("Response failure",",,,,"+t.getMessage());

                    }
                });
    }
    //get country code
    public static String getAppCountryCode(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return countryCodeValue.toUpperCase();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if(adsBannerItem.getAd_urlandroid()!=null){
                viewBannerAd(adsBannerItem.getAd_urlandroid());
            }else {
                Log.e("Mobitech Banner ","On Click No ad redirect url");
            }
        }catch (Exception e){
            Log.e("Mobitech Banner ","Not loaded yet");
        }
        return super.onTouchEvent(event);
    }
}
