package com.paskie.callrecorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.paskie.callrecorder.database.DatabaseManager;
import com.paskie.callrecorder.models.Call;
import com.paskie.callrecorder.utils.Data;
import com.paskie.callrecorder.utils.FileUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Created by user on 4/24/2017.
 */

public class CallRecorderService extends Service {

    public static final String TAG = CallRecorderService.class.getSimpleName();
    private MediaRecorder mMediaRecorder;
    private volatile boolean recordStarted;
    private String phoneNumber = "";
    private Call currentCall = new Call();
    private Context mContext;
    private long startTime;
    private long stopTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int type = -1;
        currentCall.time_ = new Date().toString();
        mContext = getApplicationContext();
        if(intent != null) {
            phoneNumber = intent.getStringExtra("phone_number");
            type = intent.getIntExtra("state", -1);
        }
        switch (type) {
            case Data.INCOMING_CALL:
                startRecording();
                break;
            case Data.CALL_END:
                stopRecording();
                break;
            case Data.OUTGOING_CALL:
                startRecording();
                break;
            default:break;
        }
        return super.onStartCommand(intent, flags, startId);
    }
    void stopRecording() {
        if(mMediaRecorder == null)
            return;

        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            recordStarted = false;
            mMediaRecorder = null;

            stopTime = System.currentTimeMillis();
            currentCall.duration = String.valueOf(stopTime - startTime);
            DatabaseManager.getInstance(this).save(currentCall);
        }catch (Exception io) {
            Log.d(TAG, "ERROR", io);
        }
    }
    void startRecording() {
       if (recordStarted)
           return;

        recordStarted = true;
        try {
            startTime = System.currentTimeMillis();
            mMediaRecorder = new MediaRecorder();
            String path = FileUtil.save(phoneNumber, mContext).getAbsolutePath();
            currentCall.mPath = path;
            currentCall.phoneNumber = phoneNumber;
            currentCall.sent = false;
            currentCall.mType = Call.Type.OUTGOING;
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(path);
            mMediaRecorder.prepare();

            mMediaRecorder.start();
        }catch (IOException io) {
            Log.d(TAG, "ERROR", io);
            recordStarted = false;
        }
    }
}
