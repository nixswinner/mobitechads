package com.ads.mobitechads;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ads.mobitechadslib.AdsModel;
import com.ads.mobitechadslib.MobiAdBanner;
import com.ads.mobitechadslib.MobitechAds;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.ads.mobitechadslib.MobitechAds.getBannerAd;
import static com.ads.mobitechadslib.MobitechAds.getBannerAdValues;


public class MainActivity extends AppCompatActivity {
    private AdsModel adsModel ;
    private MobiAdBanner mobiAdBanner;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String adCategory="3";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

       // ....................Intertistial Ad ...............
       MobitechAds.getIntertistialAd(
                MainActivity.this,
               adCategory);
       // ...................End of Intertistial ad............


      // ----------------------Banner Ad --------------------.
       mobiAdBanner = findViewById(R.id.bannerAd);
        mobiAdBanner.setOnClickListener(v -> {
            mobiAdBanner.viewBannerAd(MainActivity.this,
                    adsModel.getAd_urlandroid());
        });
        //change the category id here
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                // whatever you need to do every 2 seconds
                Observable<Response> observable = getBannerAd(adCategory);
                observable.subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response response) {
                        try {
                            Log.e("Observer", "onNext: " );
                            adsModel = getBannerAdValues(response.body().string());
                            mobiAdBanner.showAd(context,
                                    adsModel.getAd_upload());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Observer", "onError : "+e.getMessage() );
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
        };
        timer.schedule(myTask, 100, 5000);


        Observable<Response> observable = getMyBannerAd(adCategory);
        observable.interval(5,TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Long aLong) {

                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });

       //...............................end of banner ad ........................


        Observable<String> myobStringObservable = getMydata();
        myobStringObservable.interval(1,TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        /*myobStringObservable.subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                Log.e("MyObservable", "onNext: "+s );

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("MyObservable", "onError: "+e.getMessage() );
                            }

                            @Override
                            public void onComplete() {

                            }
                        });*/
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void getAd(String cat){

    }
    public Observable<String> getMydata(){
        return Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        emitter.onNext("Hello World.Rx Here");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public  static Observable<Response> getMyBannerAd(String categoryId) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://ads.mobitechtechnologies.com/api/serve_ads/"+categoryId)
                .get()
                .build();
        return Observable
                .create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> subscriber) throws Exception {
                try {
                    Response response = client.newCall(request).execute();
                    subscriber.onNext(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
