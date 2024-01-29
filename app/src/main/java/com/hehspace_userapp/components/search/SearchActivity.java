package com.hehspace_userapp.components.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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

import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.fragment.dashboard.explore.ExploreFragment;
import com.hehspace_userapp.components.fragment.dashboard.home.HomeFragment;
import com.hehspace_userapp.databinding.ActivitySearchBinding;
import com.hehspace_userapp.databinding.ItemCategoryBinding;
import com.hehspace_userapp.databinding.ItemLocationBinding;
import com.hehspace_userapp.model.CityModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertyListModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.Constant;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends BaseBindingActivity {

    ActivitySearchBinding activitySearchBinding;

    SearchView_Model searchView_model;
    SearchAdapters searchAdapters;
    public  static  List<CityModel.DataEntity> cityList = new ArrayList();

    @Override
    protected void setBinding() {
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        searchView_model = new SearchView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        Constant.CategoryIds.clear();
        Constant.CountryName="";
        searchView_model.propertycategorylivedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        searchView_model.livedata.observe(this, propertyCategoryModelApiResponse -> CityhandleResult(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(this)) {
            searchView_model.propertyCategory();
        } else {
            Uitility.nointernetDialog(this);
        }

        activitySearchBinding.rvCategories.setHasFixedSize(true);
        activitySearchBinding.rvCategories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        activitySearchBinding.rvLocation.setHasFixedSize(true);
        activitySearchBinding.rvLocation.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        activitySearchBinding.ivBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void setListeners() {
        activitySearchBinding.ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivSearch){
            if(Constant.CategoryIds.size()>0){
                startActivity(new Intent(this, MainActivity.class).putExtra("value","explore"));

            }
            else  Toast.makeText(this, "Please select Category", Toast.LENGTH_SHORT).show();

        }
    }

    private void handleResult(ApiResponse<PropertyCategoryModel> result) {
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
                        List<PropertyCategoryModel.DataEntity> catList = new ArrayList<>();
                        PropertyCategoryModel.DataEntity propertyData = new PropertyCategoryModel.DataEntity();
                        catList = result.getData().data;
                        propertyData.categoryId = catList.get(0).categoryId;
                        propertyData.categoryTitle = catList.get(0).categoryTitle;
                        propertyData.isselected = true;
                        catList.add(0, propertyData);
                        List<PropertyCategoryModel.DataEntity> finalCatList = catList;

                        AddonAdapters addonAdapters = new AddonAdapters(this,finalCatList, (type, pos) -> {
                            Constant.CategoryIds.clear();

                        });
                        activitySearchBinding.rvCategories.setAdapter(addonAdapters);
                        if (Uitility.isOnline(this)) {
                            searchView_model.getCity();
                        } else {
                            Uitility.nointernetDialog(this);
                        }
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void CityhandleResult(ApiResponse<CityModel> result) {
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
                        cityList = result.getData().data;
                         searchAdapters = new SearchAdapters(this, (type, pos) -> {
                             activitySearchBinding.tvLocation.setText(cityList.get(pos).cityName);
                             Constant.CityName = cityList.get(pos).cityName;
                             Constant.CountryName = cityList.get(pos).countryName;
                        });
                        activitySearchBinding.rvLocation.setAdapter(searchAdapters);
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    public static class AddonAdapters extends RecyclerView.Adapter<AddonAdapters.ViewHolder> {

        Context context;
        List<PropertyCategoryModel.DataEntity> list;
        ItemClickListner itemClickListner;

        public AddonAdapters(Context context,  List<PropertyCategoryModel.DataEntity> list,ItemClickListner itemClickListner) {
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
                Constant.CategoryIds.add(list.get(position).categoryId);
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
                itemClickListner.onClick("",position);
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

    public static class SearchAdapters extends RecyclerView.Adapter<SearchAdapters.ViewHolder> {
        Context context;
        ItemClickListner itemClickListner;

        public SearchAdapters(Context context, ItemClickListner itemClickListner) {
            this.context = context;
            this.itemClickListner = itemClickListner;
        }

        @Override
        public SearchAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemLocationBinding itemLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_location, parent,
                    false);
            return new SearchAdapters.ViewHolder(itemLocationBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull SearchAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemRowBinding.tvLocation.setText(cityList.get(position).cityName);

            holder.itemRowBinding.layoutLocation.setOnClickListener(v -> itemClickListner.onClick("",position));


        }

        @Override
        public int getItemCount() {
          /*  if (uriList.size() > 0) {
                return uriList.size();
            } else {
                return 6;
            }*/
            return cityList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemLocationBinding itemRowBinding;

            public ViewHolder(ItemLocationBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

}