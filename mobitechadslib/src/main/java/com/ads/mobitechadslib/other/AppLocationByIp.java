package com.ads.mobitechadslib.other;

import android.content.Context;
import android.util.Log;

import com.ads.mobitechadslib.ApiService;
import com.ads.mobitechadslib.GetIpAddressUtil;
import com.ads.mobitechadslib.model.PublicIP;
import com.ads.mobitechadslib.model.UserLoc;
import com.ads.mobitechadslib.util.SaveSharedPreference;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppLocationByIp {

    private static AppLocationByIp appLocationByIp;
    private static String country_code;
    public static UserLoc userLoc=null;
    private static Context context;
    private AppLocationByIp(){
        //Prevent from the reflection api.
        if (appLocationByIp != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    public synchronized static AppLocationByIp getInstance(Context ctx){
        if (appLocationByIp == null){ //if there is no instance available... create new one
            appLocationByIp = new AppLocationByIp();
            context =ctx;
        }
        return appLocationByIp;
    }
    public static void getAppLoc(){
        ApiService.ApiServiceIpAdress.Companion
                .create().getUserLocationUsingIp(new GetIpAddressUtil().getIPAddress(true),
                new Const().ip_stack_api_key)
                .enqueue(new Callback<UserLoc>() {
                    @Override
                    public void onResponse(Call<UserLoc> call, Response<UserLoc> response) {
                        if (response.isSuccessful()){
                            userLoc= response.body();
                            country_code = userLoc.getCountry_code();

                            SaveSharedPreference.setIpAddress(context,userLoc.getIp());
                            SaveSharedPreference.setCountryCode(context,userLoc.getCountry_code());
                            SaveSharedPreference.setCountryName(context,userLoc.getCountry_name());
                            SaveSharedPreference.setRegionName(context,userLoc.getRegion_name());

                        }else{
                            Log.e("Location Response Error",",,,,"+response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<UserLoc> call, Throwable t) {
                        Log.e("Response failure",",,,,"+t.getMessage());

                    }
                });
    }

    public static Observable<UserLoc> getAppLocViaIp(String device_ip){
        return ApiService.ApiServiceIpAdress.Companion.create()
                .getUserLocByIp(device_ip,
                        new Const().ip_stack_api_key);
    }
    /*public static Observable<PublicIP>getMyPublicIPAddress(){
        return ApiService.ApiServicePublicIPAddress.Companion.create()
                .getMyPublicIPAddress("json");
    }*/

    public static UserLoc getAppLocation(){
        return userLoc;
    }

    public static String getAppUsageCountryCode(){
        return country_code;
    }

}
