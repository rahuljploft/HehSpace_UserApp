package com.hehspace_userapp.components.sidemenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.chat.ChatActivity;
import com.hehspace_userapp.databinding.ActivityBookingDetailsBinding;
import com.hehspace_userapp.databinding.ItemViewAddonsRequestBinding;
import com.hehspace_userapp.model.BookingDetailsModel;
import com.hehspace_userapp.model.BookingListModel;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingDetailsActivity extends BaseBindingActivity {

    ActivityBookingDetailsBinding activityBookingBinding;
    BookingDetailView_Model view_model;
    public static List<BookingDetailsModel.AddonServicesEntity> list = new ArrayList<>();
    AddonAdapters addonAdapters;
    String property_id = "";
    String userid = "";
    String bookingid = "";
    String requestid = "";
    String name = "";
    String property = "";
    String image = "";
    String plat = "";
    String plong = "";

    @Override
    protected void setBinding() {
        activityBookingBinding = DataBindingUtil.setContentView(this,R.layout.activity_booking_details);
        view_model = new BookingDetailView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
        view_model.livedata1.observe(this, modelApiResponse -> handleResultCacle(modelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.getBookingDetails(Integer.parseInt(getIntent().getStringExtra("bookingid")));
        } else {
            Uitility.nointernetDialog(this);
        }
        activityBookingBinding.rvAddon.setHasFixedSize(true);
        activityBookingBinding.rvAddon.setLayoutManager(new GridLayoutManager(this, 2));
        addonAdapters = new AddonAdapters(this, (type, pos) -> {

        });
        activityBookingBinding.rvAddon.setAdapter(addonAdapters);
    }

    @Override
    protected void setListeners() {
        activityBookingBinding.ivBack.setOnClickListener(this);
        activityBookingBinding.layoutChat.setOnClickListener(this);
        activityBookingBinding.btnAddReview.setOnClickListener(this);
        activityBookingBinding.btnNext.setOnClickListener(this);
        activityBookingBinding.layoutGoToMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }

        if(view.getId() == R.id.layoutChat){
            startActivity(new Intent(this,ChatActivity.class)
                    .putExtra("userid",userid)
                    .putExtra("bookingid",bookingid)
                    .putExtra("requestid",requestid)
                    .putExtra("propertyid",property_id)
                    .putExtra("name",name)
                    .putExtra("property",property)
                    .putExtra("image",image)
            );
        }

        if(view.getId() == R.id.btnAddReview){
                startActivity(new Intent(this, AddReviewActivity.class)
                        .putExtra("booking_id",getIntent().getStringExtra("bookingid"))
                        .putExtra("property_id",property_id)
                );
        }

       if(view.getId() == R.id.layoutGoToMap){
           Log.e("current",sessionManager.getLATITUDE()+","+sessionManager.getLONGTITUDE());
           Log.e("currentprop",plat+","+plong);
           Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                   Uri.parse("http://maps.google.com/maps?saddr="+sessionManager.getLATITUDE()+","+sessionManager.getLONGTITUDE()
                           +"&daddr="+plat+","+plong));
           startActivity(intent);
        }

        if(view.getId() == R.id.btnNext){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Cancel")
                    .setMessage("Do you want to cancel your booking?")
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            dialoginterface.cancel();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            if (Uitility.isOnline(BookingDetailsActivity.this)) {
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("_method","PUT");
                                view_model.cancelBooking(Integer.parseInt(getIntent().getStringExtra("bookingid")),hashMap);
                            } else {
                                Uitility.nointernetDialog(BookingDetailsActivity.this);
                            }
                        }
                    }).show();
        }
    }

    private void handleResult(ApiResponse<BookingDetailsModel> result) {
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
                        Glide.with(this)
                                .load((result.getData().data.propertyImageUrl))
                                .error(R.drawable.logo)
                                .into(activityBookingBinding.ivImage);

                        activityBookingBinding.tvStatus.setText(result.getData().data.bookingStatus);
                        switch (result.getData().data.bookingStatus) {
                            case "COMPLETED":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.green_bg));
                                break;
                            case "UPCOMING":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.upcoming_bg));
                                break;
                            case "CONFIRMED":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.upcoming_bg));
                                break;
                            case "CANCELLED":
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.red_bg));
                                break;
                            default:
                                activityBookingBinding.tvStatus.setBackground(this.getResources().getDrawable(R.drawable.yellow_bg));
                                break;
                        }

                        activityBookingBinding.tvTitle.setText(result.getData().data.propertyTitle);
                        activityBookingBinding.tvLocation.setText(result.getData().data.propertyLocation);
                        activityBookingBinding.tvReview.setText(result.getData().data.propertyRatting+"("+result.getData().data.propertyReview+")");

                        property_id = result.getData().data.propertyId;
                        userid = result.getData().data.hostId;
                        bookingid = result.getData().data.bookingId;
                        requestid = result.getData().data.requestId;
                        name = result.getData().data.hostname;
                        property = result.getData().data.propertyTitle;
                        image = result.getData().data.propertyImageUrl;
                        plat = result.getData().data.propertyLatitude;
                        plong = result.getData().data.propertyLongitude;


                        if(list.size()>0){
                            list = result.getData().data.addonServices;
                            addonAdapters.notifyDataSetChanged();
                            activityBookingBinding.rvAddon.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvadoontitle.setVisibility(View.VISIBLE);
                        }
                        else {
                            activityBookingBinding.rvAddon.setVisibility(View.GONE);
                            activityBookingBinding.tvadoontitle.setVisibility(View.GONE);
                        }

                        activityBookingBinding.tvcheckindatetime.setText(result.getData().data.checkinDate);
                        activityBookingBinding.tvcheckoutdatetime.setText(result.getData().data.checkoutDate);
                        activityBookingBinding.tvType.setText(result.getData().data.propertyCategory);
                        activityBookingBinding.tvGuestCount.setText(result.getData().data.numberOfGuest);
                        activityBookingBinding.tvTotalAmount.setText(result.getData().data.priceCalculation.totalAmount);
                        activityBookingBinding.tvGrandTotal.setText(result.getData().data.priceCalculation.totalAmount);

                        if(!result.getData().data.priceCalculation.weeklyAmount.equals("")){
                            activityBookingBinding.layoutWeekly.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvWeeklyPrice.setText(result.getData().data.priceCalculation.weeklyAmount);
                        }
                        else activityBookingBinding.layoutWeekly.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.dailyAmount.equals("")){
                            activityBookingBinding.layoutDaily.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvDailyPrice.setText(result.getData().data.priceCalculation.dailyAmount);
                        }
                        else activityBookingBinding.layoutDaily.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.hourlyAmount.equals("")){
                            activityBookingBinding.layoutHourly.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvHourlyPrice.setText(result.getData().data.priceCalculation.hourlyAmount);
                        }
                        else activityBookingBinding.layoutHourly.setVisibility(View.GONE);
                        if(!result.getData().data.priceCalculation.servicesAmount.equals("")){
                            activityBookingBinding.layoutExtra.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvextraService.setText(result.getData().data.priceCalculation.servicesAmount);
                        }
                        else activityBookingBinding.layoutExtra.setVisibility(View.GONE);

                        activityBookingBinding.tvCleaningFee.setText(result.getData().data.priceCalculation.cleanerFee);
                        activityBookingBinding.tvServiceTax.setText(result.getData().data.priceCalculation.taxAmount);

                        if(result.getData().data.bookingStatus.equals("CONFIRMED")){
                            activityBookingBinding.btnNext.setVisibility(View.VISIBLE);
                            activityBookingBinding.tvCancel.setVisibility(View.VISIBLE);
                            activityBookingBinding.layoutChat.setVisibility(View.VISIBLE);
                            activityBookingBinding.layoutGoToMap.setVisibility(View.VISIBLE);
                            activityBookingBinding.btnAddReview.setVisibility(View.GONE);
                        }
                        else if(result.getData().data.bookingStatus.equals("CANCELLED")){
                            activityBookingBinding.btnNext.setVisibility(View.GONE);
                            activityBookingBinding.tvCancel.setVisibility(View.GONE);
                            activityBookingBinding.layoutChat.setVisibility(View.GONE);
                            activityBookingBinding.layoutGoToMap.setVisibility(View.GONE);
                            activityBookingBinding.btnAddReview.setVisibility(View.GONE);
                        }
                        else {
                            activityBookingBinding.btnNext.setVisibility(View.GONE);
                            activityBookingBinding.tvCancel.setVisibility(View.GONE);
                            activityBookingBinding.layoutChat.setVisibility(View.GONE);
                            activityBookingBinding.layoutGoToMap.setVisibility(View.GONE);
                            if(result.getData().data.feedbackStatus.equals("true")){
                                activityBookingBinding.btnAddReview.setVisibility(View.GONE);
                            }
                            else  activityBookingBinding.btnAddReview.setVisibility(View.VISIBLE);
                        }
                    }
                    else{

                    }
                }
                break;
        }
    }
    private void handleResultCacle(ApiResponse<CommonModel> result) {
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
                    startActivity(new Intent(this,MyBookingActivity.class));
                    }
                    else{

                    }
                }
                break;
        }
    }
    private void handleResultCancel(ApiResponse<CommonModel> result) {
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
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public static class AddonAdapters extends RecyclerView.Adapter<AddonAdapters.ViewHolder> {
        Context context;
        ItemClickListner itemClickListner;
        public AddonAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }
        @Override
        public AddonAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewAddonsRequestBinding itemViewAddonsRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_addons_request, parent,
                    false);
            return new AddonAdapters.ViewHolder(itemViewAddonsRequestBinding);
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AddonAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.cbName.setText(list.get(position).servicesTitle);
            holder.itemRowBinding.cbName.setChecked(true);
            holder.itemRowBinding.cbName.setEnabled(false);
        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewAddonsRequestBinding itemRowBinding;

            public ViewHolder(ItemViewAddonsRequestBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


}