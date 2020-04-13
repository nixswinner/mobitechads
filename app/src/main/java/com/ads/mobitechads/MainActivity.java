package com.ads.mobitechads;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.ads.mobitechadslib.AdsModel;
import com.ads.mobitechadslib.MobiAdBanner;
import com.ads.mobitechadslib.MobitechAds;
import com.ads.mobitechadslib.model.AdsCategory;

public class MainActivity extends AppCompatActivity {
    private AdsModel adsModel ;
    private MobiAdBanner mobiAdBanner;
    //AdsCategory Category
    /*
    Mobi Ads Categories
        News
        Entertainment
        Sport
        Business
        Games
        Education
        TestAds - development purpose
    * */
    private String adCategory= AdsCategory.TestAds;
    private float BannerRefresh = 20;//default 20 seconds
    private Context context;
    private Button btnLoadVideoAd,btnBannerAd,btnIntertistialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        // ----------------------Banner Ad initilization--------------------.
        mobiAdBanner = findViewById(R.id.bannerAd);

        btnBannerAd = findViewById(R.id.btnBannnerAd);
        btnIntertistialAd = findViewById(R.id.btnIntertistialAd);
        btnLoadVideoAd = findViewById(R.id.btnVideoAd);

        btnLoadVideoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVideoAd();
            }
        });

        btnBannerAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBannerAd();
            }
        });

        btnIntertistialAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIntertistialAd();
            }
        });

        //Refreshing banner
        //For refreshing banner add refreshRate in minutes after category id
        //refresh rate in minutes
        /*mobiAdBanner.getBannerAds(context,applicationId,
                adCategory,1);*/

       //...............................end of banner ad ........................
    }

    private void loadBannerAd(){
        showToast("Loading Mobitech Banner Ad");
        mobiAdBanner.setVisibility(View.VISIBLE);
        mobiAdBanner.getBannerAds(context,getString(R.string.mobi_app_id),
                adCategory,1);
    }

    // ....................Intertistial Ad ...............
    private void loadIntertistialAd(){
        showToast("Loading Mobitech Intertistial Ad");
        MobitechAds.getIntertistialAd(
                MainActivity.this,
                getString(R.string.mobi_app_id),
                adCategory);
        // ...................End of Intertistial ad............
    }

    //----------------video ad------------------------------
    private void loadVideoAd(){
        showToast("Loading Mobitech video Advertisement");
        MobitechAds.loadVideoAd(
                MainActivity.this,
                getString(R.string.mobi_app_id),
                adCategory);
        //----------------end video ad---------------------------

    }

    private void showToast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
