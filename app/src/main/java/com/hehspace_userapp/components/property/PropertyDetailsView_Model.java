package com.hehspace_userapp.components.property;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.CheckAvailabilityModel;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertyDetailModel;
import com.hehspace_userapp.model.PropertyListModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PropertyDetailsView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<PropertyDetailModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> livedata1 = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CheckAvailabilityModel>> livedata2 = new MutableLiveData<>();
    ApiResponse<PropertyDetailModel> apiResponse = null;
    ApiResponse<CommonModel> apiResponse1 = null;
    ApiResponse<CheckAvailabilityModel> apiResponse2 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse2 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public void getPropertyDetails(HashMap<String, String> reqData) {
        subscription = restApi.propertyDetails(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<PropertyDetailModel>() {
                    @Override
                    public void accept(PropertyDetailModel propertyDetailModel) throws Exception {
                        livedata.postValue(apiResponse.success(propertyDetailModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }
    public void addToFav(HashMap<String, String> reqData) {
        subscription = restApi.AddToFav(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata1.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel) throws Exception {
                        livedata1.postValue(apiResponse1.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata1.postValue(apiResponse1.error(throwable));
                    }
                });
    }
    public void checAvailability(HashMap<String, String> reqData) {
        subscription = restApi.checAvailability(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata2.postValue(apiResponse2.loading());
                    }
                })
                .subscribe(new Consumer<CheckAvailabilityModel>() {
                    @Override
                    public void accept(CheckAvailabilityModel commonModel) throws Exception {
                        livedata2.postValue(apiResponse2.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata2.postValue(apiResponse2.error(throwable));
                    }
                });
    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}