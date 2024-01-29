package com.hehspace_userapp.components.fragment.dashboard.profile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.databinding.FragmentProfileBinding;
import com.hehspace_userapp.model.ProfileModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseFragment;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;
import com.hehspace_userapp.util.utilss;

import java.io.File;
import java.util.List;

public class ProfileFragment extends BaseFragment implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate {
    public interface onSomeEventListener {
        public void someEvent(String name,String photourl);
    }
    onSomeEventListener someEventListener;
    FragmentProfileBinding fragmentProfileBinding;
    ProfileView_Model view_model;
    String imagepath = "";
    String documentpath = "";
    String genderid = "";
    File image;
    File documentimage;
    String imageType="";

    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        fragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        fragmentProfileBinding.setLifecycleOwner(this);
        view_model = new ProfileView_Model();
        return fragmentProfileBinding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        view_model.livedata.observe(requireActivity(), modelApiResponse -> handleResult(modelApiResponse));
        view_model.updatelivedata.observe(requireActivity(), modelApiResponse -> UpdatehandleResult(modelApiResponse));
        if (Uitility.isOnline(requireContext())) {
            view_model.getProfileDetails();
        } else {
            Uitility.nointernetDialog(requireActivity());
        }
        if (fragmentProfileBinding.tvEdit.getText().toString().equals("UPDATE")) {
            fragmentProfileBinding.tvlayout.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "You can't change Registered Email", Toast.LENGTH_SHORT).show();
                fragmentProfileBinding.etEmail.setEnabled(false);
            });
        }
    }

    @Override
    protected void initializeOnCreateObject() {

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }
    @Override
    protected void setListeners() {
        fragmentProfileBinding.ivBack.setOnClickListener(this);
        fragmentProfileBinding.tvEdit.setOnClickListener(this);
        fragmentProfileBinding.ivProfile.setOnClickListener(this);
        fragmentProfileBinding.ivUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view.getId() == R.id.ivBack) {
            startActivity(new Intent(requireContext(), MainActivity.class));
        }
        if (view.getId() == R.id.ivProfile) {
            imageType = "profile";
            if (fragmentProfileBinding.tvEdit.getText().toString().equals("UPDATE")) {
                if (utilss.checkAndRequestPermissions((Activity) requireContext())) {
                    BSImagePicker multiSelectionPicker = new BSImagePicker.Builder("com.hehspace_userapp.provider")
                            .setMinimumMultiSelectCount(1) //Default: 1.
                            .setMaximumMultiSelectCount(1)//Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                            .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                            .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                            .setMultiSelectDoneTextColor(R.color.colorPrimary) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                            .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                            .disableOverSelectionMessage() //You can also decide not to show this over select message.
                            .build();
                    multiSelectionPicker.show(getChildFragmentManager(), "picker");
                }

            }
        }
        if (view.getId() == R.id.ivUpload) {
            imageType = "document";
            if (utilss.checkAndRequestPermissions((Activity) requireContext())) {
                BSImagePicker multiSelectionPicker = new BSImagePicker.Builder("com.hehspace_userapp.provider")
                        .setMinimumMultiSelectCount(1) //Default: 1.
                        .setMaximumMultiSelectCount(1)//Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                        .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                        .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                        .setMultiSelectDoneTextColor(R.color.colorPrimary) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                        .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                        .disableOverSelectionMessage() //You can also decide not to show this over select message.
                        .build();
                multiSelectionPicker.show(getChildFragmentManager(), "picker");
            }
        }
        if (view.getId() == R.id.tvEdit) {
            if (fragmentProfileBinding.tvEdit.getText().toString().equals("EDIT")) {
                fragmentProfileBinding.tvEdit.setText("UPDATE");
                fragmentProfileBinding.iveditImg.setVisibility(View.VISIBLE);
                fragmentProfileBinding.ivUpload.setVisibility(View.VISIBLE);
                fragmentProfileBinding.etFName.setEnabled(true);
                fragmentProfileBinding.etLName.setEnabled(true);
                fragmentProfileBinding.etEmail.setEnabled(false);
                fragmentProfileBinding.etPhone.setEnabled(true);
            } else {
                if (Uitility.isOnline(requireContext())) {
                    view_model.updateProfileDetails(fragmentProfileBinding.etFName.getText().toString(),
                            fragmentProfileBinding.etLName.getText().toString(),
                            fragmentProfileBinding.etEmail.getText().toString(), fragmentProfileBinding.etPhone.getText().toString(),
                            genderid, image,documentimage);
                } else {
                    Uitility.nointernetDialog(requireActivity());
                }
            }

        }
    }

    private void handleResult(ApiResponse<ProfileModel> result) {
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
                        fragmentProfileBinding.etFName.setText(result.getData().data.firstName);
                        fragmentProfileBinding.tvName.setText(result.getData().data.firstName + " " + result.getData().data.lastName);
                        fragmentProfileBinding.etLName.setText(result.getData().data.lastName);
                        fragmentProfileBinding.etEmail.setText(result.getData().data.emailAddress);
                        fragmentProfileBinding.etPhone.setText(result.getData().data.mobileNumber);
                        sessionManager.setNAME(result.getData().data.firstName + " " + result.getData().data.lastName);
                        sessionManager.setPROFILE_IMAGE(result.getData().data.idProofUrl);
                        genderid = result.getData().data.genderId;
                        Glide.with(this)
                                .load(result.getData().data.userPicName)
                                .error(R.drawable.logo)
                                .into(fragmentProfileBinding.ivProfile);
                        Glide.with(this)
                                .load(result.getData().data.idProofUrl)
                                .error(R.drawable.logo)
                                .into(fragmentProfileBinding.ivDocument);
                        someEventListener.someEvent(result.getData().data.firstName + " " + result.getData().data.lastName,result.getData().data.userPicName);
                    } else {
                        Toast.makeText(requireActivity(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void UpdatehandleResult(ApiResponse<ProfileModel> result) {
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
                        fragmentProfileBinding.etFName.setText(result.getData().data.firstName);
                        fragmentProfileBinding.tvName.setText(result.getData().data.firstName + " " + result.getData().data.lastName);
                        fragmentProfileBinding.etLName.setText(result.getData().data.lastName);
                        fragmentProfileBinding.etEmail.setText(result.getData().data.emailAddress);
                        fragmentProfileBinding.etPhone.setText(result.getData().data.mobileNumber);
                        genderid = result.getData().data.genderId;
                        Glide.with(this)
                                .load(result.getData().data.userPicName)
                                .error(R.drawable.logo)
                                .into(fragmentProfileBinding.ivProfile);
                        fragmentProfileBinding.tvEdit.setText("EDIT");
                        fragmentProfileBinding.iveditImg.setVisibility(View.GONE);
                        fragmentProfileBinding.ivUpload.setVisibility(View.GONE);
                        fragmentProfileBinding.etFName.setEnabled(false);
                        fragmentProfileBinding.etLName.setEnabled(false);
                        fragmentProfileBinding.etEmail.setEnabled(false);
                        fragmentProfileBinding.etPhone.setEnabled(false);
                        sessionManager.setNAME(result.getData().data.firstName + " " + result.getData().data.lastName);
                        sessionManager.setFNAME(result.getData().data.firstName.substring(0, 1).toUpperCase()+result.getData().data.firstName.substring(1).toLowerCase());
                        sessionManager.setLNAME(result.getData().data.lastName.substring(0, 1).toUpperCase()+result.getData().data.lastName.substring(1).toLowerCase());
                        sessionManager.setPROFILE_IMAGE(result.getData().data.userPicName);
                        sessionManager.setPHONE(result.getData().data.mobileNumber);
                        Toast.makeText(requireActivity(), result.getData().message, Toast.LENGTH_SHORT).show();
                        someEventListener.someEvent(result.getData().data.firstName + " " + result.getData().data.lastName,result.getData().data.userPicName);

                    } else {
                        Toast.makeText(requireActivity(), result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }



    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = requireActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        if(imageType.equals("profile")){

            imagepath = getRealPathFromURI(uri+ "");
            image = new File(imagepath);
            fragmentProfileBinding.ivProfile.setImageURI(uri);


        }else {

            documentpath = getRealPathFromURI(uri+ "");
            documentimage= new File(documentpath);
            fragmentProfileBinding.ivDocument.setImageURI(uri);


        }
    }
}