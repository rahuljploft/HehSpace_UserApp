package com.hehspace_userapp.components.property;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.slider.RangeSlider;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.calender.MyCalendarActivity;
import com.hehspace_userapp.components.calender.filterCustomCalender.AirCalendarDatePickerActivity;
import com.hehspace_userapp.components.fragment.dashboard.home.HomeFragment;
import com.hehspace_userapp.databinding.ActivityPropertDetailsBinding;
import com.hehspace_userapp.databinding.ItemAmenitiesBinding;
import com.hehspace_userapp.databinding.ItemRatingBinding;
import com.hehspace_userapp.databinding.ItemViewAddonsRequestBinding;
import com.hehspace_userapp.databinding.ItemViewCategoryBinding;
import com.hehspace_userapp.databinding.LayoutAddonViewBinding;
import com.hehspace_userapp.databinding.LayoutGalleryBinding;
import com.hehspace_userapp.databinding.LayoutHighlightBinding;
import com.hehspace_userapp.databinding.LayoutImageSliderBinding;
import com.hehspace_userapp.model.AdditionalSeviceModel;
import com.hehspace_userapp.model.CheckAvailabilityModel;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.DateModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertyDetailModel;
import com.hehspace_userapp.model.ReviewRequestModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.Constant;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;
import com.hehspace_userapp.util.custom_snackbar.CookieBar;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PropertDetailsActivity extends BaseBindingActivity implements MyCalendarActivity.ItemClickListener {

    ActivityPropertDetailsBinding activityPropertDetailsBinding;
    PropertyDetailsView_Model view_model;
    TextView tvGuestCount;
    TextView tvGuestcounttext;
    public static ArrayList<PropertyDetailModel.PropertyImagesEntity> imageList = new ArrayList<>();
    public static List<String> categoryList = new ArrayList<>();
    public static List<PropertyDetailModel.PropertyService> propertyServicesList = new ArrayList<>();
    public static List<PropertyDetailModel.PropertyReviewList> propertyReviewList = new ArrayList<>();
    public static List<String> availableCheckInTime = new ArrayList<>();
    public static List<String> availableCheckOutTime = new ArrayList<>();
    public static List<String> highlightsList = new ArrayList<>();
    public static List<String> amenitiesList = new ArrayList<>();
    public static List<AdditionalSeviceModel> mainadditionalList = new ArrayList<>();
    public static String checkinDate = "";
    public static String checkoutDate = "";
    @SuppressLint("NewApi")
    private int month, year;
    List<DateModel> dateModelList = new ArrayList<>();
    AmenitiesAdapters amenitiesAdapters;
    HighlightsAdapters highlightsAdapters;
    ImageAdapters imageAdapters;
    Image_Slider image_slider;
    AddonAdapters addonAdapters;
    RatingAdapters ratingAdapters;
    CategoryAdapters categoryAdapters;
    public static String rate = "", review = "", fivestart = "", value = "";
    String reqstatus = "";
    String reqid = "";
    String categoryid = "";
    String guestmaxCount = "";
    public static String propid = "";
    ReviewRequestModel reviewRequestModel = new ReviewRequestModel();

    @Override
    protected void setBinding() {
        activityPropertDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_propert_details);
        view_model = new PropertyDetailsView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        if (getIntent().hasExtra("value")) {
            value = getIntent().getStringExtra("value");
        }
        if (getIntent().hasExtra("reqstatus")) {
            reqstatus = getIntent().getStringExtra("reqstatus");
        }
        if (getIntent().hasExtra("reqid")) {
            reqid = getIntent().getStringExtra("reqid");
        }
      if (getIntent().hasExtra("categoryid")) {
          Log.e("categoryid",categoryid);
          if(getIntent().getStringExtra("categoryid").equals("0")){
              categoryid = "All";
          }
          else  categoryid = getIntent().getStringExtra("categoryid");
        }
        propid = getIntent().getStringExtra("property_id");
        view_model.livedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        view_model.livedata1.observe(this, propertyCategoryModelApiResponse -> handleResultFav(propertyCategoryModelApiResponse));
        view_model.livedata2.observe(this, propertyCategoryModelApiResponse -> handleResultReview(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(this)) {
            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("property_id", getIntent().getStringExtra("property_id"));
            reqData.put("category_id", categoryid);
            reqData.put("user_id", sessionManager.getUSER_ID());
            view_model.getPropertyDetails(reqData);
        } else {
            Uitility.nointernetDialog(this);
        }

        if (value.equals("req")) {
            activityPropertDetailsBinding.layoutRatingAll.setVisibility(View.GONE);
            activityPropertDetailsBinding.layoutRate.setVisibility(View.GONE);
            activityPropertDetailsBinding.rvRating.setVisibility(View.GONE);
            activityPropertDetailsBinding.bottomSheet.setVisibility(View.GONE);
            activityPropertDetailsBinding.ivAddFav.setVisibility(View.GONE);
            activityPropertDetailsBinding.layoutBook.setVisibility(View.VISIBLE);
        } else {
            activityPropertDetailsBinding.layoutRatingAll.setVisibility(View.VISIBLE);
            activityPropertDetailsBinding.layoutRate.setVisibility(View.VISIBLE);
            activityPropertDetailsBinding.rvRating.setVisibility(View.VISIBLE);
            activityPropertDetailsBinding.bottomSheet.setVisibility(View.VISIBLE);
            activityPropertDetailsBinding.ivAddFav.setVisibility(View.VISIBLE);
            activityPropertDetailsBinding.layoutBook.setVisibility(View.GONE);
        }
        activityPropertDetailsBinding.rvCategory.setHasFixedSize(true);
        activityPropertDetailsBinding.rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapters = new CategoryAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvCategory.setAdapter(categoryAdapters);

        activityPropertDetailsBinding.rvHighlights.setHasFixedSize(true);
        activityPropertDetailsBinding.rvHighlights.setLayoutManager(new GridLayoutManager(this, 2));
        highlightsAdapters = new HighlightsAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvHighlights.setAdapter(highlightsAdapters);

        activityPropertDetailsBinding.rvAmenities.setHasFixedSize(true);
        activityPropertDetailsBinding.rvAmenities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        amenitiesAdapters = new AmenitiesAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvAmenities.setAdapter(amenitiesAdapters);

        activityPropertDetailsBinding.rvAddons.setHasFixedSize(true);
        activityPropertDetailsBinding.rvAddons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addonAdapters = new AddonAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvAddons.setAdapter(addonAdapters);

        activityPropertDetailsBinding.rvRating.setHasFixedSize(true);
        activityPropertDetailsBinding.rvRating.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ratingAdapters = new RatingAdapters(this, (type, pos) -> {

        });
        activityPropertDetailsBinding.rvRating.setAdapter(ratingAdapters);

        imageAdapters = new ImageAdapters(this, (type, pos) -> {
            startActivity(new Intent(this, ImageSliderActivity.class)
                    .putExtra("value", "pro"));
        });

        activityPropertDetailsBinding.rvGallery.setAdapter(imageAdapters);

        activityPropertDetailsBinding.ivAddFav.setOnClickListener(v -> {
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("property_id", getIntent().getStringExtra("property_id"));
                reqData.put("user_id", sessionManager.getUSER_ID());
                view_model.addToFav(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }
        });

        activityPropertDetailsBinding.layoutRatingAll.setOnClickListener(v -> {
            startActivity(new Intent(this, RatingReviewActivity.class));
        });

        activityPropertDetailsBinding.layoutAddOnAll .setOnClickListener(v -> {
            startActivity(new Intent(this, AddOnServicesActivity.class));
        });
        if (sessionManager.isLogin()) {
            activityPropertDetailsBinding.ivAddFav.setVisibility(View.VISIBLE);
        } else  activityPropertDetailsBinding.ivAddFav.setVisibility(View.GONE);
        if (reqstatus.equals("ACCEPTED")) {
            activityPropertDetailsBinding.layoutBook.setBackground(this.getResources().getDrawable(R.drawable.app_btn));
        } else
            activityPropertDetailsBinding.layoutBook.setBackground(this.getResources().getDrawable(R.drawable.grey_bg));


    }

    @Override
    protected void setListeners() {
        activityPropertDetailsBinding.layoutRequest.setOnClickListener(this);
        activityPropertDetailsBinding.ivBack.setOnClickListener(this);
        activityPropertDetailsBinding.layoutBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
        if (view.getId() == R.id.layoutRequest) {
            if (sessionManager.isLogin()) {
                RequestBottomSheet();
            } else alertGuest();
        }
        if (view.getId() == R.id.layoutBook) {
            if (reqstatus.equals("ACCEPTED")) {
                startActivity(new Intent(this, BookNowActivity.class).putExtra("reqid", reqid));
            } else {
                Toast.makeText(this, "Please wait for Host confirmation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleResult(ApiResponse<PropertyDetailModel> result) {
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
                    if (result.getData().status.equals("true")) {
                        imageList = result.getData().data.propertyImages;
                        propertyServicesList = result.getData().data.propertyServices;
                        propertyReviewList = result.getData().data.propertyReviewList;
                        availableCheckInTime = result.getData().data.availableCheckInTime;
                        availableCheckOutTime = result.getData().data.availableCheckOutTime;
                        activityPropertDetailsBinding.tvCatergory.setText(result.getData().data.propertyCategory);
                        categoryList=Arrays.asList(result.getData().data.propertyCategory.split("\\s*,\\s*"));
                        activityPropertDetailsBinding.tvTitle.setText(result.getData().data.propertyTitle);
                        activityPropertDetailsBinding.tvLocation.setText(result.getData().data.propertyLocation);
                        activityPropertDetailsBinding.tvDesc.setText(result.getData().data.propertyDetails);
                        makeTextViewResizable(activityPropertDetailsBinding.tvDesc, 4, "See More", true);
                        activityPropertDetailsBinding.tvTotalRating.setText(result.getData().data.propertyRatting);
                        activityPropertDetailsBinding.tvTotalReviews.setText(result.getData().data.propertyReview);
                        activityPropertDetailsBinding.tvFiveStar.setText(result.getData().data.fiveStartRatio + " %");
                        rate = result.getData().data.propertyRatting;
                        review = result.getData().data.propertyReview;
                        fivestart = result.getData().data.fiveStartRatio + " %";
                        guestmaxCount = result.getData().data.noOfGuest;
                        activityPropertDetailsBinding.tvPrice.setText(result.getData().data.hourlyRate + " /hour");
                        activityPropertDetailsBinding.tvTime.setText(result.getData().data.propertyTime);
                        if (imageList.size() > 0) {
                            reviewRequestModel.setPropertyimg(imageList.get(0).propertyImageUrl);
                        }
                        reviewRequestModel.setPropertyname(result.getData().data.propertyTitle);
                        reviewRequestModel.setPropertylocation(result.getData().data.propertyLocation);
                        reviewRequestModel.setReviewrating(result.getData().data.propertyRatting + " ( " + result.getData().data.propertyReview + " Reviews )");
                        reviewRequestModel.setPropertyType(result.getData().data.propertyCategory);
                        reviewRequestModel.setPropertyid(getIntent().getStringExtra("property_id"));
                        reviewRequestModel.setCategoryid(result.getData().data.category_id);

                        if (result.getData().data.wishlistStatus.equals("true")) {
                            Glide.with(this)
                                    .load(R.drawable.ic_like)
                                    .error(R.drawable.ic_like)
                                    .into(activityPropertDetailsBinding.ivAddFav);
                        } else {
                            Glide.with(this)
                                    .load(R.drawable.ic_likeborder)
                                    .error(R.drawable.ic_likeborder)
                                    .into(activityPropertDetailsBinding.ivAddFav);

                        }
                        activityPropertDetailsBinding.tvRating.setText(result.getData().data.propertyRatting + " ( " + result.getData().data.propertyReview + " Reviews )");
                        activityPropertDetailsBinding.tvHostName.setText(result.getData().data.hostDetails.fullName);
                        Glide.with(this)
                                .load(result.getData().data.hostDetails.userImage)
                                .error(R.drawable.user_dummy)
                                .into(activityPropertDetailsBinding.tvHostImage);
                        highlightsList = Arrays.asList(result.getData().data.propertyHiglights.split("\\s*,\\s*"));
                        amenitiesList = Arrays.asList(result.getData().data.propertyAnenities.split("\\s*,\\s*"));
                        image_slider = new Image_Slider(this, imageList);
                        activityPropertDetailsBinding.itemPicker.setSliderAdapter(image_slider);
                        if (propertyServicesList.size() > 0) {
                            addonAdapters.notifyDataSetChanged();
                            activityPropertDetailsBinding.rvAddons.setVisibility(View.VISIBLE);
                            activityPropertDetailsBinding.layoutAddOnAll.setVisibility(View.VISIBLE);
                        } else {
                            activityPropertDetailsBinding.rvAddons.setVisibility(View.GONE);
                            activityPropertDetailsBinding.layoutAddOnAll.setVisibility(View.GONE);
                        }
                        amenitiesAdapters.notifyDataSetChanged();
                        highlightsAdapters.notifyDataSetChanged();
                        imageAdapters.notifyDataSetChanged();
                        ratingAdapters.notifyDataSetChanged();
                        categoryAdapters.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void handleResultFav(ApiResponse<CommonModel> result) {
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
                    if (result.getData().status.equals("true")) {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                        if (result.getData().message.equals("PROPERTY SUCCESSFULLY REMOVED FROM WISHLIST")) {
                            Glide.with(this)
                                    .load(R.drawable.ic_likeborder)
                                    .error(R.drawable.ic_likeborder)
                                    .into(activityPropertDetailsBinding.ivAddFav);

                        } else {
                            Glide.with(this)
                                    .load(R.drawable.ic_like)
                                    .error(R.drawable.ic_like)
                                    .into(activityPropertDetailsBinding.ivAddFav);

                        }
                    } else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //image slider
    public class Image_Slider extends SliderViewAdapter<Image_Slider.SliderAdapterVH> {
        Context context;
        LayoutInflater inflater;
        ArrayList<PropertyDetailModel.PropertyImagesEntity> imageList;

        Image_Slider(Context context, ArrayList<PropertyDetailModel.PropertyImagesEntity> imageList) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.imageList = imageList;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            LayoutImageSliderBinding imageSliderAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_image_slider, parent,
                    false);
            return new SliderAdapterVH(imageSliderAdapterBinding);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH holder, int position) {

            Glide.with(context)
                    .load(imageList.get(position).propertyImageUrl)
                    .error(R.drawable.logo)
                    .into(holder.imageSliderAdapterBinding.bannerImage);

            holder.imageSliderAdapterBinding.bannerImage.setOnClickListener(v -> startActivity(new Intent(context, ImageSliderActivity.class)
                    .putExtra("value", "pro")));


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

    public static class CategoryAdapters extends RecyclerView.Adapter<CategoryAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public CategoryAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public CategoryAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewCategoryBinding itemViewCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_category, parent,
                    false);
            return new CategoryAdapters.ViewHolder(itemViewCategoryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull CategoryAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvCat.setText(categoryList.get(position));

        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewCategoryBinding itemRowBinding;

            public ViewHolder(ItemViewCategoryBinding itemRowBinding) {
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

            holder.itemRowBinding.tvPrice.setText(propertyServicesList.get(position).services_rate);
            holder.itemRowBinding.tvService.setText(propertyServicesList.get(position).services_title);

        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return Math.min(propertyServicesList.size(), 2);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutAddonViewBinding itemRowBinding;

            public ViewHolder(LayoutAddonViewBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }


    public static class RequestAddonAdapters extends RecyclerView.Adapter<RequestAddonAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public RequestAddonAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public RequestAddonAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewAddonsRequestBinding layoutAddonViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_addons_request, parent,
                    false);
            return new RequestAddonAdapters.ViewHolder(layoutAddonViewBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RequestAddonAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.cbName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (holder.itemRowBinding.cbName.isChecked()) {
                    if (propertyServicesList.get(position).isselected) {
                        propertyServicesList.get(position).isselected = false;
                    } else {
                        propertyServicesList.get(position).isselected = true;
                        if (propertyServicesList.get(position).isselected) {

                            JSONArray jsonArray = new JSONArray();
                            if (mainadditionalList.size() > 0) {
                                for (int i = 0; i < mainadditionalList.size(); i++) {

                                    JSONObject myJsonObject = new JSONObject();
                                    try {
                                        myJsonObject.put("services_title", propertyServicesList.get(position).services_title);
                                        myJsonObject.put("services_rate", propertyServicesList.get(position).services_rate);
                                        jsonArray.put(myJsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                            Log.d("qwerty", jsonArray.toString());
                        }


                    }
                    notifyDataSetChanged();
                }
            });


            holder.itemRowBinding.cbName.setOnClickListener(v -> {
                if (propertyServicesList.get(position).isselected) {
                    propertyServicesList.get(position).isselected = false;

                } else {
                    propertyServicesList.get(position).isselected = true;

                }
                notifyDataSetChanged();

            });

            holder.itemRowBinding.cbName.setText(propertyServicesList.get(position).services_title + "(" + propertyServicesList.get(position).services_rate + ")");


        }

        @Override
        public int getItemCount() {
            return propertyServicesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemViewAddonsRequestBinding itemRowBinding;

            public ViewHolder(ItemViewAddonsRequestBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }

    public static class RatingAdapters extends RecyclerView.Adapter<RatingAdapters.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public RatingAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public RatingAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemRatingBinding layoutAddonViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_rating, parent,
                    false);
            return new RatingAdapters.ViewHolder(layoutAddonViewBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull RatingAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.tvName.setText(propertyReviewList.get(position).full_name);
            holder.itemRowBinding.tvComment.setText(propertyReviewList.get(position).ratting_comment);
            holder.itemRowBinding.tvTime.setText(propertyReviewList.get(position).created_at);
            holder.itemRowBinding.rbrating.setRating(Float.parseFloat(propertyReviewList.get(position).ratting_star));
            Glide.with(context)
                    .load(propertyReviewList.get(position).user_image)
                    .error(R.drawable.user_dummy)
                    .into(holder.itemRowBinding.ivImage);
        }

        @Override
        public int getItemCount() {
            return Math.min(propertyReviewList.size(), 2);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemRatingBinding itemRowBinding;

            public ViewHolder(ItemRatingBinding itemRowBinding) {
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

            holder.itemRowBinding.cardImage.setOnClickListener(v -> itemClickListner.onClick("", position));


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
    TextView tvCheckInDate;
    TextView tvCheckOutDate;
    ///////////////////
    public void RequestBottomSheet() {
        @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.request_bottom_sheet, null);
        final BottomSheetDialog dialog1 = new BottomSheetDialog(this);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.setContentView(view1);
        ImageView close = dialog1.findViewById(R.id.close);
        RelativeLayout layoutGuest = dialog1.findViewById(R.id.layoutGuest);
        RelativeLayout layoutCheckin = dialog1.findViewById(R.id.layoutCheckIn);
        RelativeLayout layoutCheckOut = dialog1.findViewById(R.id.layoutCheckOut);
        TextView btnReview = dialog1.findViewById(R.id.btnReview);
        TextView tvcheckintext = dialog1.findViewById(R.id.tvcheckintext);
        TextView checkouttext = dialog1.findViewById(R.id.checkouttext);
        tvCheckInDate = dialog1.findViewById(R.id.tvCheckInDate);
        tvCheckOutDate = dialog1.findViewById(R.id.tvCheckOutDate);
        EditText etFirstName = dialog1.findViewById(R.id.etFirstName);
        EditText etLastName = dialog1.findViewById(R.id.etLastName);
        EditText etEmail = dialog1.findViewById(R.id.etEmail);
        EditText etPhone = dialog1.findViewById(R.id.etPhone);
        tvGuestCount = dialog1.findViewById(R.id.tvGuestCount);
        tvGuestcounttext = dialog1.findViewById(R.id.tvGuestcounttext);
        etFirstName.setText(sessionManager.getFNAME());
        etLastName.setText(sessionManager.getLNAME());
        etEmail.setText(sessionManager.getEMAIL());
        etPhone.setText(sessionManager.getPHONE());

        layoutCheckin.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getApplicationContext(), v);
            for(int i=0;i<availableCheckInTime.size();i++){
                menu.getMenu().add(availableCheckInTime.get(i));
            }
            menu.setOnMenuItemClickListener(menuItem -> {
                Log.d("vishal", menuItem + "" + menuItem.getItemId() + "");
                tvcheckintext.setText(menuItem.getTitle().toString());
                return false;
            });
            menu.show();
        });

        layoutCheckOut.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getApplicationContext(), v);
            for(int i=0;i<availableCheckOutTime.size();i++){
                menu.getMenu().add(availableCheckOutTime.get(i));
            }
            menu.setOnMenuItemClickListener(menuItem -> {
                Log.d("vishal", menuItem + "" + menuItem.getItemId() + "");
                checkouttext.setText(menuItem.getTitle().toString());
                return false;
            });
            menu.show();
        });

        tvCheckInDate.setOnClickListener(v -> {
            clicktype = "1";
            CalenderBottomSheet();

        });
        tvCheckOutDate.setOnClickListener(v -> {
            clicktype = "2";
            CalenderBottomSheet();

        });

        RecyclerView rvAmenities = dialog1.findViewById(R.id.rvAmenities);

        layoutGuest.setOnClickListener(v -> {
            GuestBottomSheet();
        });

        btnReview.setOnClickListener(v -> {
            reviewRequestModel.setCheckindate(checkinDate);
            reviewRequestModel.setCheckoutdate(checkoutDate);
            reviewRequestModel.setCheckintime(tvcheckintext.getText().toString());
            reviewRequestModel.setCheckouttime(checkouttext.getText().toString());
            reviewRequestModel.setGuestCount(tvGuestCount.getText().toString());
            if(sessionManager.getPHONE().equals("")){
                reviewRequestModel.setPhonenumber(etPhone.getText().toString());
            }

            if (checkinDate.isEmpty()) {
                Toast.makeText(this, "Please Select Check-In Date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkoutDate.isEmpty()) {
                Toast.makeText(this, "Please Select Check-Out Date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tvcheckintext.getText().toString().equals("CheckIn Time")) {
                Toast.makeText(this, "Please Select Check-In Time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkouttext.getText().toString().equals("CheckOut Time")) {
                Toast.makeText(this, "Please Select Check-Out Time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(tvGuestCount.getText().toString())) {
                Toast.makeText(this, "Please Select Guest Count", Toast.LENGTH_SHORT).show();
                return;
            }

           if (tvGuestCount.getText().toString().equals("0")) {
                Toast.makeText(this, "Guest count cannot be 0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("property_id", getIntent().getStringExtra("property_id"));
                reqData.put("number_of_guest", tvGuestCount.getText().toString());
                reqData.put("checkin_date", checkinDate);
                reqData.put("checkin_time", tvcheckintext.getText().toString());
                reqData.put("checkout_date", checkoutDate);
                reqData.put("checkout_time", checkouttext.getText().toString());
                reqData.put("service_id", "");
                view_model.checAvailability(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }

        });

        rvAmenities.setHasFixedSize(true);
        rvAmenities.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        RequestAddonAdapters requestAddonAdapters = new RequestAddonAdapters(this, (type, pos) -> {
        });
        rvAmenities.setAdapter(requestAddonAdapters);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkinDate="";
                checkoutDate="";
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    static String clicktype = "1";


    private void handleResultReview(ApiResponse<CheckAvailabilityModel> result) {
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
                    if (result.getData().status.equals("true")) {
                        reviewRequestModel.setTotalPrice(result.getData().data.calculatePrice.totalAmount);
                        reviewRequestModel.setDailyPrice(result.getData().data.calculatePrice.dailyAmount);
                        reviewRequestModel.setWeeklyPrice(result.getData().data.calculatePrice.weeklyAmount);
                        reviewRequestModel.setHourlyPrice(result.getData().data.calculatePrice.hourlyAmount);
                        reviewRequestModel.setMonthlyPrice(result.getData().data.calculatePrice.monthlyAmount);
                        reviewRequestModel.setCleaningFee(result.getData().data.calculatePrice.cleanerFee);
                        reviewRequestModel.setExtraServiceCharge(result.getData().data.calculatePrice.servicesAmount);
                        reviewRequestModel.setServiceTax(result.getData().data.calculatePrice.taxAmount);

                        reviewRequestModel.setHourly_rate(result.getData().data.propertyPrice.hourlyRate);
                        reviewRequestModel.setWeekly_rate(result.getData().data.propertyPrice.weeklyRate);
                        reviewRequestModel.setDaily_rate(result.getData().data.propertyPrice.dailyRate);
                        reviewRequestModel.setMonthly_rate(result.getData().data.propertyPrice.monthlyRate);

                        reviewRequestModel.setStay_hours(result.getData().data.calculatePrice.stayHours);
                        reviewRequestModel.setStay_days(result.getData().data.calculatePrice.stayDays);
                        reviewRequestModel.setStay_weeks(result.getData().data.calculatePrice.stayWeeks);
                        reviewRequestModel.setStay_months(result.getData().data.calculatePrice.stayMonths);
                        reviewRequestModel.setProperty_amount(result.getData().data.calculatePrice.propertyAmount);
                        reviewRequestModel.setTax_percentage(result.getData().data.calculatePrice.taxPercentage);
                        Log.e("data", reviewRequestModel.toString());
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, RequestForBookingActivity.class).putExtra("reviewRequestModel", reviewRequestModel));
                    } else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void selectDate(TextView selectDate) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        selectDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("asdfasdf", "DSGSDFGSDFG");
    }

    public void GuestBottomSheet() {
        @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.guest_bottom_sheet, null);
        final BottomSheetDialog dialog1 = new BottomSheetDialog(this);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.setContentView(view1);
        ImageView close = dialog1.findViewById(R.id.close);
        EditText etGuest = dialog1.findViewById(R.id.etGuest);
        TextView btnProceed = dialog1.findViewById(R.id.btnProceed);
        etGuest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    if(Integer.parseInt(s.toString()) > Integer.parseInt(guestmaxCount)){
                        Toast.makeText(PropertDetailsActivity.this, "Maximum "+guestmaxCount+" guests allowed.", Toast.LENGTH_SHORT).show();
                        etGuest.setError("Guests Count");
                        btnProceed.setEnabled(false);
                    }
                   else {

                        btnProceed.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnProceed.setOnClickListener(v -> {

            if (TextUtils.isEmpty(etGuest.getText().toString())) {
                Toast.makeText(this, "Please Enter at least one", Toast.LENGTH_SHORT).show();
                return;
            }
            tvGuestCount.setText(etGuest.getText().toString());
            tvGuestcounttext.setText(" Guests");
            dialog1.dismiss();
        });

        close.setOnClickListener(v -> dialog1.dismiss());
        dialog1.show();
    }

    public void CalenderBottomSheet() {
        MyCalendarActivity addPhotoBottomDialogFragment =
                MyCalendarActivity.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                MyCalendarActivity.TAG);
    }

    public void CalenderBottomSheet1() {
        MyCalendarActivity addPhotoBottomDialogFragment =
                MyCalendarActivity.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                MyCalendarActivity.TAG);
    }


    public void openClock(TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
            mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            mcurrentTime.set(Calendar.MINUTE, selectedMinute);
            SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            textView.setText(mFormat.format(mcurrentTime.getTime()));
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    @Override
    public void onItemClick(String item) {

        if (clicktype.equals("1")) {

            if (tvCheckInDate!=null)
            {
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM");
                //tvCheckInDate.setText(item);
                checkinDate = item;
                try {
                    Date oneWayTripDate = input.parse(item);  // pa // parse input
                    Log.e("===============","======currentData======"+output.format(oneWayTripDate));
                    tvCheckInDate.setText(output.format(oneWayTripDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        } else {
            if (tvCheckOutDate!=null)
            {
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM");
                checkoutDate = item;
                try {
                    Date oneWayTripDate = input.parse(item);  // pa // parse input
                    Log.e("===============","======currentData======"+output.format(oneWayTripDate));
                    tvCheckOutDate.setText(output.format(oneWayTripDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }
     //RequestBottomSheet(checkinDate, checkoutDate);
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#1b76d3"));
        }

        @Override
        public void onClick(View widget) {


        }
    }

}