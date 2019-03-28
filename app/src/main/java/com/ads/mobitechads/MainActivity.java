package com.ads.mobitechads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ads.mobitechadslib.BannerAds;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("Banner Ad Url", "onCreate: "+BannerAds.getAdsURL(MainActivity.this,"1"));

    }
}
