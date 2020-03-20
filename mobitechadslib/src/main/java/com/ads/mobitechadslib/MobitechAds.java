package com.ads.mobitechadslib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ads.mobitechadslib.model.Ads;
import com.ads.mobitechadslib.model.AdsResult;
import com.ads.mobitechadslib.model.PublicIP;
import com.ads.mobitechadslib.model.UserLoc;
import com.ads.mobitechadslib.other.AppLocationByIp;
import com.ads.mobitechadslib.other.AppUsageDetails;
import com.ads.mobitechadslib.util.SaveSharedPreference;
import com.bumptech.glide.Glide;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobitechAds {
    static AdsModel adsModel = new AdsModel();
    private static Ads adsList = null;
    private static Dialog intertistial=null;
    static boolean adLoaded=false;
    //show intertistial
    public static void getIntertistialAd(final Activity activity, final String applicationId,
                                         final String categoryId) {
        //...........

        //String device_ip = new GetIpAddressUtil().getIPAddress(true);
        ApiService.ApiServicePublicIPAddress.Companion.create()
                .getMyPublicIPAddress("json")
                .enqueue(new Callback<PublicIP>() {
                    @Override
                    public void onResponse(Call<PublicIP> call, Response<PublicIP> response) {
                        if (response.isSuccessful()){
                            getAdsForARegion(activity,applicationId,categoryId,response.body().getIp());
                        }
                    }
                    @Override
                    public void onFailure(Call<PublicIP> call, Throwable t) {
                        Log.e("Mobitech Ads Error",
                                "You do not have an active Internet connection");
                    }
                });
    }

    private static void getAdsForARegion(
            final Activity activity,
            final String applicationId,
            final String categoryId, final String device_ip) {

        if(SaveSharedPreference.getIpAddress(activity).equals(device_ip)){
            showIntertistialAd(categoryId,applicationId,
                    SaveSharedPreference.getCountryCode(activity),
                    activity);
        }else {
            AppLocationByIp.getInstance(activity).getAppLocViaIp(device_ip)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<UserLoc>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }
                        @Override
                        public void onNext(UserLoc userLoc) {
                            SaveSharedPreference.setIpAddress(activity, userLoc.getIp());
                            SaveSharedPreference.setCountryCode(activity, userLoc.getCountry_code());
                            SaveSharedPreference.setCountryName(activity, userLoc.getCountry_name());
                            SaveSharedPreference.setRegionName(activity, userLoc.getRegion_name());
                            showIntertistialAd(categoryId,applicationId,
                                    SaveSharedPreference.getCountryCode(activity),
                                    activity);
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error ", "" + e.getMessage());
                        }
                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    private static void showIntertistialAd(String categoryId,
                                           final String applicationId,
                                           final String country_code,
                                           final Activity activity){
        ApiService.Companion.create()
                .getAds(categoryId,applicationId,
                        country_code)
                .enqueue(new Callback<AdsResult>() {
                    @Override
                    public void onResponse(Call<AdsResult> call,
                                           retrofit2.Response<AdsResult> response) {
                        if (response.isSuccessful()){
                            Log.i("Mobitech Intertistial","Ad Loaded successfully ");
                            adsList=response.body().getData();
                            if (response.body().getData() !=null){
                                populateAdsList(response.body().getData(),activity);
                                AppUsageDetails.getInstance(activity,applicationId,country_code);
                            }

                        }else {
                            Log.e("Mobitech Intertistial","Ad Failed to Load ");
                        }
                    }
                    @Override
                    public void onFailure(Call<AdsResult> call, Throwable t) {
                        Log.e("Mobitech Intertistial","Ad Could not load.No Internet Connections");
                    }
                });
    }
    private static  void populateAdsList(Ads adList,Activity activity){
        if(adList.getInterstitial_upload()!=null){
            showIntertistial(activity, adList.getInterstitial_upload(),
                    adList.getInterstitial_urlandroid());
        }
    }
    // show intertistial ad
    public static Dialog showIntertistial(final Activity activity, String ad_imageUrl,
                                        final String click_url_redirect){
        final Dialog dialog = new Dialog(activity,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.intertistial_dialog);
        dialog.setCancelable(true);
        ImageView image = dialog.findViewById(R.id.imageView);
        ImageView imgCancle = dialog.findViewById(R.id.cancle);
        //show image
        Glide.with(activity)
                .load(ad_imageUrl).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click open browser
                if (click_url_redirect!=null){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(click_url_redirect));
                    activity.startActivity(i);
                }else {
                    Log.e("Mobitech Intertistial ","Ad Error Null Exception on Ad Url ");
                }
            }
        });
        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        adLoaded=true;
        return dialog;
    }

    //.........video ad ---------

    //load video ad
    public static void loadVideoAd(final Activity activity, final String applicationId,
                                   final String categoryId) {
       ApiService.ApiServicePublicIPAddress.Companion.create()
                .getMyPublicIPAddress("json")
                .enqueue(new Callback<PublicIP>() {
                    @Override
                    public void onResponse(Call<PublicIP> call, Response<PublicIP> response) {
                        if (response.isSuccessful()){
                            getVideoAdsForARegion(activity,applicationId,categoryId,response.body().getIp());
                        }
                    }
                    @Override
                    public void onFailure(Call<PublicIP> call, Throwable t) {
                        Log.e("Mobitech Ads Error",
                                "You do not have an active Internet connection");
                    }
                });
    }

    private static void getVideoAdsForARegion(
            final Activity activity,
            final String applicationId,
            final String categoryId, final String device_ip) {

        if(SaveSharedPreference.getIpAddress(activity).equals(device_ip)){
            showIntertistialAd(categoryId,applicationId,
                    SaveSharedPreference.getCountryCode(activity),
                    activity);
        }else {
            AppLocationByIp.getInstance(activity).getAppLocViaIp(device_ip)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<UserLoc>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }
                        @Override
                        public void onNext(UserLoc userLoc) {
                            SaveSharedPreference.setIpAddress(activity, userLoc.getIp());
                            SaveSharedPreference.setCountryCode(activity, userLoc.getCountry_code());
                            SaveSharedPreference.setCountryName(activity, userLoc.getCountry_name());
                            SaveSharedPreference.setRegionName(activity, userLoc.getRegion_name());
                            showIntertistialAd(categoryId,applicationId,
                                    SaveSharedPreference.getCountryCode(activity),
                                    activity);
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error ", "" + e.getMessage());
                        }
                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    private static void showVideoAd(String categoryId,
                                    final String applicationId,
                                    final String country_code,
                                    final Activity activity){
        ApiService.Companion.create()
                .getAds(categoryId,applicationId,
                        country_code)
                .enqueue(new Callback<AdsResult>() {
                    @Override
                    public void onResponse(Call<AdsResult> call,
                                           retrofit2.Response<AdsResult> response) {
                        if (response.isSuccessful()){
                            Log.i("Mobitech VideoAd","Ad Loaded successfully ");
                            adsList=response.body().getData();
                            if (response.body().getData() !=null){
                                populateVideoAdsList(response.body().getData(),activity);
                                AppUsageDetails.getInstance(activity,applicationId,country_code);
                            }

                        }else {
                            Log.e("Mobitech VideoAd","Ad Failed to Load ");
                        }
                    }
                    @Override
                    public void onFailure(Call<AdsResult> call, Throwable t) {
                        Log.e("Mobitech VideoAd","Ad Could not load.No Internet Connections");
                    }
                });
    }

    private static  void populateVideoAdsList(Ads adList,Activity activity){
        if(adList.getVideo()!=null){
            showVideoAd(activity, adList.getVideo(),
                    adList.getInterstitial_urlandroid());
        }
    }

    //-------end video ad.--------
    public static Dialog showVideoAd(final Activity activity, String ad_imageUrl,
                                          final String click_url_redirect){
        final Dialog dialog = new Dialog(activity,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.video_ad_display);
        dialog.setCancelable(true);
        VideoView videoView = dialog.findViewById(R.id.videoView);
        ImageView imgCancle = dialog.findViewById(R.id.cancle);
        //display video
        videoView.setVideoPath("http://techslides.com/demos/sample-videos/small.mp4");
        videoView.start();
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click open browser
                if (click_url_redirect!=null){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(click_url_redirect));
                    activity.startActivity(i);
                }else {
                    Log.e("Mobitech Video ","Ad Error Null Exception on Ad Url ");
                }
            }
        });
        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        adLoaded=true;
        return dialog;

    }


    public static String getAppCountryCode(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return countryCodeValue.toUpperCase();
    }


}
