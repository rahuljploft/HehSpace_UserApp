package com.hehspace_userapp.components.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.LoginModel;
import com.hehspace_userapp.model.VerifyOtpModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import java.io.File;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoginView_Model extends ViewModel {
    MutableLiveData<ApiResponse<LoginModel>> loginlivedata = new MutableLiveData<>();
    MutableLiveData<ApiResponse<LoginModel>> loginlivedata1 = new MutableLiveData<>();
    MutableLiveData<ApiResponse<LoginModel>> loginlivedata2 = new MutableLiveData<>();
    ApiResponse<LoginModel> apiResponse = null;
    ApiResponse<LoginModel> apiResponse1 = null;
    ApiResponse<LoginModel> apiResponse2 = null;
    public MutableLiveData<ApiResponse<VerifyOtpModel>> verifylivedata = new MutableLiveData<>();
    ApiResponse<VerifyOtpModel> verifyapiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        verifyapiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse2 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }
    public  void login(HashMap<String, String> reqData) {
        subscription = restApi.loginApi(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        loginlivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<LoginModel>() {
                    @Override
                    public void accept(LoginModel loginData) throws Exception {
                        loginlivedata.postValue(apiResponse.success(loginData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loginlivedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public  void socialLogin(HashMap<String, String> reqData) {
        subscription = restApi.socialloginApi(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        loginlivedata1.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<LoginModel>() {
                    @Override
                    public void accept(LoginModel loginData) throws Exception {
                        loginlivedata1.postValue(apiResponse1.success(loginData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loginlivedata1.postValue(apiResponse1.error(throwable));
                    }
                });
    }

    public  void socialLoginEmail(HashMap<String, String> reqData) {
        subscription = restApi.socialloginApi(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        loginlivedata2.postValue(apiResponse2.loading());
                    }
                })
                .subscribe(new Consumer<LoginModel>() {
                    @Override
                    public void accept(LoginModel loginData) throws Exception {
                        loginlivedata2.postValue(apiResponse2.success(loginData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loginlivedata2.postValue(apiResponse2.error(throwable));
                    }
                });
    }


    public  void verifyOTP(HashMap<String, String> reqData) {
        subscription = restApi.verifyOTP(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        verifylivedata.postValue(verifyapiResponse.loading());
                    }
                })
                .subscribe(new Consumer<VerifyOtpModel>() {
                    @Override
                    public void accept(VerifyOtpModel verifyOtpModel) throws Exception {
                        verifylivedata.postValue(verifyapiResponse.success(verifyOtpModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        verifylivedata.postValue(verifyapiResponse.error(throwable));
                    }
                });
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
