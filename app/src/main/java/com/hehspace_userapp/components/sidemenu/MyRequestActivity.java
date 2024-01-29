package com.hehspace_userapp.components.sidemenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.notification.NotificationActivity;
import com.hehspace_userapp.components.property.PropertDetailsActivity;
import com.hehspace_userapp.databinding.ActivityMyRequestBinding;
import com.hehspace_userapp.databinding.ItemBookingBinding;
import com.hehspace_userapp.databinding.LayoutNotificationBinding;
import com.hehspace_userapp.model.NotificationModel;
import com.hehspace_userapp.model.RequestListModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.List;

public class MyRequestActivity extends BaseBindingActivity {

    ActivityMyRequestBinding activityMyRequestBinding;
    MyRequestView_Model view_model;
    @Override
    protected void setBinding() {
        activityMyRequestBinding = DataBindingUtil.setContentView(this,R.layout.activity_my_request);
        view_model = new MyRequestView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.getRequesList();
        } else {
            Uitility.nointernetDialog(this);
        }
        activityMyRequestBinding.rvRequest.setHasFixedSize(true);
        activityMyRequestBinding.rvRequest.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        activityMyRequestBinding.srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Uitility.isOnline(mActivity)) {
                    view_model.getRequesList();
                } else {
                    Uitility.nointernetDialog(mActivity);
                }
            }
        });

    }

    @Override
    protected void setListeners() {

        activityMyRequestBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void handleResult(ApiResponse<RequestListModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                activityMyRequestBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        activityMyRequestBinding.rvRequest.setVisibility(View.VISIBLE);
                        activityMyRequestBinding.rlNoFound.setVisibility(View.GONE);
                        RequestAdapter requestAdapter = new RequestAdapter(this,result.getData().data);
                        activityMyRequestBinding.rvRequest.setAdapter(requestAdapter);
                    }
                    else{
                        activityMyRequestBinding.rvRequest.setVisibility(View.GONE);
                        activityMyRequestBinding.rlNoFound.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    public static class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

        Context context;
        List<RequestListModel.DataEntity> list;
        public RequestAdapter(Context context, List<RequestListModel.DataEntity> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemBookingBinding rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_booking, parent,
                    false);
            return new RequestAdapter.ViewHolder(rowDrawerBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context)
                    .load((list.get(position).propertyImageUrl))
                    .error(R.drawable.logo)
                    .into(holder.itemRowBinding.ivImage);

            holder.itemRowBinding.tvTitle.setText(list.get(position).propertyTitle);
            holder.itemRowBinding.tvRequestNo.setText("Request No: "+list.get(position).requestNumber);
            holder.itemRowBinding.tvStatus.setText(list.get(position).requestStatus);
            holder.itemRowBinding.tvName.setText(list.get(position).fullName);
            holder.itemRowBinding.tvDate.setText(list.get(position).requestedDate);
            holder.itemRowBinding.tvTime.setText(list.get(position).requestedTime);

            if(list.get(position).requestStatus.equals("ACCEPTED")){
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
            }
            else if(list.get(position).requestStatus.equals("REJECTED")){
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
            }
            else  holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.yellow_bg));

            holder.itemRowBinding.llMain.setOnClickListener(v -> {
                context.startActivity(new Intent(context, RequestDetailsActivity.class)
                        .putExtra("reqid",list.get(position).requestId)
                        .putExtra("reqstatus",list.get(position).requestStatus)
                );
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemBookingBinding itemRowBinding;
            public ViewHolder(ItemBookingBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


}