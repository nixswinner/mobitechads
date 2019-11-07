package com.ads.mobitechads;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ads.mobitechadslib.AdsModel;
import com.ads.mobitechadslib.MobiAdBanner;
import com.ads.mobitechadslib.MobitechAds;

import io.reactivex.disposables.CompositeDisposable;


public class MainActivity extends AppCompatActivity {
    private AdsModel adsModel ;
    private MobiAdBanner mobiAdBanner;
    private String adCategory="2";
    private float BannerRefresh = 20;//default 20 seconds
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;


       // ....................Intertistial Ad ...............
       MobitechAds.getIntertistialAd(
                MainActivity.this,
               adCategory);
       // ...................End of Intertistial ad............


      // ----------------------Banner Ad --------------------.
       mobiAdBanner = findViewById(R.id.bannerAd);
       /*mobiAdBanner.getBannerAds(context,
               adCategory);*/

        //Refreshing banner
        //For refreshing banner add refreshRate in minutes after category id
        //refresh rate in minutes
        mobiAdBanner.getBannerAds(context,
                adCategory,1);

       //...............................end of banner ad ........................




    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
