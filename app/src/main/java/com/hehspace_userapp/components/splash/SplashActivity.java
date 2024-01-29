package com.hehspace_userapp.components.splash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.login.LoginActivity;
import com.hehspace_userapp.databinding.ActivitySplashBinding;
import com.hehspace_userapp.ui.base.BaseBindingActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseBindingActivity {
    ActivitySplashBinding activitySplashBinding;
    String token = "";

    @Override
    protected void setBinding() {
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                        Log.d("token",token);
                        if (!token.isEmpty())
                            sessionManager.setDEVICE_TOKEN(token);
                        // Log and toast
                        //    Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
        printHashKey(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            if(sessionManager.isLogin()){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        },2000);

    }


    @Override
    protected void setListeners() {

    }

    public void printHashKey(Context context) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hehspace_userapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

}