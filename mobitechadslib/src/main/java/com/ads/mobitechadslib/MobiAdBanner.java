package com.ads.mobitechadslib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

public class MobiAdBanner extends android.support.v7.widget.AppCompatImageView
        implements View.OnClickListener {
    public MobiAdBanner(Context context) {
        super(context);
        init(null);
    }
    public MobiAdBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public MobiAdBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    public  void  init(@Nullable AttributeSet atr){
    }

    public void viewBannerAd(Context context,String ad_url_redirect){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(ad_url_redirect));
        context.startActivity(i);
    }

    public void showAd(Context context,String banner_ad_url){
        Glide.with(context)
                .asBitmap()
                .load(banner_ad_url)
                .into(new BitmapImageViewTarget(this) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    }
                });

    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        Glide.with(this.getContext()).load(background).into(this);
    }
}
