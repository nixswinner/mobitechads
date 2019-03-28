package com.ads.mobitechadslib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MobitechAds {

    static AdsModel adsModel = new AdsModel();
    public static void getAllAds(Activity activity,String categoryId){
        getBannerAd(categoryId);
        getIntertistialAd(activity,categoryId);
    }
    public static void getIntertistialAd(Activity activity,String categoryId) {
        Server.getInstance(new OnProgressListener() {
            @Override
            public void onFailure() {
                activity.runOnUiThread( () -> { });
            }
            @Override
            public void onResponse(final String response, final boolean success) {
                activity.runOnUiThread(() -> {
                    if (success) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getJSONObject("fetch_status").get("response").equals("0")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                JSONObject banner = (JSONObject) jsonArray.get(0);
                                JSONObject interstitial = (JSONObject) jsonArray.get(1);
                                adsModel.setAd_name(String.valueOf(banner.get("ad_name")));
                                adsModel.setAd_upload(String.valueOf(banner.get("ad_upload")));
                                adsModel.setAd_urlandroid(String.valueOf(banner.get("ad_urlandroid")));
                                adsModel.setAd_urlios(String.valueOf(banner.get("ad_urlios")));
                                adsModel.setInterstitial_upload(String.valueOf(interstitial.get("interstitial_upload")));
                                adsModel.setInterstitial_urlandroid(String.valueOf(interstitial.get("interstitial_urlandroid")));
                                adsModel.setInterstitial_urlios(String.valueOf(interstitial.get("interstitial_urlios")));
                                //show intertistial
                                showIntertistial(activity,adsModel.getInterstitial_upload(),
                                        adsModel.getAd_urlandroid());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }

            @Override
            public void onStart() {
            }
        }).request("http://ads.mobitechtechnologies.com/api/serve_ads/"+categoryId);
    }

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
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(click_url_redirect));
            activity.startActivity(i);
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
                    subscriber.onError(e);
                }
            }
        });
    }
    public AdsModel getBannerAdValues(String response){
        AdsModel adsModel = new AdsModel();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getJSONObject("fetch_status").get("response").equals("0")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                JSONObject banner = (JSONObject) jsonArray.get(0);
                adsModel.setAd_name(String.valueOf(banner.get("ad_name")));
                adsModel.setAd_upload(String.valueOf(banner.get("ad_upload")));
                adsModel.setAd_urlandroid(String.valueOf(banner.get("ad_urlandroid")));
                adsModel.setAd_urlios(String.valueOf(banner.get("ad_urlios")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return adsModel;
    }
}
