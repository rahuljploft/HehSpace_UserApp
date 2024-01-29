package com.hehspace_userapp.components.fragment.dashboard.save;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertyListModel;
import com.hehspace_userapp.model.WishListModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.network.RestApi;
import com.hehspace_userapp.network.RestApiFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SavedView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<WishListModel>> liveData = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<CommonModel>> livedata1 = new MutableLiveData<>();
    ApiResponse<WishListModel> apiResponse = null;
    private RestApi restApi = null;
    private Disposable subscription = null;
    ApiResponse<CommonModel> apiResponse3 = null;

    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponse3 = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public void getWishList() {
        subscription = restApi.getWishList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        liveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<WishListModel>() {
                    @Override
                    public void accept(WishListModel wishListModel) throws Exception {
                        liveData.postValue(apiResponse.success(wishListModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        liveData.postValue(apiResponse.error(throwable));
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