package com.paskie.callrecorder.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.paskie.callrecorder.R;
import com.paskie.callrecorder.models.Call;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Adigun Hammed Olalekan
 * 7/12/2017.
 * Beem24, Inc
 */

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallListViewHolder> {

    private List<Call> callList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public interface IMoreClickListener {
        void onMoreClick(int pos);
    }

    private IMoreClickListener iMoreClickListener;

    public CallListAdapter(Context context, List<Call> calls, IMoreClickListener moreClickListener) {
        callList = calls;
        mContext = context;
        if(mContext != null)
            mLayoutInflater = LayoutInflater.from(mContext);

        iMoreClickListener = moreClickListener;
    }
    @Override
    public CallListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        return new CallListViewHolder(mLayoutInflater.inflate(R.layout.call_, parent, false));
    }

    @Override
    public void onBindViewHolder(CallListViewHolder holder, int position) {
        Call call = callList.get(position);
        holder.name.setText(String.valueOf(call.phoneNumber));
        holder.time.setText(String.valueOf(call.time_));
        final int idx = position;
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iMoreClickListener != null)
                    iMoreClickListener.onMoreClick(idx);
            }
        });
    }

    @Override
    public int getItemCount() {
        return callList.size();
    }

    class CallListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_callr_name_call)
        TextView name;
        @BindView(R.id.tv_call_time_call)
        TextView time;
        @BindView(R.id.btn_more_call)
        ImageButton more;


        public CallListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
