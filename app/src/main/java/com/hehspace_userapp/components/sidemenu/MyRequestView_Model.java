package com.hehspace_userapp.components.sidemenu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.NotificationModel;
import com.hehspace_userapp.model.RequestListModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyRequestView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<RequestListModel>> livedata = new MutableLiveData<>();
    ApiResponse<RequestListModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }
    public  void getRequesList() {
        subscription = restApi.getRequesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<RequestListModel>() {
                    @Override
                    public void accept(RequestListModel notificationModel) throws Exception {
                        livedata.postValue(apiResponse.success(notificationModel));
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

