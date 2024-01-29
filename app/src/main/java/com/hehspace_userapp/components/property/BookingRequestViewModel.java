package com.hehspace_userapp.components.property;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.CheckAvailabilityModel;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.PropertyDetailModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BookingRequestViewModel extends ViewModel {
    public MutableLiveData<ApiResponse<CommonModel>> livedata = new MutableLiveData<>();
    ApiResponse<CommonModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;

    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public void createRequest(HashMap<String, String> reqData) {
        subscription = restApi.createRequest(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel propertyDetailModel) throws Exception {
                        livedata.postValue(apiResponse.success(propertyDetailModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
