package com.hehspace_userapp.components.fragment.dashboard.explore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.fragment.dashboard.save.SavedFragment;
import com.hehspace_userapp.components.property.PropertDetailsActivity;
import com.hehspace_userapp.components.search.SearchActivity;
import com.hehspace_userapp.components.search.SearchView_Model;
import com.hehspace_userapp.databinding.FragmentExploreBinding;
import com.hehspace_userapp.databinding.LayoutFavoritesBinding;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertySearchModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseFragment;
import com.hehspace_userapp.util.Constant;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExploreFragment extends BaseFragment {

    FragmentExploreBinding fragmentExploreBinding;
    public static SearchView_Model searchView_model;
    public static List<PropertySearchModel.DataEntity> list;
    WishListAdapter wishListAdapter;
    int youPositionInTheAdapter = 0;

   String staus="";
    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        fragmentExploreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        fragmentExploreBinding.setLifecycleOwner(this);
        searchView_model = new SearchView_Model();
        return fragmentExploreBinding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        searchView_model.searchlivedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        searchView_model.livedata1.observe(this, propertyCategoryModelApiResponse -> handleResultFav(propertyCategoryModelApiResponse));

        if(Constant.CategoryIds.isEmpty() && Constant.CityName.equals("")&& Constant.CountryName.equals("")&& Constant.CheckInDate.equals("")
                && Constant.CheckInTime.equals("")&& Constant.CheckOutDate.equals("")&& Constant.CheckOutTime.equals("")&& Constant.FromRate.equals("")
                && Constant.ToRate.equals("")&& Constant.Guests.equals("")&& Constant.RateType.equals("")&&Constant.AmenitiesIds.isEmpty())
        {

        }
        else {

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                staus = bundle.getString("key");
            }
            if (staus.equals("explore")){
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
                    searchView_model.SearchProperty(hashMap);
                } else {
                    Uitility.nointernetDialog(requireActivity());
                }
            }

        }

        fragmentExploreBinding.tvSearch.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SearchActivity.class));
        });
        fragmentExploreBinding.ivClose.setOnClickListener(v -> {
            Constant.CategoryIds.clear();
            Constant.CityName="";
            Constant.CountryName="";
            Constant.CheckInDate="";
            Constant.CheckInTime="";
            Constant.CheckOutDate="";
            Constant.CheckOutTime="";
            Constant.FromRate="";
            Constant.ToRate="";
            Constant.Guests="";
            Constant.RateType="";
            Constant.AmenitiesIds.clear();
            startActivity(new Intent(requireContext(), MainActivity.class));
        });
