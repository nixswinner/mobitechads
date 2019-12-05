package com.ads.mobitechadslib.other;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ads.mobitechadslib.ApiService;
import com.ads.mobitechadslib.model.AppUsage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppUsageDetails {
    private static AppUsageDetails appUsageDetails;
    private static Context context;
    private static String application_id;
    static SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AppUsageDetails(){
        //Prevent form the reflection api.
        if (appUsageDetails != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    public synchronized static AppUsageDetails getInstance(Context ctx,String app_id){
        if (appUsageDetails == null){ //if there is no instance available... create new one
            appUsageDetails = new AppUsageDetails();
            context = ctx;
            application_id = app_id;
            saveDetails();
        }
        return appUsageDetails;
    }

    public static void saveDetails(){
        String date = dateformatter.format(Calendar.getInstance().getTime());
        AppUsage appUsage = new AppUsage(
                application_id,
                getApplicationName(),
                getAppCountryCode(),
                date,
                getDeviceName(),
                getAndroidRelease());

        ApiService.Companion.create()
                .appUsage("application/json",appUsage)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                        }else {
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }
    //get App Name
    public static String getApplicationName() {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
    //get device
    public  static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return (model);
        } else {
            return (manufacturer) + " " + model;
        }
    }
    //get android version
    public static String getAndroidVersion(){
        int version = Build.VERSION.SDK_INT;
        return String.valueOf(version);
    }
    //get android release
    public static String getAndroidRelease(){
        String versionRelease = Build.VERSION.RELEASE;
        return versionRelease;
    }
    //get App Country Region
    public static String getAppCountryCode(){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return countryCodeValue.toUpperCase();
    }

}
