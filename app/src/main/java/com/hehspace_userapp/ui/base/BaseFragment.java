package com.hehspace_userapp.ui.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.hehspace_userapp.components.login.LoginActivity;
import com.hehspace_userapp.util.SessionManager;


public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    public AppCompatActivity mActivity = null;
    protected SessionManager sessionManager = null;


    protected abstract ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void createActivityObject();

    protected abstract void initializeObject();

    protected abstract void initializeOnCreateObject();

    protected abstract void setListeners();

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity == null) throw new NullPointerException("must create activity object");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessionManager = SessionManager.getInstance(getActivity());
        ViewDataBinding binding = setBinding(inflater, container);
        View view = binding.getRoot();
        createActivityObject();
        initializeObject();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeOnCreateObject();
    }

    @Override
    public void onClick(View view) {

    }

    public void alertGuest() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setTitle("HehSpace")
                .setMessage("You are not logged in. Please log in to continue")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                        startActivity(new Intent(mActivity, LoginActivity.class));
                    }
                }).show();
    }

}