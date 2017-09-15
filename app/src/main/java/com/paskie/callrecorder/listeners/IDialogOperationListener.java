package com.paskie.callrecorder.listeners;

/**
 * Created By Adigun Hammed Olalekan
 * 7/12/2017.
 * Beem24, Inc
 */

public interface IDialogOperationListener {

    public static final int ACTION_DELETE_CALL = 1;
    public static final int ACTION_LISTEN = 2;

    void onAction(int action);


}
