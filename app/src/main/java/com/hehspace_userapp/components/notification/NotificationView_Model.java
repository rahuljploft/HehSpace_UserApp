package com.hehspace_userapp.components.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.NotificationModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NotificationView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<NotificationModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> Commonlivedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> Commonlivedata1 = new MutableLiveData<>();
    ApiResponse<NotificationModel> apiResponse = null;
    ApiResponse<CommonModel> apiResponse1 = null;
    ApiResponse<CommonModel> apiResponse2 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse2 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void notificationList() {
        subscription = restApi.notificationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<NotificationModel>() {
                    @Override
                    public void accept(NotificationModel notificationModel) throws Exception {
                        livedata.postValue(apiResponse.success(notificationModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public  void deleteNotification() {
        subscription = restApi.deleteNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> Commonlivedata.postValue(apiResponse1.loading()))
                .subscribe(commonModel -> Commonlivedata.postValue(apiResponse1.success(commonModel)), throwable -> Commonlivedata.postValue(apiResponse1.error(throwable)));
    }

    public  void seenNotification() {
        subscription = restApi.seenNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> Commonlivedata1.postValue(apiResponse2.loading()))
                .subscribe(commonModel -> Commonlivedata1.postValue(apiResponse2.success(commonModel)), throwable -> Commonlivedata1.postValue(apiResponse2.error(throwable)));
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}
