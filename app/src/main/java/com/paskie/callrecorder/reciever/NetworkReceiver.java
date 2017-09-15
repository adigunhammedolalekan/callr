package com.paskie.callrecorder.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.paskie.callrecorder.services.CallUploadService;
import com.paskie.callrecorder.utils.Util;

/**
 * Created by user on 4/26/2017.
 */

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean online = Util.online(context);
            if(online) {
                Intent i = new Intent(context, CallUploadService.class);
                context.startService(i);
            }
        }
    }
}
