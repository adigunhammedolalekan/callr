package com.paskie.callrecorder.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.paskie.callrecorder.Callr;
import com.paskie.callrecorder.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created By Adigun Hammed Olalekan
 * 7/12/2017.
 * Beem24, Inc
 */

public class Pref {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private static Pref instance;
    private Pref() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Callr.get().getApplicationContext());
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized Pref getInstance() {
        if(instance == null) instance = new Pref(); return instance;
    }
    public void save(JSONObject j) {
        mEditor.putString("_user_", j.toString()).apply();
        setHasSession();
    }
    public User getUser() {
        try {
            return User.parse(new JSONObject(mSharedPreferences.getString("_user_", "{}")));
        }catch (JSONException je) {}
        return new User();
    }
    public void setHasSession() {
        mEditor.putBoolean("_session_", true).apply();
    }
    public void destroy() {
        mEditor.clear().apply();
    }
    public boolean hasSession() {
        return mSharedPreferences.getBoolean("_session_", false);
    }
}
