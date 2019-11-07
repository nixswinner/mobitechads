package com.ads.mobitechadslib

import android.arch.lifecycle.LiveData
import com.ads.mobitechadslib.model.Ads
import com.ads.mobitechadslib.model.AdsResult
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("http://ads.mobitechtechnologies.com/api/")
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }
    //get all the subjects
    @GET("serve_ads/{category_id}")
    fun getAds(@Path("category_id") categoryId:String): Call<AdsResult>

    @GET("serve_ads/{category_id}")
    fun getAdsLiveData(@Path("category_id") categoryId:String): LiveData<AdsResult>



}