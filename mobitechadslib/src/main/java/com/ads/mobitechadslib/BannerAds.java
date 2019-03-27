package com.ads.mobitechadslib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BannerAds {
    AdsModel adsModel = new AdsModel();
    private String getAdsURL(Activity activity,String categoryId) {
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

                                Log.i("tag", "<======= ==========>");
                                Log.i("tag", "run: "+adsModel.toString());
                                Log.i("tag", "<======= ==========>");

                                //Glide.with(getApplicationContext()).load(adsModel.getAd_upload()).into(imageView);

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
        return adsModel.getAd_upload();
    }
}
