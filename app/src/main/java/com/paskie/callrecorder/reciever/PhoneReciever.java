package com.paskie.callrecorder.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.paskie.callrecorder.services.CallRecorderService;
import com.paskie.callrecorder.utils.Data;

/**
 * Created by user on 4/24/2017.
 */

public class PhoneReciever extends BroadcastReceiver {


    public static final String NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
    public String mPhoneNumber = "";
    public static final String TAG = PhoneReciever.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {


        String state = "";
        int callState = -1;
        if(intent.getAction().equals(NEW_OUTGOING_CALL)) {
            mPhoneNumber = intent.getExtras().getString(EXTRA_PHONE_NUMBER);
            state = NEW_OUTGOING_CALL;
        }else {
            state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            mPhoneNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        }
        if(state == null)
            return;

        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            callState = Data.CALL_END;
        }else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            callState = Data.INCOMING_CALL;
        }
        handle(callState, context);
    }
    private void handle(int state, Context context) {
        Intent intent = new Intent(context, CallRecorderService.class);
        intent.putExtra("phone_number", mPhoneNumber);
        intent.putExtra("state", state);
        context.startService(intent);
    }

}
