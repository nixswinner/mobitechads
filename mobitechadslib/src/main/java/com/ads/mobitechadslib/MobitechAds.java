package com.ads.mobitechadslib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ads.mobitechadslib.model.Ads;
import com.ads.mobitechadslib.model.AdsResult;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MobitechAds {

    static AdsModel adsModel = new AdsModel();
    private static List<Ads> adsList = new ArrayList();
    private List<Ads> BannerAdsResult = new ArrayList();
    private Context bannerContext;
    private MobiAdBanner bannerAdObject;

    //show intertistial
    public static void getIntertistialAd(final Activity activity, String categoryId) {
        ApiService.Companion.create()
                .getAds(categoryId)
                .enqueue(new Callback<AdsResult>() {
                    @Override
                    public void onResponse(Call<AdsResult> call, retrofit2.Response<AdsResult> response) {
                        if (response.isSuccessful()){
                            Log.e("Mobitech Intertistial","Ad Loaded successfully");
                            adsList.addAll(response.body().getData());
                            populateAdsList(response.body().getData(),activity);
                        }else {
                            Log.e("Mobitech Intertistial","Ad Failed to Load "+response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<AdsResult> call, Throwable t) {
                        Log.e("Mobitech Intertistial","Ad Could not load.No Internet Connections");
                    }
                });
    }
    private static void populateAdsList(List<Ads> adList,Activity activity){
        Collections.shuffle(adList);//shuffle list
        if (adList.size()>0){
            //show intertistial
            showIntertistial(activity, adList.get(0).getInterstitial_upload(),
                    adList.get(0).getInterstitial_urlandroid());
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
