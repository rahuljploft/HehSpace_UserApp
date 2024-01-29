package com.hehspace_userapp.components.fragment.dashboard.save;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.fragment.dashboard.home.HomeFragment;
import com.hehspace_userapp.components.fragment.dashboard.home.HomeView_Model;
import com.hehspace_userapp.components.property.PropertDetailsActivity;
import com.hehspace_userapp.databinding.FragmentSavedBinding;
import com.hehspace_userapp.databinding.LayoutFavoritesBinding;
import com.hehspace_userapp.databinding.LayoutHighlightBinding;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.WishListModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseFragment;
import com.hehspace_userapp.util.Constant;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SavedFragment extends BaseFragment {

    FragmentSavedBinding fragmentSavedBinding;
    public static SavedView_Model view_model;
    public static List<WishListModel.DataEntity> list = new ArrayList<>();
    WishListAdapter wishListAdapter;
    public static String userid = "";
    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        fragmentSavedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved, container, false);
        fragmentSavedBinding.setLifecycleOwner(this);
        view_model = new SavedView_Model();
        return fragmentSavedBinding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        view_model.liveData.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        view_model.livedata1.observe(this, propertyCategoryModelApiResponse -> handleResultFav(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(requireContext())) {
            view_model.getWishList();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }
        userid = sessionManager.getUSER_ID();

        fragmentSavedBinding.srlayout.setOnRefreshListener(() -> {
            if (Uitility.isOnline(requireContext())) {
                view_model.getWishList();
            } else {
                Uitility.nointernetDialog(requireActivity());
            }
        });
        list.clear();
        fragmentSavedBinding.rvWishlist.setHasFixedSize(true);
        fragmentSavedBinding.rvWishlist.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        wishListAdapter = new WishListAdapter(requireContext(), (type, pos) -> {
            startActivity(new Intent(requireContext(), PropertDetailsActivity.class)
                    .putExtra("property_id",list.get(pos).propertyId)
                    .putExtra("categoryid",list.get(pos).category_id)
            );
        });
        fragmentSavedBinding.rvWishlist.setAdapter(wishListAdapter);
    }

    @Override
    protected void initializeOnCreateObject() {

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
                            view_model.getWishList();
                        } else {
                            Uitility.nointernetDialog(requireActivity());
                        }
                    }
                    else    Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    @Override
    protected void setListeners() {
        fragmentSavedBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            startActivity(new Intent(requireContext(),MainActivity.class));
        }
    }

    private void handleResult(ApiResponse<WishListModel> result) {
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
                fragmentSavedBinding.srlayout.setRefreshing(false);
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        list.clear();
                        list = result.getData().data;
                        if(list.size()>0){
                            wishListAdapter.notifyDataSetChanged();
                            fragmentSavedBinding.rvWishlist.setVisibility(View.VISIBLE);
                            fragmentSavedBinding.cardNoBooking.setVisibility(View.GONE);
                        }
                        else {
                            fragmentSavedBinding.rvWishlist.setVisibility(View.GONE);
                            fragmentSavedBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        }

                    }
                    else{
                        fragmentSavedBinding.rvWishlist.setVisibility(View.GONE);
                        fragmentSavedBinding.cardNoBooking.setVisibility(View.VISIBLE);
                        Toast.makeText(requireContext(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public static class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

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
            holder.itemRowBinding.tvPrice.setText(list.get(position).hourlyRate+" /hour");
            holder.itemRowBinding.rbRating.setRating(Float.parseFloat(list.get(position).propertyRatting));
            Glide.with(context)
                    .load(list.get(position).propertyImageUrl)
                    .error(R.drawable.logo)
                    .into(holder.itemRowBinding.ivImage);
            holder.itemRowBinding.cardWishList.setOnClickListener(v -> itemClickListner.onClick("",position));

            holder.itemRowBinding.ivHeart.setOnClickListener(v -> {
                if (Uitility.isOnline(context)) {
                    HashMap<String, String> reqData = new HashMap<>();
                    reqData.put("property_id",list.get(position).propertyId);
                    reqData.put("user_id",userid);
                    view_model.addToFav(reqData);
                } else {
                    Uitility.nointernetDialog((Activity) context);
                }
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

    @Override
    public void onResume() {
        super.onResume();
        if (Uitility.isOnline(requireContext())) {
            view_model.getWishList();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }

    }
}