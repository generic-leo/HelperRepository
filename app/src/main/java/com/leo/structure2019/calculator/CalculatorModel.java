package com.leo.structure2019.calculator;

import android.app.Application;
import android.databinding.ObservableBoolean;

import com.leo.homeloan.main.helpers.BaseViewModel;

import org.jetbrains.annotations.NotNull;

public class CalculatorModel extends BaseViewModel{

    public ObservableBoolean showEMI = new ObservableBoolean(false);

    public CalculatorModel(@NotNull Application appContext) {
        super(appContext);
    }


}
