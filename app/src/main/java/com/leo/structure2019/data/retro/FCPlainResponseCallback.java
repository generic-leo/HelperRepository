package com.leo.structure2019.data.retro;

import com.google.gson.JsonSyntaxException;
import com.leo.homeloan.data.Repository;

import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class FCPlainResponseCallback<T> implements Callback<T> {

    private Repository.RemoteCallback<T> responseListener;

    public FCPlainResponseCallback(Repository.RemoteCallback responseListener){
        this.responseListener = responseListener;
        responseListener.onStart();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
                    try {
                        responseListener.onSuccess( response.body() ) ;
                    }catch (Exception e){
                        Timber.e(e);
                        responseListener.onFailure("2", "Received Invalid Response From Server");
                    }
        }else{
            responseListener.onFailure("1", "Server Error\n"+ response.code()+ ":"+ response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        String error_message = "Failed To Connect Server";

        Timber.e(t);

        // A JSON Exception Case
        if(t instanceof JsonSyntaxException){
            error_message = "Received Invalid Response From Server";
        }

        if (t instanceof FileNotFoundException){
            error_message = "IOException: Failed To Upload File To Server";
        }

        responseListener.onFailure("0", error_message);
    }
}
