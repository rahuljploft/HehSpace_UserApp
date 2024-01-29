package com.hehspace_userapp.components.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.databinding.ActivityNotificationBinding;
import com.hehspace_userapp.databinding.LayoutNotificationBinding;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.NotificationModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.List;

public class NotificationActivity extends BaseBindingActivity {

    ActivityNotificationBinding activityNotificationBinding;
    NotificationView_Model view_model;
    @Override
    protected void setBinding() {
        activityNotificationBinding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        view_model = new NotificationView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        view_model.Commonlivedata.observe(this, modelApiResponse -> handleResultDeleteNotifcation(modelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.notificationList();
        } else {
            Uitility.nointernetDialog(this);
        }
        activityNotificationBinding.rvNotification.setHasFixedSize(true);
        activityNotificationBinding.rvNotification.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        if (Uitility.isOnline(NotificationActivity.this)) {
            view_model.seenNotification();
        } else {
            Uitility.nointernetDialog(NotificationActivity.this);
        }
    }

    @Override
    protected void setListeners() {
        activityNotificationBinding.ivBack.setOnClickListener(this);
        activityNotificationBinding.layoutClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
        if(view.getId() == R.id.layoutClear){
            if(activityNotificationBinding.rlNoFound.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "You have no notification.", Toast.LENGTH_SHORT).show();
                return;
            }
            alertView();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void alertView( ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle( "Clear Notification" )
                .setMessage("Are you sure, you want to clear all?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                        if (Uitility.isOnline(NotificationActivity.this)) {
                            view_model.deleteNotification();
                        } else {
                            Uitility.nointernetDialog(NotificationActivity.this);
                        }
                    }
                }).show();
    }

    private void handleResultDeleteNotifcation(ApiResponse<CommonModel> result) {
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
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        if (Uitility.isOnline(this)) {
                            view_model.notificationList();
                        } else {
                            Uitility.nointernetDialog(this);
                        }
                    }
                }
                break;
        }
    }



    private void handleResult(ApiResponse<NotificationModel> result) {
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
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        activityNotificationBinding.rvNotification.setVisibility(View.VISIBLE);
                        activityNotificationBinding.rlNoFound.setVisibility(View.GONE);
                        NotificationAdapter notificationAdapter = new NotificationAdapter(this,result.getData().data);
                        activityNotificationBinding.rvNotification.setAdapter(notificationAdapter);
                    }
                    else{
                        activityNotificationBinding.rvNotification.setVisibility(View.GONE);
                        activityNotificationBinding.rlNoFound.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    public static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        Context context;
        List<NotificationModel.DataEntity> list;
        public NotificationAdapter(Context context, List<NotificationModel.DataEntity> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutNotificationBinding rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_notification, parent,
                    false);
            return new NotificationAdapter.ViewHolder(rowDrawerBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvTitle.setText(list.get(position).notificationTitle);
            holder.itemRowBinding.tvdetails.setText(list.get(position).notificationDetails);
            holder.itemRowBinding.tvDate.setText(list.get(position).createdAt);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutNotificationBinding itemRowBinding;
            public ViewHolder(LayoutNotificationBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }
}