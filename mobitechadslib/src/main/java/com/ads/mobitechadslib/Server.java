package com.ads.mobitechadslib;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class Server {

    private Callback callback = new C09251();
    private OkHttpClient okHttpClient;
    private OnProgressListener onProgressListener;
    private Request request;

    class C09251 implements Callback {
        C09251() {
        }

        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            if (Server.this.onProgressListener != null) {
                Server.this.onProgressListener.onFailure();
            }
        }

        public void onResponse(Call call, Response response) throws IOException {
            Log.i(TAG, "onResponse: "+response);
            String body = response.body().string();
            if (Server.this.onProgressListener != null) {
                Server.this.onProgressListener.onResponse(body, response.isSuccessful());
            }
        }
    }

    private Server(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
        this.okHttpClient = new OkHttpClient();
    }

    public static Server getInstance(OnProgressListener onProgressListener) {
        return new Server(onProgressListener);
    }

    public void request(String url) {
        if (this.onProgressListener != null) {
            this.onProgressListener.onStart();
        }
        this.request = new Request.Builder().url(url).build();
        this.okHttpClient.newCall(this.request).enqueue(this.callback);
    }

}
