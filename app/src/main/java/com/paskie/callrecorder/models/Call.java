package com.paskie.callrecorder.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paskie.callrecorder.database.DatabaseManager;

/**
 * Created by Adigun on 4/24/2017.
 */

public class Call {

    public int callID = 0;
    public String phoneNumber = "";
    public String time_ = "";
    public Type mType;
    public String duration = "";
    public boolean sent = false;
    public int mSize = 0;
    public String mPath = "";


    //database
    public static final String CALL_RECORDER_TABLE = "call_s";
    public static final String CALL_ID = "call_id";
    public static final String PHONE_NUMBER = "phone_num";
    public static final String CALL_TYPE = "call_type";
    public static final String CALL_DURATION = "call_duration";
    public static final String CALL_SENT = "call_sent";
    public static final String CALL_SIZE = "call_size";
    public static final String PATH = "path_";
    public static final String TIME = "_time_";

    public static final String CREATE_CALL_TABLE = "CREATE TABLE "+CALL_RECORDER_TABLE+"("+CALL_ID +
            " integer primary key autoincrement, " +
            PHONE_NUMBER +" text, " +
            CALL_TYPE + " integer, " +
            CALL_DURATION +" text, " +
            CALL_SENT +" integer, " +
            TIME +" text, " +
            CALL_SIZE +" call_size, " +
            PATH +");";

    public enum Type {

        OUTGOING(0), INCOMING(1);
        int mType;
        Type(int type) {
            mType = type;
        }
    }

    public static Call from(Cursor cursor) {
        if(cursor == null)
            return new Call();

        Call call = new Call();
        call.callID = cursor.getInt(cursor.getColumnIndexOrThrow(CALL_ID));
        call.phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(PHONE_NUMBER));
        call.duration = cursor.getString(cursor.getColumnIndexOrThrow(CALL_DURATION));
        call.mType =
                cursor.getInt(cursor.getColumnIndexOrThrow(CALL_TYPE)) == 0 ? Type.OUTGOING : Type.INCOMING;
        call.sent = cursor.getInt(cursor.getColumnIndexOrThrow(CALL_SENT)) == 1;
        call.mPath = cursor.getString(cursor.getColumnIndexOrThrow(PATH));
        call.time_ = cursor.getString(cursor.getColumnIndexOrThrow(TIME));
        call.mSize = cursor.getInt(cursor.getColumnIndexOrThrow(CALL_SIZE));

        return call;
    }
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PHONE_NUMBER, phoneNumber);
        contentValues.put(CALL_DURATION, duration);
        contentValues.put(CALL_TYPE, mType == Type.OUTGOING ? 0 : 1);
        contentValues.put(CALL_SENT, !sent ? 0 : 1);
        contentValues.put(PATH, mPath);
        contentValues.put(TIME, time_);
        contentValues.put(CALL_SIZE, mSize);

        return contentValues;
    }
    public void setPath(String path) {
        this.mPath = path;
    }
    public void save(Context context) {
        if(context == null)
            return;

        SQLiteDatabase database = DatabaseManager.getInstance(context).getDB();
        database.insert(CREATE_CALL_TABLE, null, getContentValues());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Phone Number ").append(" = ").append(phoneNumber).append("; Path = ").append(mPath);
        return stringBuilder.toString();
    }
}
