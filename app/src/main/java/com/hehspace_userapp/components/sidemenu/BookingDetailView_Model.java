package com.hehspace_userapp.components.sidemenu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.BookingDetailsModel;
import com.hehspace_userapp.model.BookingListModel;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BookingDetailView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<BookingDetailsModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> livedata1 = new MutableLiveData<>();
    ApiResponse<BookingDetailsModel> apiResponse = null;
    ApiResponse<CommonModel> apiResponse1 = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse1 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public  void getBookingDetails(int itemId) {
        subscription = restApi.getBookingDetails(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedata.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<BookingDetailsModel>() {
                    @Override
                    public void accept(BookingDetailsModel bookingDetailsModel) throws Exception {
                        livedata.postValue(apiResponse.success(bookingDetailsModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public  void cancelBooking(int id, HashMap<String,String> reqData) {
        subscription = restApi.cancelBooking(id,reqData)
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



    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}

