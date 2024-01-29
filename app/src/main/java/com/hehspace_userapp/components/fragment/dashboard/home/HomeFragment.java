package com.hehspace_userapp.components.fragment.dashboard.home;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.calender.filterCustomCalender.AirCalendarDatePickerActivity;
import com.hehspace_userapp.components.calender.filterCustomCalender.core.AirCalendarIntent;
import com.hehspace_userapp.components.property.PropertDetailsActivity;
import com.hehspace_userapp.components.search.SearchActivity;
import com.hehspace_userapp.databinding.FragmentHomeBinding;
import com.hehspace_userapp.databinding.ItemCategoryBinding;
import com.hehspace_userapp.databinding.ItemPropertyBinding;
import com.hehspace_userapp.databinding.ItemViewAddonsRequestBinding;
import com.hehspace_userapp.model.PropertyAmenitiesModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertyListModel;
import com.hehspace_userapp.model.PropertySearchModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseFragment;
import com.hehspace_userapp.util.Constant;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;
import com.hehspace_userapp.util.custom_snackbar.CookieBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;


public class HomeFragment extends BaseFragment {

    FragmentHomeBinding fragmentHomeBinding;
    HomeView_Model view_model;
    public static String CategoryId = "0";
    PropertyAdapters propertyAdapters;
    public static List<PropertyListModel.PropertyListEntity> propertyList = new ArrayList();
    public static List<PropertyCategoryModel.DataEntity> filtercatList = new ArrayList<>();
    public static List<PropertyAmenitiesModel.DataEntity> filteramenitiesList = new ArrayList<>();
    public final static int REQUEST_CODE = 1;
    public static TextView tvCheckInDate, tvCheckOutDate;
    TextView tvGuestCount;

    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        fragmentHomeBinding.setLifecycleOwner(this);
        view_model = new HomeView_Model();
        return fragmentHomeBinding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        view_model.propertycategorylivedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        view_model.searchlivedata.observe(this, propertyCategoryModelApiResponse -> handleResultFilter(propertyCategoryModelApiResponse));
        view_model.propertycategorylivedata1.observe(this, propertyCategoryModelApiResponse -> handleResult1(propertyCategoryModelApiResponse));
        view_model.propertylistlivedata.observe(this, propertyCategoryModelApiResponse -> propertyListhandleResult(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(requireContext())) {
            view_model.propertyCategory();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }
        if (Uitility.isOnline(requireContext())) {
            view_model.propertyCategory1();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }
        view_model.amenitieslistlivedata.observe(this, propertyCategoryModelApiResponse -> amenitieshandleResult(propertyCategoryModelApiResponse));

        if (Uitility.isOnline(requireContext())) {
            view_model.getPropertyAmenities();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }

        fragmentHomeBinding.rvCategories.setHasFixedSize(true);
        fragmentHomeBinding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        if (MainActivity.city==null){
            fragmentHomeBinding.tvCurrentLocation.setText(MainActivity.country);
        }else {
            fragmentHomeBinding.tvCurrentLocation.setText(MainActivity.city+", "+MainActivity.country);
        }

        fragmentHomeBinding.rvProperties.setHasFixedSize(true);
        fragmentHomeBinding.rvProperties.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        propertyAdapters = new PropertyAdapters(requireContext(), (type, pos) -> {
            startActivity(new Intent(requireContext(), PropertDetailsActivity.class)
                    .putExtra("property_id", propertyList.get(pos).propertyId)
                    .putExtra("categoryid", CategoryId)

            );
        });
        fragmentHomeBinding.rvProperties.setAdapter(propertyAdapters);

        fragmentHomeBinding.tvLocation.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SearchActivity.class));
        });

        fragmentHomeBinding.rlFilter.setOnClickListener(v -> {
            Constant.CityName = "";
            Constant.CountryName = "";
            Constant.CheckInDate = "";
            Constant.CheckInTime = "";
            Constant.CheckOutDate = "";
            Constant.CheckOutTime = "";
            Constant.CategoryIds.clear();
            Constant.AmenitiesIds.clear();
            Constant.FromRate = "";
            Constant.ToRate = "";
            Constant.Guests = "";
            Constant.RateType = "";
            FilterBottomSheet();
        });
        fragmentHomeBinding.srlayout.setOnRefreshListener(() -> {
            if (Uitility.isOnline(requireContext())) {
                view_model.propertyCategory();
            } else {
                Uitility.nointernetDialog(requireActivity());
            }
            fragmentHomeBinding.srlayout.setRefreshing(false);
        });


    }

    @Override
    protected void initializeOnCreateObject() {

    }

    @Override
    protected void setListeners() {

    }

    private void handleResult(ApiResponse<PropertyCategoryModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    fragmentHomeBinding.srlayout.setRefreshing(false);
                    if (result.getData().status.equals("true")) {
                        List<PropertyCategoryModel.DataEntity> catList = new ArrayList<>();
                        PropertyCategoryModel.DataEntity propertyData = new PropertyCategoryModel.DataEntity();
                        catList = result.getData().data;
                        propertyData.categoryId = "0";
                        propertyData.categoryTitle = "All";
                        propertyData.isselected = true;
                        catList.add(0, propertyData);
                        List<PropertyCategoryModel.DataEntity> finalCatList = catList;
                        AddonAdapters addonAdapters = new AddonAdapters(requireContext(), finalCatList, (type, pos) -> {
                            propertyList.clear();
                            CategoryId = finalCatList.get(pos).categoryId;
                            if (Uitility.isOnline(requireContext())) {
                                HashMap<String, String> reqData = new HashMap<>();
                                reqData.put("city_name", "");
                                reqData.put("country_name", "");
                                reqData.put("user_id", sessionManager.getUSER_ID());
                                view_model.getPropertyList(reqData);
                            } else {
                                Uitility.nointernetDialog(requireActivity());
                            }
                        });
                        fragmentHomeBinding.rvCategories.setAdapter(addonAdapters);
                        if (Uitility.isOnline(requireContext())) {
                            HashMap<String, String> reqData = new HashMap<>();
                            reqData.put("city_name", "");
                            reqData.put("country_name", "");
                            reqData.put("user_id", sessionManager.getUSER_ID());
                            view_model.getPropertyList(reqData);
                        } else {
                            Uitility.nointernetDialog(requireActivity());
                        }
                    } else {
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void handleResult1(ApiResponse<PropertyCategoryModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                // ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                //  ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {

                        filtercatList.clear();
                        filtercatList = result.getData().data;


                    } else {
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void propertyListhandleResult(ApiResponse<PropertyListModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
//                fragmentHomeBinding.ivPB.setVisibility(View.VISIBLE);
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
//                fragmentHomeBinding.ivPB.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
//                fragmentHomeBinding.ivPB.setVisibility(View.GONE);
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {
                        if (result.getData().data.get(0).notification_count.equals("0")) {
                            MainActivity.tvNotCount.setVisibility(View.GONE);
                        } else {
                            MainActivity.tvNotCount.setVisibility(View.VISIBLE);
                            MainActivity.tvNotCount.setText(result.getData().data.get(0).notification_count);
                        }
                        List<PropertyListModel.DataEntity> list = new ArrayList<>();
                        List<PropertyListModel.PropertyListEntity> proplist = new ArrayList<>();
                        list = result.getData().data;

                        for (int i = 0; i < list.size(); i++) {
                            proplist = result.getData().data.get(i).propertyList;
                            if (list.get(i).categoryId.equals(CategoryId)) {
                                propertyList.clear();
                                propertyList.addAll(proplist);
                            }
                        }
                        if (propertyList.size() > 0) {
                            fragmentHomeBinding.ivNoProp.setVisibility(View.GONE);
                            fragmentHomeBinding.rvProperties.setVisibility(View.VISIBLE);
                            propertyAdapters.notifyDataSetChanged();
                        } else {
                            fragmentHomeBinding.ivNoProp.setVisibility(View.VISIBLE);
                            fragmentHomeBinding.rvProperties.setVisibility(View.GONE);
                        }


                        Log.e("propertyList_________", propertyList.toString());

                    } else {
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void amenitieshandleResult(ApiResponse<PropertyAmenitiesModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                // ProgressDialog.hideProgressDialog();
                //  fragmentHomeBinding.ivPB.setVisibility(View.VISIBLE);
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                //ProgressDialog.showProgressDialog(requireActivity());
                // fragmentHomeBinding.ivPB.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                //  ProgressDialog.hideProgressDialog();
                //   fragmentHomeBinding.ivPB.setVisibility(View.GONE);
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {
                        filteramenitiesList = result.getData().data;

                    } else {
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    public static class AddonAdapters extends RecyclerView.Adapter<AddonAdapters.ViewHolder> {

        Context context;
        List<PropertyCategoryModel.DataEntity> list;
        ItemClickListner itemClickListner;

        public AddonAdapters(Context context, List<PropertyCategoryModel.DataEntity> list, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
            this.list = list;
        }

        @Override
        public AddonAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemCategoryBinding itemCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_category, parent,
                    false);
            return new AddonAdapters.ViewHolder(itemCategoryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AddonAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


            holder.itemRowBinding.tvCat.setText(list.get(position).categoryTitle);
            if (list.get(position).isselected) {
                holder.itemRowBinding.layoutCat.setBackground(context.getResources().getDrawable(R.drawable.app_btn));
                holder.itemRowBinding.tvCat.setTextColor(context.getResources().getColor(R.color.white));
                CategoryId = list.get(position).categoryId;
            } else {
                holder.itemRowBinding.layoutCat.setBackground(null);
                holder.itemRowBinding.tvCat.setTextColor(context.getResources().getColor(R.color.black));
            }

            holder.itemRowBinding.layoutCat.setOnClickListener(v -> {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).isselected = false;
                }
                list.get(position).isselected = true;
                notifyDataSetChanged();
                itemClickListner.onClick("", position);
            });

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
            ItemCategoryBinding itemRowBinding;

            public ViewHolder(ItemCategoryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class PropertyAdapters extends RecyclerView.Adapter<PropertyAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;
        private int lastPosition = -1;

        public PropertyAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public PropertyAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemPropertyBinding itemPropertyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_property, parent,
                    false);
            return new PropertyAdapters.ViewHolder(itemPropertyBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull PropertyAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.cardProperty.setOnClickListener(v -> {
                itemClickListner.onClick("", position);
                setAnimation(holder.itemView, position);
            });
            Glide.with(context).load(propertyList.get(position).propertyImageUrl).into(holder.itemRowBinding.ivProImage);
            holder.itemRowBinding.tvTitle.setText(propertyList.get(position).propertyTitle);
            holder.itemRowBinding.tvPrice.setText(propertyList.get(position).hourlyRate + " /hour");

            holder.itemRowBinding.rbrating.setRating(Float.parseFloat(propertyList.get(position).propertyRatting));

            setAnimation(holder.itemView, position);
        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        @Override
        public int getItemCount() {
            return propertyList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemPropertyBinding itemRowBinding;

            public ViewHolder(ItemPropertyBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
//                Toast.makeText(requireContext(), "Select Date range : " +
//                        data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE) + " ~ " +
//                        data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE), Toast.LENGTH_SHORT).show();
                String datecheckin = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE);
                String datecheckout = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM");
                try {
                    Date oneWayTripDate = input.parse(datecheckin);  // parse input
                    Date checkOutDate = input.parse(datecheckout);  // parse input
                    Log.e("===============", "======currentData======" + output.format(oneWayTripDate));
                    tvCheckInDate.setText(output.format(oneWayTripDate));
                    tvCheckOutDate.setText(output.format(checkOutDate));
                    Constant.CheckInDate = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE);
                    Constant.CheckOutDate = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


//                SimpleDateFormat format = new SimpleDateFormat("dd MMM");
//                String date = format.format(Date.parse(data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE)));

            }
        }
    }

    RangeSeekBar seekbar;

    public void FilterBottomSheet() {
        @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.filter_bottom_sheet, null);
        final BottomSheetDialog dialog1 = new BottomSheetDialog(requireContext());
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.setContentView(view1);
        ImageView close = dialog1.findViewById(R.id.close);
        RecyclerView rvPropertyType = dialog1.findViewById(R.id.rvPropertyType);
        RecyclerView rvAmenities = dialog1.findViewById(R.id.rvAmenities);
        TextView tvMin = dialog1.findViewById(R.id.tvMin);
        TextView tvMax = dialog1.findViewById(R.id.tvMax);
        TextView tvCheckinTime = dialog1.findViewById(R.id.tvCheckinTime);
        TextView tvCheckoutTime = dialog1.findViewById(R.id.tvCheckoutTime);
        TextView btnApply = dialog1.findViewById(R.id.btnApply);
        RangeSeekBar seekbar = dialog1.findViewById(R.id.rangeSeekBar);
        Spinner filterSpinner = dialog1.findViewById(R.id.filterSpinner);
        tvCheckOutDate = dialog1.findViewById(R.id.tvCheckOutDate);
        tvCheckInDate = dialog1.findViewById(R.id.tvCheckInDate);
        tvGuestCount = dialog1.findViewById(R.id.tvGuestCount);
        LinearLayout layout = dialog1.findViewById(R.id.layoutDates);
        LinearLayout layoutGuest = dialog1.findViewById(R.id.layoutGuest);

        tvMin.setText("5");
        tvMax.setText("50");
        Constant.FromRate = tvMin.getText().toString();
        Constant.ToRate = tvMax.getText().toString();

        assert seekbar != null;
        seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(
                    final RangeSeekBar seekBar, final int progressStart, final int progressEnd, final boolean fromUser) {
                tvMin.setText(String.valueOf(progressStart));
                tvMax.setText(String.valueOf(progressEnd));
                Constant.FromRate = tvMin.getText().toString();
                Constant.ToRate = tvMax.getText().toString();
                Log.e("minmax", Constant.FromRate + "____" + Constant.ToRate);

            }
            @Override
            public void onStartTrackingTouch(final RangeSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar seekBar) {
            }
        });



        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    Log.e("spinner", item.toString());
                    if (item.toString().equals("Hourly")) {
                        seekbar.setMax(100);
                        Constant.RateType = "307";
                    } else if (item.toString().equals("Daily")) {
                        Constant.RateType = "308";
                        seekbar.setMax(1500);
                    } else if (item.toString().equals("Weekly")) {
                        Constant.RateType = "309";
                        seekbar.setMax(3000);
                    } else if (item.toString().equals("Monthly")) {
                        Constant.RateType = "310";
                        seekbar.setMax(5000);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvCheckinTime.setOnClickListener(v -> openClock(tvCheckinTime, "checkin"));
        tvCheckoutTime.setOnClickListener(v -> openClock(tvCheckoutTime, "checkout"));

        layout.setOnClickListener(v -> {
            AirCalendarIntent intent = new AirCalendarIntent(requireContext());
            intent.setSelectButtonText("Select");
            intent.setResetBtnText("Reset");
            intent.setWeekStart(Calendar.MONDAY);
            startActivityForResult(intent, REQUEST_CODE);
        });

        layoutGuest.setOnClickListener(v -> GuestBottomSheet());


        PropertyTypeAdapters propertyTypeAdapters = new PropertyTypeAdapters(requireContext(), (type, pos) -> {

        });
        rvPropertyType.setAdapter(propertyTypeAdapters);

        AmenitiesAdapters amenitiesAdapters = new AmenitiesAdapters(requireContext(), (type, pos) -> {

        });
        rvAmenities.setAdapter(amenitiesAdapters);
        close.setOnClickListener(v -> {
            dialog1.dismiss();
        });

        assert btnApply != null;
        btnApply.setOnClickListener(v -> {
            if (Constant.CategoryIds.size() > 0) {
                if (Uitility.isOnline(requireContext())) {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("category_ids",android.text.TextUtils.join(",", Constant.CategoryIds));
                    hashMap.put("city_name",    Constant.CityName);
                    hashMap.put("country_name", Constant.CountryName);
                    hashMap.put("checkin_date", Constant.CheckInDate);
                    hashMap.put("checkin_time", Constant.CheckInTime);
                    hashMap.put("checkout_date",Constant.CheckOutDate);
                    hashMap.put("checkout_time",Constant.CheckOutTime);
                    hashMap.put("from_rate",    Constant.FromRate);
                    hashMap.put("to_rate",      Constant.ToRate);
                    hashMap.put("allowed_guest",Constant.Guests);
                    hashMap.put("rate_type",    Constant.RateType);
                    hashMap.put("anenities_ids",android.text.TextUtils.join(",", Constant.AmenitiesIds));
                    if(sessionManager.isLogin()){
                        hashMap.put("user_id",sessionManager.getUSER_ID());
                    }
                    view_model.SearchProperty(hashMap);
                } else {
                    Uitility.nointernetDialog(requireActivity());
                }

            } else
                Toast.makeText(getContext(), "Please Select Property Type", Toast.LENGTH_SHORT).show();
        });

        dialog1.show();
    }

    private void handleResultFilter(ApiResponse<PropertySearchModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error Search- " + result.getError().getMessage());
               Toast.makeText(mActivity, "PROPERTY NOT FOUND", Toast.LENGTH_SHORT).show();

                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        if(result.getData().data.size()>0){
                            startActivity(new Intent(getContext(), MainActivity.class).putExtra("value", "explore"));
                        }
                        else {
                            CookieBar.build(requireActivity())
                                    .setTitle(R.string.app_name)
                                    .setTitleColor(R.color.black)
                                    .setMessageColor(R.color.black)
                                    .setMessage( result.getData().message)
                                    .setBackgroundColor(R.color.colorPrimary)
                                    .setIconAnimation(R.animator.icon_anim)
                                    .setIcon(R.drawable.logo_black)
                                    .setDuration(5000) // 5 seconds
                                    .show();
                        }
                    }
                    else{
                        Toast.makeText(mActivity, result.getData().message, Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(mActivity, result.getData().message, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void openClock(TextView textView, String value) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(requireContext(), (timePicker, selectedHour, selectedMinute) -> {
            mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            mcurrentTime.set(Calendar.MINUTE, selectedMinute);
            SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            boolean isPM = (selectedHour >= 12);
            textView.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, minute, isPM ? "PM" : "AM"));
//            textView.setText(mFormat.format(mcurrentTime.getTime()));
            if (value.equals("checkin")) {
                Constant.CheckInTime = mFormat.format(mcurrentTime.getTime());
            } else Constant.CheckOutTime = mFormat.format(mcurrentTime.getTime());
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void GuestBottomSheet() {
        @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.guest_bottom_sheet, null);
        final BottomSheetDialog dialog1 = new BottomSheetDialog(requireContext());
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.setContentView(view1);
        ImageView close = dialog1.findViewById(R.id.close);
        EditText etGuest = dialog1.findViewById(R.id.etGuest);
        TextView btnProceed = dialog1.findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etGuest.getText().toString())) {
                Toast.makeText(requireContext(), "Please Enter at least one", Toast.LENGTH_SHORT).show();
                return;
            }
            tvGuestCount.setText(etGuest.getText().toString());
            Constant.Guests = etGuest.getText().toString();
            dialog1.dismiss();
        });
        close.setOnClickListener(v -> dialog1.dismiss());
        dialog1.show();
    }


    public static class PropertyTypeAdapters extends RecyclerView.Adapter<PropertyTypeAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public PropertyTypeAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public PropertyTypeAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewAddonsRequestBinding itemViewAddonsRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_addons_request, parent,
                    false);
            return new PropertyTypeAdapters.ViewHolder(itemViewAddonsRequestBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull PropertyTypeAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.cbName.setText(filtercatList.get(position).categoryTitle);
            if (filtercatList.get(position).isselected) {
                holder.itemRowBinding.cbName.setChecked(true);
            } else {
                holder.itemRowBinding.cbName.setChecked(false);
            }
            holder.itemRowBinding.cbName.setOnClickListener(v -> {
                if (filtercatList.get(position).isselected) {
                    filtercatList.get(position).isselected = false;
                    Constant.CategoryIds.remove(filtercatList.get(position).categoryId);
//                    Constant.Amenities.remove(list.get(position).anenitiesTitle);
                } else {
                    filtercatList.get(position).isselected = true;
                    Constant.CategoryIds.add(filtercatList.get(position).categoryId);
//                    Constant.Amenities.add(list.get(position).anenitiesTitle);
                }
                notifyDataSetChanged();
//                for (int i = 0; i < filtercatList.size(); i++) {
//                    filtercatList.get(i).isselected = false;
//                }
//                Constant.CategoryIds.add(filtercatList.get(position).categoryId);
//                Log.e("pos+cat", filtercatList.get(position).categoryId + "___" + filtercatList.get(position).categoryTitle);
//                filtercatList.get(position).isselected = true;
//                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return filtercatList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewAddonsRequestBinding itemRowBinding;

            public ViewHolder(ItemViewAddonsRequestBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class AmenitiesAdapters extends RecyclerView.Adapter<AmenitiesAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public AmenitiesAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public AmenitiesAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewAddonsRequestBinding itemViewAddonsRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_addons_request, parent,
                    false);
            return new AmenitiesAdapters.ViewHolder(itemViewAddonsRequestBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AmenitiesAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            //   holder.itemRowBinding.cardProperty.setOnClickListener(v -> itemClickListner.onClick("",position));
            holder.itemRowBinding.cbName.setText(filteramenitiesList.get(position).anenitiesTitle);
            if (filteramenitiesList.get(position).isselected) {
                holder.itemRowBinding.cbName.setChecked(true);
            } else {
                holder.itemRowBinding.cbName.setChecked(false);
            }
            holder.itemRowBinding.cbName.setOnClickListener(v -> {
                if (filteramenitiesList.get(position).isselected) {
                    filteramenitiesList.get(position).isselected = false;
                    Constant.AmenitiesIds.remove(filteramenitiesList.get(position).anenitiesId);
                } else {
                    filteramenitiesList.get(position).isselected = true;
                    Constant.AmenitiesIds.add(filteramenitiesList.get(position).anenitiesId);
                }
                notifyDataSetChanged();

            });
        }

        @Override
        public int getItemCount() {
            return filteramenitiesList.size();
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