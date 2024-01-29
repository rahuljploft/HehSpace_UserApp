package com.hehspace_userapp.components.sidemenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.property.BookNowActivity;
import com.hehspace_userapp.components.property.ImageSliderActivity;
import com.hehspace_userapp.components.property.PropertDetailsActivity;
import com.hehspace_userapp.databinding.ActivityRequestDetailsBinding;
import com.hehspace_userapp.databinding.ItemAmenitiesBinding;
import com.hehspace_userapp.databinding.LayoutAddonViewBinding;
import com.hehspace_userapp.databinding.LayoutGalleryBinding;
import com.hehspace_userapp.databinding.LayoutHighlightBinding;
import com.hehspace_userapp.databinding.LayoutImageSliderBinding;
import com.hehspace_userapp.model.AdditionalSeviceModel;
import com.hehspace_userapp.model.PropertyDetailModel;
import com.hehspace_userapp.model.RequestDetailsModel;
import com.hehspace_userapp.model.ReviewRequestModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestDetailsActivity extends BaseBindingActivity {
    ActivityRequestDetailsBinding activityRequestDetailsBinding;
    RequestDetailsView_Model requestDetailsView_model;
    public static List<RequestDetailsModel.PropertyImagesEntity> imageList = new ArrayList<>();
    public static List<RequestDetailsModel.AddonServicesEntity> propertyServicesList = new ArrayList<>();
    public static List<String> highlightsList = new ArrayList<>();
    public static List<String> amenitiesList = new ArrayList<>();
    ReviewRequestModel reviewRequestModel = new ReviewRequestModel();
    AmenitiesAdapters amenitiesAdapters;
    HighlightsAdapters highlightsAdapters;
    ImageAdapters imageAdapters;
    Image_Slider image_slider;
    AddonAdapters addonAdapters;
    String reqstatus = "";

    @Override
    protected void setBinding() {
        activityRequestDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_request_details);
        requestDetailsView_model = new RequestDetailsView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
    mActivity = this;
    }

    @Override
    protected void initializeObject() {
        requestDetailsView_model.livedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        reqstatus = getIntent().getStringExtra("reqstatus");
        if (Uitility.isOnline(this)) {
            requestDetailsView_model.getRequestDetails(Integer.parseInt(getIntent().getStringExtra("reqid")));
        } else {
            Uitility.nointernetDialog(this);
        }
        activityRequestDetailsBinding.rvHighlights.setHasFixedSize(true);
        activityRequestDetailsBinding.rvHighlights.setLayoutManager(new GridLayoutManager(this,2));
        highlightsAdapters = new HighlightsAdapters(this, (type, pos) -> {

        });
        activityRequestDetailsBinding.rvHighlights.setAdapter(highlightsAdapters);

        activityRequestDetailsBinding.rvAmenities.setHasFixedSize(true);
        activityRequestDetailsBinding.rvAmenities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        amenitiesAdapters = new AmenitiesAdapters(this, (type, pos) -> {

        });
        activityRequestDetailsBinding.rvAmenities.setAdapter(amenitiesAdapters);

        activityRequestDetailsBinding.rvAddons.setHasFixedSize(true);
        activityRequestDetailsBinding.rvAddons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addonAdapters = new AddonAdapters(this, (type, pos) -> {

        });
        activityRequestDetailsBinding.rvAddons.setAdapter(addonAdapters);



        imageAdapters = new ImageAdapters(this, (type, pos) -> {
            startActivity(new Intent(this,ImageSliderActivity.class).putExtra("value","req"));
        });
        activityRequestDetailsBinding.rvGallery.setAdapter(imageAdapters);
        if(reqstatus.equals("ACCEPTED")){
            activityRequestDetailsBinding.layoutBook.setBackground(this.getResources().getDrawable(R.drawable.app_btn));
        }
        else activityRequestDetailsBinding.layoutBook.setBackground(this.getResources().getDrawable(R.drawable.grey_bg));
    }

    @Override
    protected void setListeners() {
        activityRequestDetailsBinding.ivBack.setOnClickListener(this);
        activityRequestDetailsBinding.layoutBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
        if(view.getId() == R.id.layoutBook){
            if(reqstatus.equals("ACCEPTED")){
                startActivity(new Intent(this, BookNowActivity.class).
                        putExtra("reviewRequestModel",reviewRequestModel).
                        putExtra("reqid",getIntent().getStringExtra("reqid")));
            }
            else{
                Toast.makeText(this, "Please wait for Host confirmation", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void handleResult(ApiResponse<RequestDetailsModel> result) {
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
                        imageList = result.getData().data.propertyImages;
                        propertyServicesList = result.getData().data.addonServices;
                        activityRequestDetailsBinding.tvCatergory.setText(result.getData().data.propertyCategory);
                        activityRequestDetailsBinding.tvCat.setText(result.getData().data.propertyCategory);
                        activityRequestDetailsBinding.tvTitle.setText(result.getData().data.propertyTitle);
                        activityRequestDetailsBinding.tvLocation.setText(result.getData().data.propertyLocation);
                        activityRequestDetailsBinding.tvDesc.setText(result.getData().data.propertyDetails);
                        PropertDetailsActivity.makeTextViewResizable(activityRequestDetailsBinding.tvDesc, 4, "See More", true);
                        activityRequestDetailsBinding.tvTime.setText(result.getData().data.propertyTime);
                        if(imageList.size()>0){
                            reviewRequestModel.setPropertyimg(imageList.get(0).propertyImageUrl);
                        }
                        reviewRequestModel.setPropertyname(result.getData().data.propertyTitle);
                        reviewRequestModel.setPropertylocation(result.getData().data.propertyLocation);
                        reviewRequestModel.setReviewrating(result.getData().data.propertyRatting+" ( "+result.getData().data.propertyReview+" Reviews )");
                        reviewRequestModel.setPropertyType(result.getData().data.propertyCategory);
                        reviewRequestModel.setPropertyid(getIntent().getStringExtra("property_id"));
                        reviewRequestModel.setTotalPrice(result.getData().data.priceCalculation.totalAmount);
                        reviewRequestModel.setDailyPrice(result.getData().data.priceCalculation.dailyAmount);
                        reviewRequestModel.setWeeklyPrice(result.getData().data.priceCalculation.weeklyAmount);
                        reviewRequestModel.setHourlyPrice(result.getData().data.priceCalculation.hourlyAmount);
                        reviewRequestModel.setMonthlyPrice(result.getData().data.priceCalculation.monthlyAmount);
                        reviewRequestModel.setCleaningFee(result.getData().data.priceCalculation.cleanerFee);
                        reviewRequestModel.setExtraServiceCharge(result.getData().data.priceCalculation.servicesAmount);
                        reviewRequestModel.setServiceTax(result.getData().data.priceCalculation.taxAmount);

                        reviewRequestModel.setHourly_rate(result.getData().data.propertyRate.hourlyRate);
                        reviewRequestModel.setWeekly_rate(result.getData().data.propertyRate.weeklyRate);
                        reviewRequestModel.setDaily_rate(result.getData().data.propertyRate.dailyRate);
                        reviewRequestModel.setMonthly_rate(result.getData().data.propertyRate.monthlyRate);

                        reviewRequestModel.setStay_hours(result.getData().data.timeCalculation.stayHours);
                        reviewRequestModel.setStay_days(result.getData().data.timeCalculation.stayDays);
                        reviewRequestModel.setStay_weeks(result.getData().data.timeCalculation.stayWeeks);
                        reviewRequestModel.setStay_months(result.getData().data.timeCalculation.stayMonths);

                        reviewRequestModel.setCheckindate(result.getData().data.checkinDate);
                        reviewRequestModel.setCheckoutdate(result.getData().data.checkoutDate);
                        reviewRequestModel.setCheckintime(result.getData().data.checkinTime);
                        reviewRequestModel.setCheckouttime(result.getData().data.checkoutTime);
                        reviewRequestModel.setGuestCount(result.getData().data.numberOfGuest);
//                        reviewRequestModel.setProperty_amount(result.getData().data.calculatePrice.propertyAmount);
//                        reviewRequestModel.setTax_percentage(result.getData().data.calculatePrice.taxPercentage);

                        activityRequestDetailsBinding.tvRating.setText(result.getData().data.propertyRatting+" ( "+result.getData().data.propertyReview+" Reviews )");
                        activityRequestDetailsBinding.tvHostName.setText(result.getData().data.hostDetails.fullName);
                        Glide.with(this)
                                .load(result.getData().data.hostDetails.userImage)
                                .error(R.drawable.user_dummy)
                                .into(activityRequestDetailsBinding.tvHostImage);
                        highlightsList = Arrays.asList(result.getData().data.propertyHiglights.split("\\s*,\\s*"));
                        amenitiesList = Arrays.asList(result.getData().data.propertyAnenities.split("\\s*,\\s*"));
                        image_slider = new Image_Slider(this,imageList);
                        activityRequestDetailsBinding.itemPicker.setSliderAdapter(image_slider);
                        if(propertyServicesList.size()>0){
                            addonAdapters.notifyDataSetChanged();
                            activityRequestDetailsBinding.rvAddons.setVisibility(View.VISIBLE);
                            activityRequestDetailsBinding.layoutAddOnAll.setVisibility(View.VISIBLE);
                        }
                        else {
                            activityRequestDetailsBinding.rvAddons.setVisibility(View.GONE);
                            activityRequestDetailsBinding.layoutAddOnAll.setVisibility(View.GONE);
                        }
                        amenitiesAdapters.notifyDataSetChanged();
                        highlightsAdapters.notifyDataSetChanged();
                        imageAdapters.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //image slider
    public class Image_Slider extends SliderViewAdapter<Image_Slider.SliderAdapterVH> {
        Context context;
        LayoutInflater inflater;
        List<RequestDetailsModel.PropertyImagesEntity> imageList;

        Image_Slider(Context context,List<RequestDetailsModel.PropertyImagesEntity> imageList) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.imageList = imageList;
        }

        @Override
        public Image_Slider.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            LayoutImageSliderBinding imageSliderAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_image_slider, parent,
                    false);
            return new Image_Slider.SliderAdapterVH(imageSliderAdapterBinding);
        }

        @Override
        public void onBindViewHolder(Image_Slider.SliderAdapterVH holder, int position) {

            Glide.with(context)
                    .load(imageList.get(position).propertyImageUrl)
                    .error(R.drawable.logo)
                    .into(holder.imageSliderAdapterBinding.bannerImage);

            holder.imageSliderAdapterBinding.bannerImage.setOnClickListener(v ->  startActivity(new Intent(context, ImageSliderActivity.class)
                    .putExtra("value","req")));



        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return imageList.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            LayoutImageSliderBinding imageSliderAdapterBinding;

            public SliderAdapterVH(LayoutImageSliderBinding imageSliderAdapterBinding) {
                super(imageSliderAdapterBinding.getRoot());
                this.imageSliderAdapterBinding = imageSliderAdapterBinding;
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
            ItemAmenitiesBinding itemAmenitiesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_amenities, parent,
                    false);
            return new AmenitiesAdapters.ViewHolder(itemAmenitiesBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AmenitiesAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvAmenities.setText(amenitiesList.get(position));

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return amenitiesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemAmenitiesBinding itemRowBinding;

            public ViewHolder(ItemAmenitiesBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

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
            LayoutAddonViewBinding layoutAddonViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_addon_view, parent,
                    false);
            return new AddonAdapters.ViewHolder(layoutAddonViewBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull AddonAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvPrice.setText(propertyServicesList.get(position).servicesRate);
            holder.itemRowBinding.tvService.setText(propertyServicesList.get(position).servicesTitle);

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return propertyServicesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutAddonViewBinding itemRowBinding;

            public ViewHolder(LayoutAddonViewBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ViewHolder> {
        Context context;
        ItemClickListner itemClickListner;

        public ImageAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public ImageAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutGalleryBinding layoutGalleryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_gallery, parent,
                    false);
            return new ImageAdapters.ViewHolder(layoutGalleryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ImageAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context)
                    .load(imageList.get(position).propertyImageUrl)
                    .error(R.drawable.logo)
                    .into(holder.itemRowBinding.ivImage);

            holder.itemRowBinding.cardImage.setOnClickListener(v -> itemClickListner.onClick("",position));


        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutGalleryBinding itemRowBinding;

            public ViewHolder(LayoutGalleryBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class HighlightsAdapters extends RecyclerView.Adapter<HighlightsAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public HighlightsAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public HighlightsAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutHighlightBinding layoutHighlightBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_highlight, parent,
                    false);
            return new HighlightsAdapters.ViewHolder(layoutHighlightBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull HighlightsAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvHighlights.setText(highlightsList.get(position));

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return highlightsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutHighlightBinding itemRowBinding;

            public ViewHolder(LayoutHighlightBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


}