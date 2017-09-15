package com.paskie.callrecorder.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.paskie.callrecorder.database.DatabaseManager;
import com.paskie.callrecorder.listeners.CustomResponseHandler;
import com.paskie.callrecorder.models.Call;
import com.paskie.callrecorder.utils.Pref;
import com.paskie.callrecorder.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 4/24/2017.
 */

public class CallUploadService extends IntentService {


    public CallUploadService() {
        super("UploadService");
    }

    private AsyncHttpClient asyncHttpClient = new SyncHttpClient();
    public static final String TAG = CallUploadService.class.getSimpleName();
    List<Call> sent = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(!Util.online(this)) {
            stopSelf();
            return;
        }

        List<Call> unSent = DatabaseManager.getInstance(this).getUnsent();
        if(unSent == null)
            return;

        mContext = this;
        int count = 0;
        asyncHttpClient.setTimeout(60000 * 3);
        RequestParams requestParams = new RequestParams();

        for (int i = 0; i < unSent.size(); i++) {
            count += 1;
            if(count == 5)
                break;
            Call next = unSent.get(i);
            requestParams.put("user_id", Pref.getInstance().getUser().ID);
            requestParams.put("phone_number", next.phoneNumber + " ");
            requestParams.put("contact_name", next.phoneNumber + " ");
            try{
                String pName = "call_"+i;
                requestParams.put(pName, new File(next.mPath));
            }catch (FileNotFoundException ex) {
                Log.d(TAG, "ERROR", ex);
            }
            sent.add(next);
        }
        if(sent.size() > 0) {
            requestParams.put("count_", sent.size());
            String URL = Util.URL + "/call";
            asyncHttpClient.post(URL, requestParams, customResponseHandler);
        }
    }
    private CustomResponseHandler customResponseHandler = new CustomResponseHandler(sent) {

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {

            List<Call> sent = get();
            for (Call call : sent) {
                DatabaseManager.getInstance(mContext).update(call);
            }
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            super.onFailure(statusCode, headers, responseString, throwable);
        }

    };
}
