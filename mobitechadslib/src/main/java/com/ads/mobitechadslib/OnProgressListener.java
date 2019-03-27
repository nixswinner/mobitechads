package com.ads.mobitechadslib;

public interface OnProgressListener {

    void onFailure();

    void onResponse(String response, boolean success);

    void onStart();


}
