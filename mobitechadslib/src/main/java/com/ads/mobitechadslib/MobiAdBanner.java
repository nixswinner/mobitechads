package com.ads.mobitechadslib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ads.mobitechadslib.model.Ads;
import com.ads.mobitechadslib.model.AdsResult;
import com.ads.mobitechadslib.other.AppUsageDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MobiAdBanner extends  android.support.v7.widget.AppCompatImageView
        implements View.OnClickListener {
    public MobiAdBanner(Context context) {
        super(context);
        init(null);
    }
    private Ads BannerAdsResult = null;
    private Ads adsBannerItem;
    private Context context;
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
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                BannerAdsResult=getTheBannerAds(applicationId,categoryId);
                handler.sendEmptyMessage(0);
            }
        }.start();
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
    public static Ads getTheBannerAds(String applicationId,String categoryId){
        Ads bannerAds=null;
        try {

            AdsResult response =ApiService.Companion
                    .create().getAds(categoryId,applicationId)
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(adsBannerItem.getAd_urlandroid()!=null){
            viewBannerAd(adsBannerItem.getAd_urlandroid());
        }else {
            Log.e("Mobitech Banner ","On Click No ad redirect url");
        }
        return super.onTouchEvent(event);
    }
}
