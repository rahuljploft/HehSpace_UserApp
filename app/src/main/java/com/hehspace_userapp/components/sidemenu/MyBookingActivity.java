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
import com.hehspace_userapp.databinding.ActivityMyBookingBinding;
import com.hehspace_userapp.databinding.ItemBookingBinding;
import com.hehspace_userapp.model.BookingListModel;
import com.hehspace_userapp.model.RequestListModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.ArrayList;
import java.util.List;

public class MyBookingActivity extends BaseBindingActivity {

    ActivityMyBookingBinding activityMyBookingBinding;
    BookingAdapter bookingAdapter;
    MyBookingView_Model view_model;
    public static List<BookingListModel.DataEntity> oldlist = new ArrayList<>();
    public static List<BookingListModel.DataEntity> finallist = new ArrayList<>();

    @Override
    protected void setBinding() {
        activityMyBookingBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_booking);
        view_model = new MyBookingView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.getBookingList();
        } else {
            Uitility.nointernetDialog(this);
        }

        activityMyBookingBinding.srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Uitility.isOnline(mActivity)) {
                    view_model.getBookingList();
                } else {
                    Uitility.nointernetDialog(mActivity);
                }

            }
        });

        activityMyBookingBinding.rvBooking.setHasFixedSize(true);
        activityMyBookingBinding.rvBooking.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

        bookingAdapter = new BookingAdapter(mActivity, (type, pos) -> {
            if (type.equals("view")) {
                startActivity(new Intent(mActivity, BookingDetailsActivity.class).putExtra("bookingid", finallist.get(pos).bookingId));
            }
        });
        activityMyBookingBinding.rvBooking.setAdapter(bookingAdapter);

        activityMyBookingBinding.layoutAll.setOnClickListener(v -> {
            finallist.clear();
            activityMyBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.app_btn));
            activityMyBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.white));
            activityMyBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.black));
            finallist.addAll(oldlist);
            if (finallist.size() > 0) {
                activityMyBookingBinding.cardNoBooking.setVisibility(View.GONE);
                activityMyBookingBinding.rvBooking.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            } else {
                activityMyBookingBinding.rvBooking.setVisibility(View.GONE);
                activityMyBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });

        activityMyBookingBinding.layoutUpcoming.setOnClickListener(v -> {
            finallist.clear();
            activityMyBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.app_btn));
            activityMyBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.white));
            activityMyBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.black));
            for (int i = 0; i < oldlist.size(); i++) {
                if (oldlist.get(i).bookingStatus.equals("CONFIRMED")) {
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist", finallist.size() + "");
            if (finallist.size() > 0) {
                activityMyBookingBinding.cardNoBooking.setVisibility(View.GONE);
                activityMyBookingBinding.rvBooking.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            } else {
                activityMyBookingBinding.rvBooking.setVisibility(View.GONE);
                activityMyBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
        activityMyBookingBinding.layoutCompleted.setOnClickListener(v -> {
            finallist.clear();
            activityMyBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.app_btn));
            activityMyBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.white));
            activityMyBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.black));
            for (int i = 0; i < oldlist.size(); i++) {
                if (oldlist.get(i).bookingStatus.equals("COMPLETED")) {
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist", oldlist.size() + "");
            if (finallist.size() > 0) {
                activityMyBookingBinding.cardNoBooking.setVisibility(View.GONE);
                activityMyBookingBinding.rvBooking.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            } else {
                activityMyBookingBinding.rvBooking.setVisibility(View.GONE);
                activityMyBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
        activityMyBookingBinding.layoutCancle.setOnClickListener(v -> {
            finallist.clear();
            activityMyBookingBinding.layoutAll.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutUpcoming.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutCompleted.setBackground(getResources().getDrawable(R.drawable.grey_bg));
            activityMyBookingBinding.layoutCancle.setBackground(getResources().getDrawable(R.drawable.app_btn));
            activityMyBookingBinding.tvAll.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvUpcoming.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
            activityMyBookingBinding.tvCancle.setTextColor(getResources().getColor(R.color.white));
            for (int i = 0; i < oldlist.size(); i++) {
                if (oldlist.get(i).bookingStatus.equals("CANCELLED")) {
                    finallist.add(oldlist.get(i));
                }
            }
            Log.e("finallist", finallist.size() + "");
            if (finallist.size() > 0) {
                activityMyBookingBinding.cardNoBooking.setVisibility(View.GONE);
                activityMyBookingBinding.rvBooking.setVisibility(View.VISIBLE);
                bookingAdapter.notifyDataSetChanged();
            } else {
                activityMyBookingBinding.rvBooking.setVisibility(View.GONE);
                activityMyBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void setListeners() {
        activityMyBookingBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    public static class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public BookingAdapter(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public BookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemBookingBinding itemBookingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_booking, parent,
                    false);
            return new BookingAdapter.ViewHolder(itemBookingBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvStatus.setVisibility(View.VISIBLE);
            holder.itemRowBinding.tvStatus.setText(finallist.get(position).bookingStatus);
            if (finallist.get(position).bookingStatus.equals("COMPLETED")) {
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
            } else if (finallist.get(position).bookingStatus.equals("UPCOMING")) {
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.upcoming_bg));
            } else if (finallist.get(position).bookingStatus.equals("CONFIRMED")) {
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.upcoming_bg));
            } else if (finallist.get(position).bookingStatus.equals("CANCELLED")) {
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.red_bg));
            } else
                holder.itemRowBinding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.yellow_bg));
            Glide.with(context)
                    .load((finallist.get(position).propertyImageUrl))
                    .error(R.drawable.logo)
                    .into(holder.itemRowBinding.ivImage);
            holder.itemRowBinding.tvRequestNo.setText("Booking No: " + finallist.get(position).bookingNumber);
            holder.itemRowBinding.tvTitle.setText(finallist.get(position).propertyTitle);
            holder.itemRowBinding.tvName.setText(finallist.get(position).fullName);
            holder.itemRowBinding.tvDate.setText(finallist.get(position).checkinDate + " to " + finallist.get(position).checkoutDate);
            holder.itemRowBinding.tvTime.setText(finallist.get(position).checkinTime + " to " + finallist.get(position).checkoutTime);
            holder.itemRowBinding.llMain.setOnClickListener(v -> {
                itemClickListner.onClick("view", position);
            });
        }

        @Override
        public int getItemCount() {
            return finallist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemBookingBinding itemRowBinding;

            public ViewHolder(ItemBookingBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }

    private void handleResult(ApiResponse<BookingListModel> result) {
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
                    activityMyBookingBinding.srlayout.setRefreshing(false);
                    if (result.getData().status.equals("true")) {
                        oldlist.clear();
                        oldlist = result.getData().data;
                        finallist.clear();
                        finallist.addAll(oldlist);
                        if (oldlist.size() > 0) {
                            activityMyBookingBinding.cardNoBooking.setVisibility(View.GONE);
                            activityMyBookingBinding.rvBooking.setVisibility(View.VISIBLE);
                            bookingAdapter.notifyDataSetChanged();
                        } else {
                            activityMyBookingBinding.rvBooking.setVisibility(View.GONE);
                            activityMyBookingBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        }
                    } else {

                    }
                }
                break;
        }
    }


}