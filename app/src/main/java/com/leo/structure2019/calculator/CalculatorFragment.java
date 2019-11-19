package com.leo.structure2019.calculator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.homeloan.R;
import com.leo.homeloan.calculator.eligibility.EligibilityCalculatorFragment;
import com.leo.homeloan.calculator.emi.EmiCalculatorFragment;
import com.leo.homeloan.databinding.FragmentCalculatorBinding;
import com.leo.homeloan.main.MainActivity;
import com.leo.homeloan.main.helpers.BaseFragment;
import com.leo.homeloan.util.ActivityUtils;

public class CalculatorFragment extends BaseFragment<MainActivity>{

    public static CalculatorFragment newInstance(){
        return new CalculatorFragment();
    }

    FragmentCalculatorBinding binding;
    CalculatorModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        model = MainActivity.getModel(activity(), CalculatorModel.class);
        binding = FragmentCalculatorBinding.inflate(inflater, container, false);
        binding.setModel(model);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (model.showEMI.get())
            loadEMI();
        else
            loadEligibility();

        binding.btnEligibilityCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEligibility();
            }
        });

        binding.btnEmiCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEMI();
            }
        });
    }

    private void loadEligibility(){
        model.showEMI.set(false);
        ActivityUtils.replaceFragmentInActivity(getChildFragmentManager(),
                EligibilityCalculatorFragment.newInstance(),
                R.id.contentFrameChild);
    }

    private void loadEMI(){
        model.showEMI.set(true);
        ActivityUtils.replaceFragmentInActivity(getChildFragmentManager(),
                EmiCalculatorFragment.newInstance(),
                R.id.contentFrameChild);
    }
}
