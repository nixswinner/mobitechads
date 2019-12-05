package com.ads.mobitechadslib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ads.mobitechadslib.model.Ads;
import com.ads.mobitechadslib.model.AdsResult;
import com.ads.mobitechadslib.other.AppUsageDetails;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;

public class MobitechAds {
    static AdsModel adsModel = new AdsModel();
    private static Ads adsList = null;
    //show intertistial
    public static void getIntertistialAd(final Activity activity, final String applicationId,
                                         String categoryId) {
        //...........
        ApiService.Companion.create()
                .getAds(categoryId,applicationId)
                .enqueue(new Callback<AdsResult>() {
                    @Override
                    public void onResponse(Call<AdsResult> call,
                                           retrofit2.Response<AdsResult> response) {
                        if (response.isSuccessful()){
                            Log.i("Mobitech Intertistial","Ad Loaded successfully ");
                            adsList=response.body().getData();
                            populateAdsList(response.body().getData(),activity);

                            AppUsageDetails.getInstance(activity,applicationId);

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

}
