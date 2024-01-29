package com.hehspace_userapp.components.property;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.R;
import com.hehspace_userapp.databinding.ActivityRatingReviewBinding;
import com.hehspace_userapp.databinding.ItemRatingBinding;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ItemClickListner;

public class RatingReviewActivity extends BaseBindingActivity {

    ActivityRatingReviewBinding activityRatingReviewBinding;

    @Override
    protected void setBinding() {
    activityRatingReviewBinding = DataBindingUtil.setContentView(this,R.layout.activity_rating_review);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        activityRatingReviewBinding.rvRatings.setHasFixedSize(true);
        activityRatingReviewBinding.rvRatings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if(PropertDetailsActivity.propertyReviewList.size()>0){
            RatingAdapters ratingAdapters = new RatingAdapters(this, (type, pos) -> {

            });
            activityRatingReviewBinding.rvRatings.setAdapter(ratingAdapters);
            activityRatingReviewBinding.cardNoReview.setVisibility(View.GONE);
            activityRatingReviewBinding.rvRatings.setVisibility(View.VISIBLE);
        }
        else {
            activityRatingReviewBinding.rvRatings.setVisibility(View.GONE);
            activityRatingReviewBinding.cardNoReview.setVisibility(View.VISIBLE);
        }

        activityRatingReviewBinding.tvTotalRating.setText(PropertDetailsActivity.rate);
        activityRatingReviewBinding.tvTotalReviews.setText(PropertDetailsActivity.review);
        activityRatingReviewBinding.tvFiveStar.setText(PropertDetailsActivity.fivestart);
    }

    @Override
    protected void setListeners() {
        activityRatingReviewBinding.ivBack.setOnClickListener(this);
        activityRatingReviewBinding.ivFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }
        if(view.getId() == R.id.ivFilter){
            PopupMenu popup = new PopupMenu(RatingReviewActivity.this,  activityRatingReviewBinding.ivFilter);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
            popup.show(); //showing popup menu
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
            holder.itemRowBinding.tvName.setText(PropertDetailsActivity.propertyReviewList.get(position).full_name);
            holder.itemRowBinding.tvComment.setText(PropertDetailsActivity.propertyReviewList.get(position).ratting_comment);
            holder.itemRowBinding.tvTime.setText(PropertDetailsActivity.propertyReviewList.get(position).created_at);
            holder.itemRowBinding.rbrating.setRating(Float.parseFloat(PropertDetailsActivity.propertyReviewList.get(position).ratting_star));
            Glide.with(context)
                    .load(PropertDetailsActivity.propertyReviewList.get(position).user_image)
                    .error(R.drawable.user_dummy)
                    .into(holder.itemRowBinding.ivImage);
        }

        @Override
        public int getItemCount() {
            return PropertDetailsActivity.propertyReviewList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemRatingBinding itemRowBinding;
            public ViewHolder(ItemRatingBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }
        }
    }

}