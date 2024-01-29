package com.hehspace_userapp.components.sidemenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.databinding.ActivityAddReviewBinding;
import com.hehspace_userapp.model.BookingDetailsModel;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.HashMap;

public class AddReviewActivity extends BaseBindingActivity {

    ActivityAddReviewBinding activityAddReviewBinding;
    AddReviewView_Model view_model;
    String star = "";
    @Override
    protected void setBinding() {
        activityAddReviewBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_review);
        view_model = new AddReviewView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
    mActivity = this;
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));
    }

    @Override
    protected void setListeners() {
        activityAddReviewBinding.ivBack.setOnClickListener(this);
        activityAddReviewBinding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.ivBack){
            onBackPressed();
        }

        if(view.getId() == R.id.btnSubmit){
          star = String.valueOf(activityAddReviewBinding.rbrating.getRating());
          if(TextUtils.isEmpty(activityAddReviewBinding.etReview.getText().toString())){
              Toast.makeText(this, "Please Enter Review", Toast.LENGTH_SHORT).show();
              return;
          }
            if (Uitility.isOnline(this)) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("booking_id",getIntent().getStringExtra("booking_id"));
                hashMap.put("ratting_star",star);
                hashMap.put("ratting_comment",activityAddReviewBinding.etReview.getText().toString());
                hashMap.put("property_id",getIntent().getStringExtra("property_id"));
                view_model.addFeedback(hashMap);
            } else {
                Uitility.nointernetDialog(this);
            }
        }

    }

    private void handleResult(ApiResponse<CommonModel> result) {
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
                    else{

                    }
                }
                break;
        }
    }

}