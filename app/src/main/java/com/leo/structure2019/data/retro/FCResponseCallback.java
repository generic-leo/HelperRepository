package com.leo.structure2019.data.retro;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.remote.APIResponse;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class FCResponseCallback<T> implements Callback<APIResponse> {

    private Repository.RemoteCallback<T> responseListener;
    private boolean decodeResponse = false;

    // https://stackoverflow.com/questions/27893342/how-to-convert-list-to-a-json-object-using-gson
    private Type listType = null;

    public FCResponseCallback(Repository.RemoteCallback responseListener){
        this.responseListener = responseListener;
        responseListener.onStart();
    }

    public FCResponseCallback(Repository.RemoteCallback responseListener, boolean decodeResponse, Type listType){
        this.decodeResponse = decodeResponse;
        this.responseListener = responseListener;
        this.listType = listType;
        responseListener.onStart();
    }

    @Override
    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
        if(response.isSuccessful()){
            String error_code = response.body().error_code;
            if(error_code.equalsIgnoreCase("0")){
                if(decodeResponse){
                    // To Decrypt
                    try {
                        String output = response.body().data.toString();
                        Timber.i("Response = " + output);
                        String decrypted = output;
                        responseListener.onSuccess( (T) new Gson().fromJson(decrypted , listType) ) ;
                    }catch (Exception e){
                        Timber.e(e);
                        responseListener.onFailure("2", "Received Invalid Response From Server");
                    }

                }else{
                    String output = response.body().data;
                    Timber.i("Response = " + output);
                    responseListener.onSuccess( (T) output );
                }

            }else{
                responseListener.onFailure(error_code, response.body().error_message);
            }
        }else{
            responseListener.onFailure("1", "Server Error\n"+ response.code()+ ":"+ response.message());
        }
    }

    @Override
    public void onFailure(Call<APIResponse> call, Throwable t) {
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
