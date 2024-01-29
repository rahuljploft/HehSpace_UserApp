package com.hehspace_userapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hehspace_userapp.Play_Store_Update.Constants;
import com.hehspace_userapp.Play_Store_Update.InAppUpdateManager;
import com.hehspace_userapp.Play_Store_Update.InAppUpdateStatus;
import com.hehspace_userapp.components.fragment.dashboard.explore.ExploreFragment;
import com.hehspace_userapp.components.fragment.dashboard.home.HomeFragment;
import com.hehspace_userapp.components.fragment.dashboard.inbox.InboxFragment;
import com.hehspace_userapp.components.fragment.dashboard.profile.ProfileFragment;
import com.hehspace_userapp.components.fragment.dashboard.save.SavedFragment;
import com.hehspace_userapp.components.login.LoginActivity;
import com.hehspace_userapp.components.notification.NotificationActivity;
import com.hehspace_userapp.components.sidemenu.MyBookingActivity;
import com.hehspace_userapp.components.sidemenu.MyRequestActivity;
import com.hehspace_userapp.components.sidemenu.SettingsActivity;
import com.hehspace_userapp.components.splash.SplashActivity;
import com.hehspace_userapp.databinding.ActivityMainBinding;
import com.hehspace_userapp.databinding.RowDrawerBinding;
import com.hehspace_userapp.databinding.SupportDailogBinding;
import com.hehspace_userapp.model.DrawerData;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.GpsTracker;
import com.hehspace_userapp.util.utilss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends BaseBindingActivity implements InAppUpdateManager.InAppUpdateHandler,ProfileFragment.onSomeEventListener
    {
    ActivityMainBinding activityMainBinding;
    HomeFragment homeFragment;
    ExploreFragment exploreFragment;
    SavedFragment savedFragment;
    InboxFragment inboxFragment;
    ProfileFragment profileFragment;
    List<DrawerData> list;
    DrawerAdapter drawerAdapter;
    String value = "";
    public static TextView tvNotCount;
    private GoogleMap mMap;
    private Marker marker;
    String latittude = "";
    String longitude = "";
    public  static String city="",country="";
    private InAppUpdateManager inAppUpdateManager;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    @Override
    protected void setBinding() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        tvNotCount = findViewById(R.id.tvNotCount);
        if(sessionManager.isLogin()){
            activityMainBinding.username.setText(sessionManager.getNAME());
            Glide.with(this)
                    .load(sessionManager.getPROFILE_IMAGE())
                    .error(R.drawable.user_dummy)
                    .into(activityMainBinding.ivUpload);
        }
        else {
            activityMainBinding.username.setText("Guest");
            Glide.with(this)
                    .load(R.drawable.user_dummy)
                    .error(R.drawable.user_dummy)
                    .into(activityMainBinding.ivUpload);
        }
        activityMainBinding.layoutMain.backRel.setOnClickListener(v -> {
            activityMainBinding.drawerLayout.openDrawer(GravityCompat.START);
            if(sessionManager.isLogin()){
                activityMainBinding.username.setText(sessionManager.getNAME());
                Glide.with(this)
                        .load(sessionManager.getPROFILE_IMAGE())
                        .error(R.drawable.user_dummy)
                        .into(activityMainBinding.ivUpload);
            }
            else {
                activityMainBinding.username.setText("Guest");
                Glide.with(this)
                        .load(R.drawable.user_dummy)
                        .error(R.drawable.user_dummy)
                        .into(activityMainBinding.ivUpload);
            }
                }

        );
        activityMainBinding.layoutMain.ivNotification.setOnClickListener(v ->{
                    if(sessionManager.isLogin()){
                        startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                    }
                    else alertGuest();
                }
               );
        activityMainBinding.close.setOnClickListener(v -> activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START));
        homeFragment = new HomeFragment();
        exploreFragment = new ExploreFragment();
        savedFragment = new SavedFragment();
        inboxFragment = new InboxFragment();
        profileFragment = new ProfileFragment();
        profileFragment = new ProfileFragment();
        setDrawerAdapter();
        setUpBottomBar();

        permission();
        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true)
                .mode(Constants.UpdateMode.IMMEDIATE)
                .useCustomNotification(true)
                .handler(this);
    }

    @Override
    protected void setListeners() {

    }

    private void loadFragment(Fragment fragment,String Tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment,Tag);
        transaction.addToBackStack(fragment.toString());
        transaction.commit();
    }
    private void loadFragment1(Fragment fragment,String Tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("key", Tag);
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment,Tag);
        transaction.addToBackStack(fragment.toString());
        transaction.commit();
    }
    public void setUpBottomBar() {

        if(getIntent().hasExtra("value")){
            value = getIntent().getStringExtra("value");
        }

        switch (value) {
            case "fav":
                loadFragment(savedFragment,"fav");
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            case "pro":
                loadFragment(profileFragment,"pro");
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
            case "explore":
                loadFragment1(exploreFragment,"explore");
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            default:
                loadFragment(homeFragment,"home");

                activityMainBinding.layoutMain.homeHeader.setVisibility(View.VISIBLE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
        }


        activityMainBinding.layoutMain.layoutHome.setOnClickListener(v -> {

            loadFragment(homeFragment,"home");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.VISIBLE);
            activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        activityMainBinding.layoutMain.layoutBottom.layoutExplore.setOnClickListener(v -> {
            loadFragment(exploreFragment,"explore");
            activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
            activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
            activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        activityMainBinding.layoutMain.layoutBottom.layoutSaved.setOnClickListener(v -> {
            if(sessionManager.isLogin()){
                loadFragment(savedFragment,"save");
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
            else  alertGuest();

        });

        activityMainBinding.layoutMain.layoutBottom.layoutInbox.setOnClickListener(v -> {
            if(sessionManager.isLogin()){
                loadFragment(inboxFragment,"inbox");
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
            else  alertGuest();

        });

        activityMainBinding.layoutMain.layoutBottom.layoutProfile.setOnClickListener(v -> {
            if(sessionManager.isLogin()){
                loadFragment(profileFragment,"pro");
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.GONE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            } else  alertGuest();

        });
    }


    private void permission() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    101
            );

        }
        else latLong();
    }

    private void latLong() {
//        mSocket!!.connect()
//        connectSocket()
//        mSocket!!.on("joined", joined)
//        //  mSocket.on("currentlocationupdate", currentlocationupdate);
//        mSocket!!.on("sendproviderlocation", sendproviderlocation)
        GpsTracker gps = new GpsTracker(this);
        if (gps.canGetLocation()) {
            latittude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            Log.e("getLatitude()", "" + gps.getLatitude());
            Log.e("getLongitude()", "" + gps.getLongitude());
            sessionManager.setlATITUDE(latittude);
            sessionManager.setLONGTITUDE(longitude);
            getAddress(gps.getLatitude(), gps.getLongitude());

//            LatLng coordinate = new LatLng(Double.parseDouble(latittude), Double.parseDouble(longitude));

//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    coordinate, 17));

            //  sendLocation(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()))
        } else {
            gps.showSettingsAlert();
        }
    }

    public String getAddress(Double latitude,  Double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.e("qwert",addresses.get(0).toString());
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
           String  address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
           String state = addresses.get(0).getAdminArea();
          country = addresses.get(0).getCountryName();
           String postalCode = addresses.get(0).getPostalCode();
           String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            sessionManager.setCity(city);
            sessionManager.setCountry(country);
//            Constant.PropertyAddress = address;
        } catch (Exception ignored) {
            getAddress(latitude, longitude);
        }
        return "";
    }



    private void setDrawerAdapter() {
        list = new ArrayList<>();
        String[] mTestArray = getResources().getStringArray(R.array.sidemenu);
        for (int i = 0; i < mTestArray.length; i++) {
            list.add(new DrawerData(i, mTestArray[i]));
        }
        drawerAdapter = new DrawerAdapter(list);
        activityMainBinding.navigationList.setAdapter(drawerAdapter);
    }

        @Override
        public void someEvent(String name, String photourl) {
            activityMainBinding.username.setText(name);
            Glide.with(this)
                    .load(photourl)
                    .error(R.drawable.user_dummy)
                    .into(activityMainBinding.ivUpload);
        }


        public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
        List<DrawerData> list;

        public DrawerAdapter(List<DrawerData> list) {
            this.list = list;
        }

        @Override
        public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RowDrawerBinding rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_drawer, parent,
                    false);
            return new DrawerAdapter.ViewHolder(rowDrawerBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull DrawerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            DrawerData drawerData = list.get(position);
            holder.itemRowBinding.txtDrawerName.setText(drawerData.name);

                holder.itemRowBinding.tvStatus.setVisibility(View.GONE);

               if(position == 7){
                   if(sessionManager.isLogin()){
                       holder.itemRowBinding.txtDrawerName.setText("Logout");
                   }
                   else  holder.itemRowBinding.txtDrawerName.setText("Login");
               }

            holder.itemRowBinding.llMain.setOnClickListener(v -> {
                switch (position) {
                    case 0:
                        if(sessionManager.isLogin()){
                            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                            startActivity(new Intent(MainActivity.this, MainActivity.class).putExtra("value","pro"));
                        }
                        else {
                            alertGuest();
                        }

                        break;

                    case 1:
                        if(sessionManager.isLogin()){
                            startActivity(new Intent(MainActivity.this, MyRequestActivity.class));
                            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        else {
                            alertGuest();
                        }
                        break;

                    case 2:
                        if(sessionManager.isLogin()){
                            startActivity(new Intent(MainActivity.this, MyBookingActivity.class));
                            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        else {
                            alertGuest();
                        }
                        break;

                    case 3:
                        if(sessionManager.isLogin()){
                            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                            startActivity(new Intent(MainActivity.this, MainActivity.class).putExtra("value","fav"));
                        }
                        else {
                            alertGuest();
                        }
                        break;

                    case 4:
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            break;

                    case 5:
                        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportDialog();
                        break;

                    case 6:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.hehspace_host")));
                        break;
                    case 7:
                        if(sessionManager.isLogin()){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("Logout")
                                    .setMessage("Are you sure, you want to Logout?")
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            dialoginterface.cancel();
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
                                            sessionManager.logout();
                                            LoginManager.getInstance().logOut();
                                            startActivity(new Intent(MainActivity.this, SplashActivity.class));
                                        }
                                    }).show();
                            break;
                        }
                        else {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }

                }

            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RowDrawerBinding itemRowBinding;

            public ViewHolder(RowDrawerBinding itemRowBinding) {
                super(itemRowBinding.getRoot());
                this.itemRowBinding = itemRowBinding;
            }

        }

    }

    private void getSupportDialog() {
        Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        SupportDailogBinding dailogHirePhotographerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mActivity),
                R.layout.support_dailog,
                null,
                false
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialogBinding.tvTitle.setText(resources.getString(R.string.internet_issue))
//        dialogBinding.tvMessage.setText(resources.getString(R.string.please_check_your_internet))
        dailogHirePhotographerBinding.ivClose.setOnClickListener(v ->
                dialog.dismiss());
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        dailogHirePhotographerBinding.layoutEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { dailogHirePhotographerBinding.tvEmail.getText().toString() });
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
            startActivity(Intent.createChooser(intent, ""));
            dialog.dismiss();
        });
        dailogHirePhotographerBinding.layoutCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:7976232985"));
            startActivity(intent);
            dialog.dismiss();
        });

        dailogHirePhotographerBinding.layoutWeb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.hehspace.com"));
            startActivity(intent);
            dialog.dismiss();
        });
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(dailogHirePhotographerBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if(activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            Log.d("qwerty", getVisibleFragment().getId() + "__");
            if(getVisibleFragment().getTag().equals("home")){

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Exit App")
                        .setMessage("Are you sure, you want to exit App ?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                finishAffinity();
                            }
                        }).show();
            }else{
                activityMainBinding.layoutMain.homeHeader.setVisibility(View.VISIBLE);
                activityMainBinding.layoutMain.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                activityMainBinding.layoutMain.layoutBottom.ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvExplore.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivSaved.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvSved.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivMessage.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                activityMainBinding.layoutMain.layoutBottom.ivProfile.setColorFilter(ContextCompat.getColor(this, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                activityMainBinding.layoutMain.layoutBottom.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
                loadFragment(homeFragment,"home");
            }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == utilss.PERMISSION_REUEST_CODE) {
            HashMap<String, Integer> permissionresult = new HashMap<>();
            int deniedcount = 0;
            //gather permission grant result
            for (int i = 0; i < grantResults.length; i++) {
                //add only permission which denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionresult.put(permissions[i], grantResults[i]);
                    deniedcount++;
                }
            }
            //if all permission are granted
            if (deniedcount == 0) {
                // Proced    ahead in the app
                // isStoragePermissionGranted();
            }
            //atleast one or all permissions are denied
            else {
                for (Map.Entry<String, Integer> entry : permissionresult.entrySet()) {
                    String permName = entry.getKey();
                    int permresult = entry.getValue();
                    //permission dnied when first time "never ask again" is not  check
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        //show dialog
                        // yes and another for dismiss
                        utilss.checkAndRequestPermissions(MainActivity.this);
                    }
                    //permission is dnied (and never ask again is check)
                    else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        }
        if (requestCode == 101) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    latLong();
                    startActivity(new Intent(this,MainActivity.class));
                }
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
//                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }
    }


    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onInAppUpdateError(int code, Throwable error) {
        Log.d("updatestatus", "code: " + code, error);
    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        if (status.isUpdateAvailable()) {
            Log.d("updatestatus", "dddddcode: " + status.isUpdateAvailable());
            showdialog();
        } else if (status.isDownloaded()) {
            Log.d("updatestatus", "dddddcode: " + status.isDownloaded());
            inAppUpdateManager.snackBarMessage("Complete your HehSpace app Update").completeUpdate();
        }else if (status.isFailed()) {
            showdialog();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                inAppUpdateManager.checkForAppUpdate();
                Log.d("updatestatus", "Update flow failed! Result code: " + resultCode);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void showdialog() {
        ViewGroup viewGroup = this.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.update_dialog, viewGroup, false);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        LinearLayout Update_Now = alertDialog.findViewById(R.id.Update_Now);
        LinearLayout Close_App = alertDialog.findViewById(R.id.Close_App);
        Update_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inAppUpdateManager.checkForAppUpdate();
                alertDialog.dismiss();
            }
        });
        Close_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.exit(0);
                alertDialog.dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Apply the newly created layout parameters to the alert dialog window
        alertDialog.getWindow().setAttributes(layoutParams);

    }

}