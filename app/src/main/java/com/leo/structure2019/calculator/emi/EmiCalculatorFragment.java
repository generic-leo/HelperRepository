package com.leo.structure2019.calculator.emi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.leo.homeloan.databinding.FragmentEmiCalculatorBinding;
import com.leo.homeloan.main.MainActivity;
import com.leo.homeloan.main.helpers.BaseActivity;
import com.leo.homeloan.main.helpers.BaseFragment;

public class EmiCalculatorFragment extends BaseFragment<MainActivity> {

    public static EmiCalculatorFragment newInstance(){
        return new EmiCalculatorFragment();
    }

    FragmentEmiCalculatorBinding binding;
    EmiCalculatorModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // model = MainActivity.getModel(getActivity(), EmiCalculatorModel.class);
        model = new EmiCalculatorModel(activity().getApplication());

        binding = FragmentEmiCalculatorBinding.inflate(inflater, container, false);
        binding.setViewModel(model);

        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        binding.btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showMessageDialog(model.calculateEMI(), "OK", new BaseActivity.DialogDismiss() {
                    @Override
                    public void onDismiss() {

                    }
                },"",null);
            }
        });*/

        binding.edMonthlyIncome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                model.setMonthlyIncomePropertyChangedCallbacks(false);
                binding.edMonthlyIncome.setText("");
                return false;
            }
        });

        binding.edMonthlyIncome.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {
                    String s = model.parseMonthlyIncome();
                    if (!TextUtils.isEmpty(s)) {
                        model.strMonthlyIncome.set("");
                        activity().showMessageDialog(s, "OK", new BaseActivity.DialogDismiss() {
                            @Override
                            public void onDismiss() {
                                binding.edMonthlyIncome.requestFocus();
                            }
                        }, "", null);
                    }
                }
                return false;
            }
        });

        binding.edRateOfInterest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                model.setROIPropertyChangedCallbacks(false);
                binding.edRateOfInterest.setText("");
                return false;
            }
        });

        binding.edRateOfInterest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String roiMsg =  model.parseRateOfInterest();
                    if (!TextUtils.isEmpty(roiMsg)) {
                        model.strRateOfIncome.set("");
                        activity().showMessageDialog(roiMsg, "OK", new BaseActivity.DialogDismiss() {
                            @Override
                            public void onDismiss() {
                                binding.edRateOfInterest.requestFocus();
                            }
                        }, "", null);
                    }
                    binding.edRateOfInterest.setSelection(binding.edRateOfInterest.getText().length());
                }
                return false;
            }
        });

        binding.edLoanTenure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                model.setLoanTenurePropertyChangedCallbacks(false);
                binding.edLoanTenure.setText("");
                return false;
            }
        });

        binding.edLoanTenure.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String tenure = model.parseTenureYears();
                    if (!TextUtils.isEmpty(tenure)) {
                        model.strLoanTenure.set("");
                        activity().showMessageDialog(tenure, "OK", new BaseActivity.DialogDismiss() {
                            @Override
                            public void onDismiss() {
                                binding.edLoanTenure.requestFocus();
                            }
                        }, "", null);
                    }
                }
                return false;
            }
        });

        binding.sbROI.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                model.setROIPropertyChangedCallbacks(true);
                return false;
            }
        });

        binding.sbLoanTenure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                model.setLoanTenurePropertyChangedCallbacks(true);
                return false;
            }
        });

        binding.sbMonthlyIncome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                model.setMonthlyIncomePropertyChangedCallbacks(true);
                return false;
            }
        });

        binding.sbROI.setMax(model.getRateOfInterestMax());
        binding.sbMonthlyIncome.setMax(model.getLoanAmountMax());
    }
}
