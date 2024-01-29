package com.hehspace_userapp.components.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.CityModel;
import com.hehspace_userapp.model.CommonModel;
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

public class SearchView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<PropertyCategoryModel>> propertycategorylivedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CityModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<PropertySearchModel>> searchlivedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> livedata1 = new MutableLiveData<>();
    ApiResponse<PropertyCategoryModel> apiResponse = null;
    ApiResponse<CityModel> apiResponse1 = null;
    ApiResponse<PropertySearchModel> apiResponse2 = null;
    ApiResponse<CommonModel> apiResponse3 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse2 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
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


    public  void getCity() {
        subscription = restApi.getCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse1.loading());
                    }
                })
                .subscribe(new Consumer<CityModel>() {
                    @Override
                    public void accept(CityModel propertyCategoryModel) throws Exception {
                        livedata.postValue(apiResponse1.success(propertyCategoryModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse1.error(throwable));
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
                        searchlivedata.postValue(apiResponse2.loading());
                    }
                })
                .subscribe(new Consumer<PropertySearchModel>() {
                    @Override
                    public void accept(PropertySearchModel searchModel) throws Exception {
                        searchlivedata.postValue(apiResponse2.success(searchModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        searchlivedata.postValue(apiResponse2.error(throwable));
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
                        livedata1.postValue(apiResponse3.loading());
                    }
                })
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel) throws Exception {
                        livedata1.postValue(apiResponse3.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata1.postValue(apiResponse3.error(throwable));
                    }
                });
    }



    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}