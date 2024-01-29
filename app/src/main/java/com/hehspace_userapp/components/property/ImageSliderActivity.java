package com.hehspace_userapp.components.property;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.sidemenu.RequestDetailsActivity;
import com.hehspace_userapp.databinding.DiscreteItemBinding;
import com.hehspace_userapp.databinding.LayoutGalleryBinding;
import com.hehspace_userapp.databinding.LayoutImageSliderBinding;
import com.hehspace_userapp.model.PropertyDetailModel;
import com.hehspace_userapp.util.ItemClickListner;
import com.hehspace_userapp.util.ZoomableImageView;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;

public class ImageSliderActivity extends AppCompatActivity {

    DiscreteScrollView discreateview;

   public static String value = "";


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        value = getIntent().getStringExtra("value");
        discreateview = findViewById(R.id.discreateview);
        discreateview.setOrientation(DSVOrientation.HORIZONTAL);
        if(value.equals("pro")){
            discreateview.setAdapter(new ImageAdapters(this));
        }
        else  discreateview.setAdapter(new ImageAdapters1(this));
        discreateview.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.6f)
                .build());

        findViewById(R.id.ivBack).setOnClickListener(v -> onBackPressed());

    }


    public class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ViewHolder> {

        Context context;

        public ImageAdapters(Context context) {
            this.context = context;
        }

        @Override
        public ImageAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DiscreteItemBinding layoutGalleryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.discrete_item, parent,
                    false);
            return new ImageAdapters.ViewHolder(layoutGalleryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ImageAdapters.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

                Glide.with(context)
                        .load(PropertDetailsActivity.imageList.get(position).propertyImageUrl)
                        .error(R.drawable.logo)
                        .into(holder.itemRowBinding.bannerImage);
        }

        @Override
        public int getItemCount() {
            return PropertDetailsActivity.imageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            DiscreteItemBinding itemRowBinding;

            public ViewHolder(DiscreteItemBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    public static class ImageAdapters1 extends RecyclerView.Adapter<ImageAdapters1.ViewHolder> {

        Context context;

        public ImageAdapters1(Context context) {
            this.context = context;
        }

        @Override
        public ImageAdapters1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DiscreteItemBinding layoutGalleryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.discrete_item, parent,
                    false);
            return new ImageAdapters1.ViewHolder(layoutGalleryBinding);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull ImageAdapters1.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

                Glide.with(context)
                        .load(RequestDetailsActivity.imageList.get(position).propertyImageUrl)
                        .error(R.drawable.logo)
                        .into(holder.itemRowBinding.bannerImage);
        }

        @Override
        public int getItemCount() {
            return RequestDetailsActivity.imageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            DiscreteItemBinding itemRowBinding;

            public ViewHolder(DiscreteItemBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

}