package com.ads.mobitechadslib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ads.mobitechadslib.model.Ads;
import com.ads.mobitechadslib.model.AdsResult;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobitechAds {

    static AdsModel adsModel = new AdsModel();
    private static Ads adsList = null;
    private List<Ads> BannerAdsResult = new ArrayList();
    private Context bannerContext;
    private MobiAdBanner bannerAdObject;
    private String country_code;
    //show intertistial
    public static void getIntertistialAd(final Activity activity, String applicationId,
                                         String categoryId) {

        //...........
        ApiService.Companion.create()
                .getAds(categoryId,applicationId)
                .enqueue(new Callback<AdsResult>() {
                    @Override
                    public void onResponse(Call<AdsResult> call,
                                           retrofit2.Response<AdsResult> response) {
                        if (response.isSuccessful()){
                            Log.e("Mobitech Intertistial","Ad Loaded successfully ");
                            adsList=response.body().getData();
                            populateAdsList(response.body().getData(),activity);
                            Log.e("App Name ","---"+getApplicationName(activity));
                            Log.e("App Phone Manufacturer ","---"+getDeviceName());
                            Log.e("App Country  ","---"+getAppCountryCode(activity));

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
    private static void populateAdsList(Ads adList,Activity activity){
        if (adList!=null){
            //show intertistial
            showIntertistial(activity, adList.getInterstitial_upload(),
                    adList.getInterstitial_urlandroid());
        }
    }
    // show intertistial ad
    public static void showIntertistial(final Activity activity, String ad_imageUrl,
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
    }
    //banner ads .....

    //get App Name
    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
    //get device
    public  static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return (model);
        } else {
            return (manufacturer) + " " + model;
        }
    }
    //get App Country Region
    public static String getAppCountryCode(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return countryCodeValue.toUpperCase();
    }
}
