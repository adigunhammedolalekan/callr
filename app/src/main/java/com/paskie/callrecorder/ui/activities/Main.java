package com.paskie.callrecorder.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.paskie.callrecorder.R;
import com.paskie.callrecorder.database.DatabaseManager;
import com.paskie.callrecorder.listeners.IDialogOperationListener;
import com.paskie.callrecorder.models.Call;
import com.paskie.callrecorder.ui.adapters.CallListAdapter;
import com.paskie.callrecorder.ui.fragments.CallOperationOptionDialog;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * Created by user on 4/24/2017.
 */

public class Main extends BaseActivity implements CallListAdapter.IMoreClickListener, IDialogOperationListener{

    @BindView(R.id.rv_call_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_call)
    TextView noCallTv;

    private CallListAdapter callListAdapter;

    private Call mSelectedCall;
    private List<Call> callList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        if(checkPermission()) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            callList = DatabaseManager.getInstance(this).all();
            if(callList.isEmpty()) {
                noCallTv.setVisibility(View.VISIBLE);
            }else {
                noCallTv.setVisibility(View.GONE);
            }
            callListAdapter = new CallListAdapter(this, callList, this);
            mRecyclerView.setAdapter(callListAdapter);
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Callr");
        }
    }
    boolean checkPermission() {
        //int granted =
        return true;
    }

    @Override
    public void onAction(int action) {
        switch (action) {
            case ACTION_DELETE_CALL:
                new AlertDialog.Builder(this)
                        .setTitle("Delete Record").setMessage("Delete this call record. Can not be undone")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(mSelectedCall.mPath);
                                if(file.exists())
                                    file.delete();

                                DatabaseManager.getInstance(Main.this).delete(mSelectedCall);
                                callList.remove(mSelectedCall);
                                callListAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("CANCEL", null).create().show();
                break;
            case ACTION_LISTEN:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(mSelectedCall.mPath)), "audio/*");
                //if(getPackageManager().resolveActivity(intent, PackageManager.GET_META_DATA))
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
        }
    }

    @Override
    public void onMoreClick(int pos) {
        mSelectedCall = callList.get(pos);

        CallOperationOptionDialog callOperationOptionDialog = new CallOperationOptionDialog();
        callOperationOptionDialog.show(getSupportFragmentManager(), "CRP");
    }
}
