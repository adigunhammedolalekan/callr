package com.paskie.callrecorder.listeners;

import android.os.Bundle;

import com.loopj.android.http.TextHttpResponseHandler;
import com.paskie.callrecorder.models.Call;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 4/26/2017.
 */

public class CustomResponseHandler extends TextHttpResponseHandler {

    public List<Call> data;
    public CustomResponseHandler(List<Call> call) {
        data = call;
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {

    }
    public List<Call> get() {
        return data;
    }
}