//        fragmentExploreBinding.rvSearch.setHasFixedSize(true);
        fragmentExploreBinding.rvSearch.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void initializeOnCreateObject() {

    }

    @Override
    protected void setListeners() {


    }

    private void handleResultFav(ApiResponse<CommonModel> result) {
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
                    if (result.getData().status.equals("true")) {
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                        if (Uitility.isOnline(requireContext())) {
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("category_ids",android.text.TextUtils.join(",", Constant.CategoryIds));
                            hashMap.put("city_name",Constant.CityName);
                            hashMap.put("country_name",Constant.CountryName);
                            hashMap.put("checkin_date",Constant.CheckInDate);
                            hashMap.put("checkin_time",Constant.CheckInTime);
                            hashMap.put("checkout_date",Constant.CheckOutDate);
                            hashMap.put("checkout_time",Constant.CheckOutTime);
                            hashMap.put("from_rate",Constant.FromRate);
                            hashMap.put("to_rate",Constant.ToRate);
                            hashMap.put("allowed_guest",Constant.Guests);
                            hashMap.put("rate_type",Constant.RateType);
                            hashMap.put("anenities_ids",android.text.TextUtils.join(",", Constant.AmenitiesIds));
                            if(sessionManager.isLogin()){
                                hashMap.put("user_id",sessionManager.getUSER_ID());
                            }
                            searchView_model.SearchProperty(hashMap);

                        } else {
                            Uitility.nointernetDialog(requireActivity());
                        }
                    }
                    else    Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private void handleResult(ApiResponse<PropertySearchModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                fragmentExploreBinding.tvnotitle.setVisibility(View.GONE);
                fragmentExploreBinding.tvnotitle1.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(requireActivity());
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        list = result.getData().data;
                        if(list.size()>0){
                             wishListAdapter = new WishListAdapter(requireContext(), (type, pos) -> {
                                if(type.equals("fav")){


                                }
                                if(type.equals("view")){
                                    startActivity(new Intent(requireContext(), PropertDetailsActivity.class).
                                            putExtra("property_id",list.get(pos).propertyId).
                                            putExtra("categoryid",list.get(pos).category_id)
                                    );
                                }
                            });
                            fragmentExploreBinding.rvSearch.setVisibility(View.VISIBLE);
                            fragmentExploreBinding.tvCountProperty.setVisibility(View.VISIBLE);
                            fragmentExploreBinding.noList.setVisibility(View.GONE);
                            if(list.size() == 1){
                                fragmentExploreBinding.tvCountProperty.setText(list.size()+" Place Found");
                            }
                           else fragmentExploreBinding.tvCountProperty.setText(list.size()+" Places Found");
                            fragmentExploreBinding.rvSearch.setAdapter(wishListAdapter);

                            if(youPositionInTheAdapter!=0){
                                fragmentExploreBinding.rvSearch.getLayoutManager().scrollToPosition(youPositionInTheAdapter);
                            }

                        }
                        else {
                            fragmentExploreBinding.tvCountProperty.setVisibility(View.GONE);
                            fragmentExploreBinding.rvSearch.setVisibility(View.GONE);
                            fragmentExploreBinding.noList.setVisibility(View.VISIBLE);
                            fragmentExploreBinding.tvnotitle.setVisibility(View.GONE);
                            fragmentExploreBinding.tvnotitle1.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

        Context context;
        ItemClickListner itemClickListner;

        public WishListAdapter(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public WishListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutFavoritesBinding layoutHighlightBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.layout_favorites, parent,
                    false);
            return new WishListAdapter.ViewHolder(layoutHighlightBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.itemRowBinding.cardWishList.setVisibility(View.VISIBLE);
            holder.itemRowBinding.tvPropertyName.setText(list.get(position).propertyTitle);
            holder.itemRowBinding.tvPrice.setText("$"+list.get(position).hourlyRate+" /hour");
            holder.itemRowBinding.rbRating.setRating(Float.parseFloat(list.get(position).propertyRatting));
            Glide.with(context)
                    .load(list.get(position).propertyImageUrl)
                    .error(R.drawable.logo)
                    .into(holder.itemRowBinding.ivImage);

            if(list.get(position).wishlistStatus.equals("true")){
                Glide.with(context)
                        .load(R.drawable.ic_like)
                        .error(R.drawable.ic_like)
                        .into(holder.itemRowBinding.ivHeart);
            }
            else{
                Glide.with(context)
                        .load(R.drawable.ic_likeborder)
                        .error(R.drawable.ic_likeborder)
                        .into(holder.itemRowBinding.ivHeart);

            }
            holder.itemRowBinding.cardWishList.setOnClickListener(v -> itemClickListner.onClick("view",position));
            holder.itemRowBinding.ivHeart.setOnClickListener(v -> {
                youPositionInTheAdapter = position;
                if(sessionManager.isLogin()){
                    if (Uitility.isOnline(context)) {
                        HashMap<String, String> reqData = new HashMap<>();
                        reqData.put("property_id",list.get(position).propertyId);
                        reqData.put("user_id",sessionManager.getUSER_ID());
                        searchView_model.addToFav(reqData);
                    } else {
                        Uitility.nointernetDialog((Activity) context);
                    }
                }
                else alertGuest();
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LayoutFavoritesBinding itemRowBinding;

            public ViewHolder(LayoutFavoritesBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }





}