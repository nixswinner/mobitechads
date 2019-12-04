package com.ads.mobitechadslib.model

import com.google.gson.annotations.SerializedName

data class AdsResult(var data:Ads,
                     @SerializedName("fetch_status") val fetch_status : Fetch_status)

data class Fetch_status (
        @SerializedName("response") val response : Int
)