package com.ads.mobitechadslib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nix on 3/10/18.
 */


public class SaveSharedPreference {
    static final String PREF_IP_ADDRESS = "ip_address";
    static final String PREF_COUNTRY_CODE = "country_code";
    static final String PREF_COUNTRY_NAME = "country_name";
    static final String PREF_REGION_NAME = "region_name";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setCountryName(Context ctx, String country_name) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_COUNTRY_NAME, country_name);
        editor.commit();
    }

    public static void setCountryCode(Context ctx, String country_code) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_COUNTRY_CODE, country_code);
        editor.commit();
    }

    public static void setIpAddress(Context ctx, String ip_address) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_IP_ADDRESS, ip_address);
        editor.commit();
    }
    public static void setRegionName(Context ctx, String region_name) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_REGION_NAME, region_name);
        editor.commit();
    }


    public static String getIpAddress(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_IP_ADDRESS, "");
    }

    public static String getCountryCode(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_COUNTRY_CODE, "KE");
    }
    public static String getCountryName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_COUNTRY_NAME, "");
    }
    public static String getRegionName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_REGION_NAME, "");
    }




    //logging out
    public static void cleardata(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

}

