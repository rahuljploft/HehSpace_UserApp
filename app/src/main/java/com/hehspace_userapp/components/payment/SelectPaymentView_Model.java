package com.hehspace_userapp.components.payment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hehspace_userapp.model.Bitcoin_Payment_Check_Model;
import com.hehspace_userapp.model.Bitcoin_payment_model;
import com.hehspace_userapp.model.CommonModel;
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

public class SelectPaymentView_Model extends ViewModel {
    public MutableLiveData<ApiResponse<CommonModel>> livedata = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<Bitcoin_payment_model>> livedatabitcoin = new MutableLiveData<>();
    public MutableLiveData<ApiResponse<Bitcoin_Payment_Check_Model>> livedatabitcoinsuccess = new MutableLiveData<>();
    ApiResponse<CommonModel> apiResponse = null;
    ApiResponse<Bitcoin_payment_model> apiResponsebitcoin = null;
    ApiResponse<Bitcoin_Payment_Check_Model> apiResponsebitcoincusccess = null;
    private RestApi restApi = null;
    private Disposable subscription = null;

    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponsebitcoin = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResponsebitcoincusccess = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public void createBooking(String first_name, String last_name, String email_address, String mobile_number, String book_for, String request_id,
                              File id_document, String card_no, String exp_month, String exp_year, String cvc) {
        MultipartBody.Part mp_first_name = MultipartBody.Part.createFormData("first_name", first_name);
        MultipartBody.Part mp_last_name = MultipartBody.Part.createFormData("last_name", last_name);
        MultipartBody.Part mp_email_address = MultipartBody.Part.createFormData("email_address", email_address);
        MultipartBody.Part mp_mobile_number = MultipartBody.Part.createFormData("mobile_number", mobile_number);
        MultipartBody.Part mp_book_for = MultipartBody.Part.createFormData("book_for", book_for);
        MultipartBody.Part mp_request_id = MultipartBody.Part.createFormData("request_id", request_id);
        MultipartBody.Part mp_card_token = MultipartBody.Part.createFormData("card_token", "tok_gkjhkjhkjhkjhjk");
        MultipartBody.Part mp_card_no = MultipartBody.Part.createFormData("card_no", card_no);
        MultipartBody.Part mp_exp_month = MultipartBody.Part.createFormData("exp_month", exp_month);
        MultipartBody.Part mp_exp_year = MultipartBody.Part.createFormData("exp_year", exp_year);
        MultipartBody.Part mp_cvc = MultipartBody.Part.createFormData("cvc", cvc);
        MultipartBody.Part id_documentphoto;
        if (id_document != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), id_document);
            id_documentphoto = MultipartBody.Part.createFormData("id_document", id_document.getName(), requestBody);
        } else {
            id_documentphoto = MultipartBody.Part.createFormData("id_document", "");
        }


        subscription = restApi.createBooking(mp_book_for, mp_request_id, mp_card_token, mp_first_name, mp_last_name, mp_email_address, mp_mobile_number,
                id_documentphoto, mp_card_no,
                mp_exp_month, mp_exp_year, mp_cvc)
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
                    public void accept(CommonModel commonModel) throws Exception {
                        livedata.postValue(apiResponse.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedata.postValue(apiResponse.error(throwable));
                    }
                });
    }

    public void createBooking_bitcoin(String first_name, String last_name, String email_address, String mobile_number, String book_for, String request_id,
                                      File id_document) {
        MultipartBody.Part mp_first_name = MultipartBody.Part.createFormData("first_name", first_name);
        MultipartBody.Part mp_last_name = MultipartBody.Part.createFormData("last_name", last_name);
        MultipartBody.Part mp_email_address = MultipartBody.Part.createFormData("email_address", email_address);
        MultipartBody.Part mp_mobile_number = MultipartBody.Part.createFormData("mobile_number", mobile_number);
        MultipartBody.Part mp_book_for = MultipartBody.Part.createFormData("book_for", book_for);
        MultipartBody.Part mp_request_id = MultipartBody.Part.createFormData("request_id", request_id);
        MultipartBody.Part mp_card_token = MultipartBody.Part.createFormData("card_token", "tok_gkjhkjhkjhkjhjk");
        MultipartBody.Part id_documentphoto;
        if (id_document != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), id_document);
            id_documentphoto = MultipartBody.Part.createFormData("id_document", id_document.getName(), requestBody);
        } else {
            id_documentphoto = MultipartBody.Part.createFormData("id_document", "");
        }


        subscription = restApi.create_booking_bitcoin(mp_book_for, mp_request_id, mp_card_token, mp_first_name, mp_last_name, mp_email_address, mp_mobile_number,
                id_documentphoto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedatabitcoin.postValue(apiResponsebitcoin.loading());
                    }
                })
                .subscribe(new Consumer<Bitcoin_payment_model>() {
                    @Override
                    public void accept(Bitcoin_payment_model commonModel) throws Exception {
                        livedatabitcoin.postValue(apiResponsebitcoin.success(commonModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedatabitcoin.postValue(apiResponsebitcoin.error(throwable));
                    }
                });
    }

    public void checkpayment(HashMap<String, String> reqData) {
        subscription = restApi.checkpayment(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        livedatabitcoinsuccess.postValue(apiResponsebitcoincusccess.loading());
                    }
                })
                .subscribe(new Consumer<Bitcoin_Payment_Check_Model>() {
                    @Override
                    public void accept(Bitcoin_Payment_Check_Model loginData) throws Exception {
                        livedatabitcoinsuccess.postValue(apiResponsebitcoincusccess.success(loginData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        livedatabitcoinsuccess.postValue(apiResponsebitcoincusccess.error(throwable));
                    }
                });
    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}


