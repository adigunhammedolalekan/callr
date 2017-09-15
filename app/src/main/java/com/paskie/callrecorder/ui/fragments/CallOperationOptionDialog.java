package com.paskie.callrecorder.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.paskie.callrecorder.R;
import com.paskie.callrecorder.listeners.IDialogOperationListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By Adigun Hammed Olalekan
 * 7/12/2017.
 * Beem24, Inc
 */

public class CallOperationOptionDialog extends DialogFragment {

    @BindView(R.id.layout_delete_call)
    LinearLayout delete;
    @BindView(R.id.layout_play_call)
    LinearLayout play;

    private IDialogOperationListener iDialogOperationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.call_operation_option, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            iDialogOperationListener = (IDialogOperationListener) context;
        }catch (ClassCastException ex) {}
    }
    @OnClick(R.id.layout_delete_call) public void onDeleteCall() {
        if(iDialogOperationListener != null)
            iDialogOperationListener.onAction(IDialogOperationListener.ACTION_DELETE_CALL);
    }
    @OnClick(R.id.layout_play_call) public void onPlayClick() {
        if(iDialogOperationListener != null)
            iDialogOperationListener.onAction(IDialogOperationListener.ACTION_LISTEN);
    }
}
