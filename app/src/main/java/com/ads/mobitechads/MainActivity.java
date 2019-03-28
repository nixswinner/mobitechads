package com.ads.mobitechads;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ads.mobitechadslib.BannerAds;
import com.ads.mobitechadslib.TestClass;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("Banner Ad Url", "onCreate: "+
                BannerAds.getAdsURL(MainActivity.this,"1"));
        /*TestClass.alertDialog(MainActivity.this);
        TestClass.getButtonAction(MainActivity.this);*/

        MyDialog(MainActivity.this);

    }


    public static void MyDialog(Activity activity){
        Dialog dialog = new Dialog(activity,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.show_dialog);
        dialog.setCancelable(true);
        ImageView image = dialog.findViewById(R.id.imageView);
        ImageView imgCancle = dialog.findViewById(R.id.cancle);
        imgCancle.setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.show();

    }
}
