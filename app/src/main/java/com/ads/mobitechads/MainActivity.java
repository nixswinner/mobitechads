package com.ads.mobitechads;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ads.mobitechadslib.AdsModel;
import com.ads.mobitechadslib.MobitechAds;
import com.ads.mobitechadslib.OnProgressListener;
import com.ads.mobitechadslib.Server;
import com.ads.mobitechadslib.TestClass;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.concurrent.Callable;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private AdsModel adsModel ;
    private MobiAdBanner mobiAdBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("Banner Ad Url", "onCreate: " +
                MobitechAds.getBannerAd(MainActivity.this, "1"));

       // ....................Intertistial Ad ...............
       MobitechAds.getIntertistialAd(
                MainActivity.this,
                "1");
       // ...................End of Intertistial ad............


      // ----------------------Banner Ad --------------------.
       mobiAdBanner = findViewById(R.id.bannerAd);
        mobiAdBanner.setOnClickListener(v -> {
            mobiAdBanner.viewBannerAd(MainActivity.this,
                    adsModel.getAd_urlandroid());
        });
        //change the category id here
       Observable<Response> observable = getBannerAds("1");
       observable.subscribe(new Observer<Response>() {
           @Override
           public void onSubscribe(Disposable d) {
           }
           @Override
           public void onNext(Response response) {
               try {
                   adsModel = getAdsValues(response.body().string());
                   mobiAdBanner.showAd(MainActivity.this,
                           adsModel.getAd_upload());
               } catch (IOException e) {
                   e.printStackTrace();
               }
          }
           @Override
           public void onError(Throwable e) {
           }
           @Override
           public void onComplete() {
           }
       });

       //...............................end of banner ad ........................
    }

    public AdsModel getAdsValues(String response){
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


    public static Observable<Response> getBannerAds(String categoryId) {
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
}
