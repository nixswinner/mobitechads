package com.ads.mobitechadslib;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TestClass extends AppCompatActivity {
   // private RelativeLayout relativeLayout;


    public static void getButtonAction(Activity activity){
        Button fab = new Button(activity);
        RelativeLayout.LayoutParams rel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rel.setMargins(15, 15, 15, 15);
        rel.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rel.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fab.setLayoutParams(rel);
        fab.setText("My Button");


    }

    public static void alertDialog(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity
                ,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        alertDialog.show();
    }
}
