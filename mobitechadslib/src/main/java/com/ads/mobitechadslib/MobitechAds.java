package com.ads.mobitechadslib;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.ads.mobitechadslib.model.Ads;
import com.ads.mobitechadslib.model.AdsResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MobitechAds {

    static AdsModel adsModel = new AdsModel();
    private static List<Ads> adsList = new ArrayList();
    public static void getAllAds(Activity activity,String categoryId){
        getBannerAd(categoryId);
        getIntertistialAd(activity,categoryId);
    }
    private List<Ads> BannerAdsResult = new ArrayList();
    private Context bannerContext;
    private MobiAdBanner bannerAdObject;

    //show intertistial
    public static void getIntertistialAd(Activity activity,String categoryId) {
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
    public static void showIntertistial(Activity activity,String ad_imageUrl,
                                        String click_url_redirect){
        Dialog dialog = new Dialog(activity,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.intertistial_dialog);
        dialog.setCancelable(true);
        ImageView image = dialog.findViewById(R.id.imageView);
        ImageView imgCancle = dialog.findViewById(R.id.cancle);
        //show image
        Glide.with(activity)
                .load(ad_imageUrl).into(image);
        image.setOnClickListener(v->{
            //on click open browser
            if (click_url_redirect!=null){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(click_url_redirect));
                activity.startActivity(i);
            }else {
                Log.e("Mobitech Intertistial ","Ad Error Null Exception on Ad Url ");
            }

        });
        imgCancle.setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.show();
    }
    //banner ads .....

    public static Observable<Response> getBannerAd(String categoryId) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://ads.mobitechtechnologies.com/api/serve_ads/"+categoryId)
                .get()
                .build();
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> subscriber) throws Exception {
                try {
                    Response response = client.newCall(request).execute();
                    subscriber.onNext(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
