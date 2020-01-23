package com.ads.mobitechadslib

import androidx.lifecycle.LiveData
import com.ads.mobitechadslib.model.AdsResult
import com.ads.mobitechadslib.model.AppUsage
import com.ads.mobitechadslib.model.PublicIP
import com.ads.mobitechadslib.model.UserLoc
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



interface ApiService {
    companion object {
        fun create(): ApiService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
          /* val client = OkHttpClient.Builder()
                    .addInterceptor(LogJsonInterceptor())
                    .build()*/
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("https://ads.mobitechads.com/api/")
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    interface ApiServiceIpAdress {
        companion object {
            fun create(): com.ads.mobitechadslib.ApiService {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC
//            val client = OkHttpClient.Builder()
//                    .addInterceptor(LogJsonInterceptor())
//                    .build()
                val retrofit = Retrofit.Builder()
                        .addCallAdapterFactory(
                                RxJava2CallAdapterFactory.create())
                        .addConverterFactory(
                                GsonConverterFactory.create())
                        .baseUrl("https://api.ipstack.com")
                        .build()
                return retrofit.create(com.ads.mobitechadslib.ApiService::class.java)
            }
        }
    }

    interface ApiServicePublicIPAddress {
        companion object {
            fun create(): com.ads.mobitechadslib.ApiService {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC
//            val client = OkHttpClient.Builder()
//                    .addInterceptor(LogJsonInterceptor())
//                    .build()
                val retrofit = Retrofit.Builder()
                        .addCallAdapterFactory(
                                RxJava2CallAdapterFactory.create())
                        .addConverterFactory(
                                GsonConverterFactory.create())
                        .baseUrl("https://api.ipify.org")
                        .build()
                return retrofit.create(com.ads.mobitechadslib.ApiService::class.java)
            }
        }
    }

    //get public ip https://api.ipify.org?format=json
    //get all the subjects
    @GET("serve_ads/{category_id}/{app_id}/{country_code}")
    fun getAds(@Path("category_id") categoryId:String,
               @Path("app_id") app_id:String,
               @Path("country_code")country_code:String): Call<AdsResult>

    //post app details
    @POST("app_usage/")
    fun appUsage(@Header("Content-Type") type:String,
                 @Header("Accept") accept:String,
                 @Body usage: AppUsage):Call<Void>

    @GET("serve_ads/{category_id}")
    fun getAdsLiveData(@Path("category_id") categoryId:String): LiveData<AdsResult>

    //get Ip Address Data
    @GET("/{ip_adress}")
    fun getUserLocationUsingIp(@Path("ip_adress") IPAddress:String,
                               @Query("access_key")access_key:String):Call<UserLoc>

    @GET("/{ip_adress}")
    fun getUserLocByIp(@Path("ip_adress") IPAddress:String,
                               @Query("access_key")access_key:String):Observable<UserLoc>

    @GET("/")
    fun getMyPublicIPAddress(@Query("format")format:String):Call<PublicIP>




}