package com.leo.structure2019.util;

import android.databinding.ObservableField;
import android.text.TextUtils;
import android.widget.Spinner;

import com.leo.homeloan.main.helpers.StringSpinnerAdapter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import timber.log.Timber;

public class Utils {

    private static String TAG = Utils.class.getClass().getSimpleName();

    public static boolean checkMin(ObservableField<String> field, long amount){
        try {
            Long currentAmount = Long.parseLong(field.get());
            return currentAmount < amount;
        }catch (Exception e){
            Timber.e(e);
            return false;
        }
    }

    public static boolean checkMinLength(ObservableField<String> field, long length){
        try {
            return field.get().length() < length;
        }catch (Exception e){
            Timber.e(e);
            return false;
        }
    }

    public static String rupeeFormat(String value){
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }

    public static boolean isDigitsOnly(String s) {
        return s != null && !s.isEmpty() && TextUtils.isDigitsOnly(s);
    }

    public static boolean isDouble(String number){
        try{
            Double.parseDouble(number);
            return true;
        }catch (Exception e){}
        return false;
    }

    public static void setSpinnerValue(Spinner spinner, String value) {

        try {
            StringSpinnerAdapter adapter = (StringSpinnerAdapter) spinner.getAdapter();
            List<String> strings = adapter.getItems();
            for (int i = 0; i < strings.size(); i++) {
                if (strings.get(i).equalsIgnoreCase(value)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public static void setSpinnerValue(Spinner spinner, int position) {

        try {
            spinner.setSelection(position);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
