package com.paskie.callrecorder;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created By Adigun Hammed Olalekan
 * 7/12/2017.
 * Beem24, Inc
 */

public class Callr extends Application {


    private static Callr app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());

    }
    public static Callr get() {
        return app;
    }
}
