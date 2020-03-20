package com.ads.mobitechads;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ads.mobitechadslib.AdsModel;
import com.ads.mobitechadslib.MobiAdBanner;
import com.ads.mobitechadslib.MobitechAds;

public class MainActivity extends AppCompatActivity {
    private AdsModel adsModel ;
    private MobiAdBanner mobiAdBanner;
    private String adCategory="5";
    private String applicationId="562024";
    private float BannerRefresh = 20;//default 20 seconds
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

       // ....................Intertistial Ad ...............
      /* MobitechAds.getIntertistialAd(
                MainActivity.this,
               applicationId,
               adCategory);
       // ...................End of Intertistial ad............
       */
        //----------------video ad------------------------------
        MobitechAds.loadVideoAd(
                MainActivity.this,
                applicationId,
                adCategory);
        //----------------end video ad---------------------------


      // ----------------------Banner Ad --------------------.
      /* mobiAdBanner = findViewById(R.id.bannerAd);
       *//*mobiAdBanner.getBannerAds(context,
               adCategory);*//*

        //Refreshing banner
        //For refreshing banner add refreshRate in minutes after category id
        //refresh rate in minutes
        mobiAdBanner.getBannerAds(context,applicationId,
                adCategory,1);

       //...............................end of banner ad ........................
*/



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
