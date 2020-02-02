package com.ads.mobitechadslib.model

import com.google.gson.annotations.SerializedName

data class AppUsage(@SerializedName("app_id")var app_id:String,
                    @SerializedName("app_name")var app_name:String,
                    @SerializedName("region")var region:String,
                    @SerializedName("date_opened")var date_opened:String,
                    @SerializedName("phone_type")var phone_type:String,
                    @SerializedName("android_os")var android_os:String)
