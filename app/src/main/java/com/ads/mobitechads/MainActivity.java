package com.ads.mobitechads;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;


import com.ads.mobitechadslib.AdsModel;
import com.ads.mobitechadslib.MobiAdBanner;
import com.ads.mobitechadslib.MobitechAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
      /* Observable<Response> observable = getBannerAds("1");
       observable.subscribe(new Observer<Response>() {
           @Override
           public void onSubscribe(Disposable d) {
           }
           @Override
           public void onNext(Response response) {
               try {
                   adsModel = getBannerAdValues(response.body().string());
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
       });*/

       //...............................end of banner ad ........................
    }

}
