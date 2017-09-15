package com.paskie.callrecorder.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by user on 4/24/2017.
 */

public class Util {

    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static final String URL = "http://192.168.43.97/callr/api";
    public static boolean online(Context context) {
        if(context == null)
            return false;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
    public static boolean empty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }
    public static String text(EditText editText) {
        return editText.getText().toString().trim();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
