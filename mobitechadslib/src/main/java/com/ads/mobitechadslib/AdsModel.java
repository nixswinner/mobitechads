package com.ads.mobitechadslib;

import com.google.gson.Gson;

public class AdsModel {

    private String ad_name;
    private String ad_upload;
    private String ad_urlios;
    private String ad_urlandroid;
    private String interstitial_upload;
    private String interstitial_urlandroid;
    private String interstitial_urlios;

    public AdsModel() {
    }

    public AdsModel(String ad_name, String ad_upload, String ad_urlios, String ad_urlandroid, String interstitial_upload, String interstitial_urlandroid, String interstitial_urlios) {
        this.ad_name = ad_name;
        this.ad_upload = ad_upload;
        this.ad_urlios = ad_urlios;
        this.ad_urlandroid = ad_urlandroid;
        this.interstitial_upload = interstitial_upload;
        this.interstitial_urlandroid = interstitial_urlandroid;
        this.interstitial_urlios = interstitial_urlios;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getAd_upload() {
        return ad_upload;
    }

    public void setAd_upload(String ad_upload) {
        this.ad_upload = ad_upload;
    }

    public String getAd_urlios() {
        return ad_urlios;
    }

    public void setAd_urlios(String ad_urlios) {
        this.ad_urlios = ad_urlios;
    }

    public String getAd_urlandroid() {
        return ad_urlandroid;
    }

    public void setAd_urlandroid(String ad_urlandroid) {
        this.ad_urlandroid = ad_urlandroid;
    }

    public String getInterstitial_upload() {
        return interstitial_upload;
    }

    public void setInterstitial_upload(String interstitial_upload) {
        this.interstitial_upload = interstitial_upload;
    }

    public String getInterstitial_urlandroid() {
        return interstitial_urlandroid;
    }

    public void setInterstitial_urlandroid(String interstitial_urlandroid) {
        this.interstitial_urlandroid = interstitial_urlandroid;
    }

    public String getInterstitial_urlios() {
        return interstitial_urlios;
    }

    public void setInterstitial_urlios(String interstitial_urlios) {
        this.interstitial_urlios = interstitial_urlios;
    }

    @Override
    public String toString() {
        return new Gson().toJson((Object)this);
    }
}
