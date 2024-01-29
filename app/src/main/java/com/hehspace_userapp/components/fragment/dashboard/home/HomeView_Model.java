package com.hehspace_userapp.components.fragment.dashboard.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.PropertyAmenitiesModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertyListModel;
import com.hehspace_userapp.model.PropertySearchModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<PropertyCategoryModel>> propertycategorylivedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<PropertyCategoryModel>> propertycategorylivedata1 = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<PropertyListModel>> propertylistlivedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<PropertyAmenitiesModel>> amenitieslistlivedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<PropertySearchModel>> searchlivedata = new MutableLiveData<>();
    ApiResponse<PropertyCategoryModel> apiResponse = null;
    ApiResponse<PropertyCategoryModel> apiResponse11 = null;
    ApiResponse<PropertyListModel> apiResponse1 = null;
    ApiResponse<PropertyAmenitiesModel> apiResponse2 = null;
    ApiResponse<PropertySearchModel> apiResponse3 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse2 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse11 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse3 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void propertyCategory() {
        subscription = restApi.propertyCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<PropertyCategoryModel>() {
                    @Override
                    public void accept(PropertyCategoryModel propertyCategoryModel) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.success(propertyCategoryModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        propertycategorylivedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public  void propertyCategory1() {
        subscription = restApi.propertyCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        propertycategorylivedata1.postValue(apiResponse11.loading());
                    }
                })
                .subscribe(new Consumer<PropertyCategoryModel>() {
                    @Override
                    public void accept(PropertyCategoryModel propertyCategoryModel) throws Exception {
                        propertycategorylivedata1.postValue(apiResponse11.success(propertyCategoryModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        propertycategorylivedata1.postValue(apiResponse11.error(throwable));
                    }
                });
    }

    public void getPropertyList(HashMap<String, String> reqData) {
        subscription = restApi.propertyList(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        propertylistlivedata.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<PropertyListModel>() {
                    @Override
                    public void accept(PropertyListModel propertyListModel) throws Exception {
                        propertylistlivedata.postValue(apiResponse1.success(propertyListModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        propertylistlivedata.postValue(apiResponse1.error(throwable));
                    }
                });
    }

    public void getPropertyAmenities() {
        subscription = restApi.getPropertyAmenities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        amenitieslistlivedata.postValue(apiResponse2.loading());
                    }
                })
                .subscribe(new Consumer<PropertyAmenitiesModel>() {
                    @Override
                    public void accept(PropertyAmenitiesModel propertyListModel) throws Exception {
                        amenitieslistlivedata.postValue(apiResponse2.success(propertyListModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        amenitieslistlivedata.postValue(apiResponse2.error(throwable));
                    }
                });
    }

    public  void SearchProperty(HashMap<String,String> hashMap) {
        subscription = restApi.searchProperty(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        searchlivedata.postValue(apiResponse3.loading());
                    }
                })
                .subscribe(new Consumer<PropertySearchModel>() {
                    @Override
                    public void accept(PropertySearchModel searchModel) throws Exception {
                        searchlivedata.postValue(apiResponse3.success(searchModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        searchlivedata.postValue(apiResponse3.error(throwable));
                    }
                });
    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}