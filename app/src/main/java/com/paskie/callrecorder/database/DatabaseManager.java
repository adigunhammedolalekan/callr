package com.paskie.callrecorder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paskie.callrecorder.models.Call;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 4/24/2017.
 */

public class DatabaseManager {

    public static DatabaseManager instance;
    private DatabaseHelper databaseHelper;

    public static synchronized DatabaseManager getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }
    private DatabaseManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
    public SQLiteDatabase getDB() {
        return databaseHelper.getWritableDatabase();
    }
    public List<Call> getUnsent() {
        String whereClause = Call.CALL_SENT + "=?";
        String[] whereArgs = {String.valueOf(0)};
        Cursor cursor = getDB().query(Call.CALL_RECORDER_TABLE, null, whereClause, whereArgs, null, null, null);
        if(cursor == null)
            return Collections.emptyList();

        List<Call> callList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Call call = Call.from(cursor);
            callList.add(call);
        }
        cursor.close();
        return callList;
    }
    public List<Call> all() {

        Cursor cursor = getDB().rawQuery("SELECT * FROM "+Call.CALL_RECORDER_TABLE, null);

        if(cursor == null)
            return Collections.emptyList();

        List<Call> callList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Call call = Call.from(cursor);
            callList.add(call);
        }
        cursor.close();
        return callList;
    }
    public void save(Call call) {
        if(call == null)
            return;

        ContentValues contentValues = call.getContentValues();
        SQLiteDatabase sqLiteDatabase = getDB();
        sqLiteDatabase.insert(Call.CALL_RECORDER_TABLE, null, contentValues);
    }
    public synchronized void update(Call call) {

        String where = Call.CALL_ID + "=?";
        String args[] = {String.valueOf(call.callID)};
        int updated  = getDB().update(Call.CALL_RECORDER_TABLE, call.getContentValues(), where, args);
        if(updated != -1) {
            File file = new File(call.mPath);
            if(file.exists())
                file.delete();
        }
    }
    public synchronized void delete(Call call) {
        getDB().delete(Call.CALL_RECORDER_TABLE, Call.CALL_ID + "=?", new String[]{String.valueOf(call.callID)});
    }
}
