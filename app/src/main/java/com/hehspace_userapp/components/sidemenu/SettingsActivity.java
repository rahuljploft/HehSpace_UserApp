package com.hehspace_userapp.components.sidemenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.BuildConfig;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.forgotpassword.SetPasswordActivity;
import com.hehspace_userapp.databinding.ActivitySettingsBinding;
import com.hehspace_userapp.model.PageModel;
import com.hehspace_userapp.model.RequestDetailsModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.Arrays;

public class SettingsActivity extends BaseBindingActivity {

    ActivitySettingsBinding activitySettingsBinding;
    SettingView_Model view_model;
    String privacyurl = "";
    @Override
    protected void setBinding() {
        activitySettingsBinding = DataBindingUtil.setContentView(this,R.layout.activity_settings);
        view_model = new SettingView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        if (BuildConfig.BUILD_TYPE.equals("debug")){
            activitySettingsBinding.versioncode.setText(BuildConfig.VERSION_NAME+""+"(Debug)");
        }else {
            activitySettingsBinding.versioncode.setText(BuildConfig.VERSION_NAME+""+"(Release)");
        }

        view_model.livedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        if (Uitility.isOnline(this)) {
            view_model.getPages();
        } else {
            Uitility.nointernetDialog(this);
        }
        findViewById(R.id.ivBack).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.layoutChangePassword).setOnClickListener(v ->
        {
            if(sessionManager.isLogin()){
                startActivity(new Intent(this, SetPasswordActivity.class));
            }
            else {
                alertGuest();
            }

        });

        findViewById(R.id.layoutPP).setOnClickListener(v ->
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyurl));
            startActivity(browserIntent);

        });
    }

    @Override
    protected void setListeners() {

    }

    private void handleResult(ApiResponse<PageModel> result) {
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
                        privacyurl = result.getData().data.privacy_policy;
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}