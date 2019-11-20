package com.leo.structure2019.data.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefRepository {
    SharedPreferences sharedPreferences;

    public SharedPrefRepository(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveLoginID(String loginID){
        sharedPreferences.edit().putString("loginID",  loginID).apply();
    }
    public void saveappFormNo(String appFormNo){
        sharedPreferences.edit().putString("appFormNo", appFormNo).apply();
    }

    public void saveUserType(String type){
        sharedPreferences.edit().putString("userType", type).apply();
    }

    public String getUserType(){
        return sharedPreferences.getString("userType","");
    }

    public void setLoginStatus(boolean status){
        sharedPreferences.edit().putBoolean("status",status).apply();
    }

    public boolean getLoginStatus(){
        return sharedPreferences.getBoolean("status", false);
    }

    /*
     * Only In Case of Employee
     */
    public  String getLoginID(){
        return sharedPreferences.getString("loginID","0");
    }

    public void saveUserID(String userID){
        sharedPreferences.edit().putString("userID", userID).apply();
    }

    public String getUserID(){
        return sharedPreferences.getString("userID","0");
    }
    public String getappFormNo(){
        return sharedPreferences.getString("appFormNo","");
    }

    public void saveMasterVersion(String version){
        sharedPreferences.edit().putString("masterVersion", version).apply();
    }

    public String getMasterVersion(){
        return sharedPreferences.getString("masterVersion","0");
    }

    public void setSessionTime(String time){
        sharedPreferences.edit().putString("sessionTime", time).apply();
    }

    public String getSessionTime(String defaultTime){
        return sharedPreferences.getString("sessionTime", defaultTime);
    }
}
