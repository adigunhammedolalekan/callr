package com.paskie.callrecorder.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 4/24/2017.
 */

public class FileUtil {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_mm_dd-hh:mm:ss", Locale.US);
    public static File save(String name, Context context) throws IOException{
        if(context == null)
            return null;
        String parentDir = context.getFilesDir().getAbsolutePath() + File.separator + "CR";
        File parent = new File(parentDir);
        if(!parent.exists())
            parent.mkdirs();

        File mainFile = new File(parentDir, generate(name));
        mainFile.createNewFile();

        return mainFile;
    }

    static String generate(String name) {
        if(name == null)
            return "Call_"+simpleDateFormat.format(new Date()) + ".3gp";
        return name.hashCode() + simpleDateFormat.format(new Date()) + ".3gp";
    }
    public void cleanUp(Context context) {

        if(context == null)
            return;

        String parent = context.getFilesDir().getAbsolutePath() + File.separator + "CR";
        File parentFile = new File(parent);
        if(parentFile.exists() && parentFile.isDirectory()) {
            File[] files = parentFile.listFiles();
            if(files == null || files.length < 10)
                return;

            for (File next : files) {
                if(next.exists())
                    next.delete();
            }
        }
    }
}
