package com.leo.structure2019.calculator.eligibility;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.leo.homeloan.util.Utils;

import timber.log.Timber;

public class EligibilityCalculatorModel extends AndroidViewModel {

    // LIMITS
    private int MONTHLY_MIN = 10_000;
    private int MONTHLY_MAX = 50_00_000;

    private int TENURE_MIN = 1;
    private int TENURE_MAX = 30;

    public int RATEOI_MIN = 7;
    public int RATEOI_MAX = 20;
    public String SEPARATOR_PERCENT = "%";

    public ObservableInt loanAmount = new ObservableInt(0);
    public ObservableBoolean tenureTypeYear = new ObservableBoolean(true);
    public ObservableInt tenureStart = new ObservableInt(TENURE_MIN);
    public ObservableInt tenureEnd = new ObservableInt(TENURE_MAX);
    public ObservableInt tenureYears = new ObservableInt(0);
    public ObservableInt rateOfInterest = new ObservableInt(0);
    public ObservableInt monthlyIncomeSeekBarProgress = new ObservableInt(0);

    public ObservableField<String> eligibleAmt = new ObservableField<>("");
    public ObservableField<String> strTenureStart = new ObservableField<>( String.valueOf(TENURE_MIN) );
    public ObservableField<String> strTenureEnd = new ObservableField<>( String.valueOf(TENURE_MAX) );
    public ObservableField<String> strMonthlyIncome = new ObservableField<>("");
    public ObservableField<String> strLoanTenure = new ObservableField<>("");
    public ObservableField<String> strRateOfIncome = new ObservableField<>("");
    private Observable.OnPropertyChangedCallback monthlyIncomeCallback;
    private Observable.OnPropertyChangedCallback LoanTenureCallback;
    private Observable.OnPropertyChangedCallback rateOfInterestCallback;

    public ObservableBoolean eligibleAmtShow = new ObservableBoolean(false);

    public EligibilityCalculatorModel(@NonNull Application application) {
        super(application);

        eligibleAmtShow.set(false);
        tenureTypeYear.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(tenureTypeYear.get()) {
                    tenureStart.set(0);
                    strTenureStart.set(String.valueOf(TENURE_MIN));
                    strTenureEnd.set(String.valueOf(TENURE_MAX));
                }
                else {
                    tenureStart.set(0);
                    strTenureStart.set(String.valueOf(TENURE_MIN * 12));
                    strTenureEnd.set(String.valueOf(TENURE_MAX * 12));
                }

                tenureEnd.set(getTenureMax());
                tenureYears.notifyChange();
            }
        });

        initPropertyChangeListeners();

        strMonthlyIncome.set("₹ " + Utils.rupeeFormat(String.valueOf(MONTHLY_MIN)));
        strLoanTenure.set(TENURE_MIN+" Yrs");
        strRateOfIncome.set(RATEOI_MIN+" %");
        monthlyIncomeSeekBarProgress.addOnPropertyChangedCallback(monthlyIncomeCallback);
        tenureYears.addOnPropertyChangedCallback(LoanTenureCallback);
        rateOfInterest.addOnPropertyChangedCallback(rateOfInterestCallback);

        monthlyIncomeSeekBarProgress.set(0);
        tenureYears.set(0);
        rateOfInterest.set(0);
    }

    public int getMonthlyMax(){
        return MONTHLY_MAX / MONTHLY_MIN;
    }

    public int getTenureMax(){
        if (tenureTypeYear.get()){
            return TENURE_MAX / TENURE_MIN;
        }else {
            return (TENURE_MAX*12)/ (TENURE_MIN*12);
        }
    }

    public int getRateOfInterestMax(){
        return RATEOI_MAX - RATEOI_MIN;
    }

    private double getMonthlySeekbar(){
        return getMonthlyIncome(monthlyIncomeSeekBarProgress.get());
    }

    private int getRateOfInterestSeekbar(){
        return getRateOfInterest(rateOfInterest.get());
    }

    private double getTenureSeekbar(){
        return getTenure(tenureYears.get(), tenureTypeYear.get()) * (tenureTypeYear.get() ? 12 : 1);
    }

    private double getTenureSeekbar(int input){
        return input * (tenureTypeYear.get() ? 12 : 1);
    }

    private double getROIFromEditText(double roi){
        String rateOfInterest = strRateOfIncome.get();
        if (Utils.isDouble(rateOfInterest)) {
            return Double.parseDouble(rateOfInterest);
        }

        return roi;
    }

    private double getTenureMonthsFromEditText(double tenure_in_months){
        String tenureYears = strLoanTenure.get()
                            .replace("Yrs","")
                            .replace("Months","")
                            .trim();

        if (Utils.isDigitsOnly(tenureYears)) {
            Integer tenure = Integer.parseInt(tenureYears);
            if (tenureTypeYear.get()) {
                // Convert To Month
                return tenure * 12;
            } else {
                // Convert To Year
                return tenure;
            }
        }

        return tenure_in_months;
    }

    public String calculate(double monthly_income, double roi, double tenure_in_months) {

        roi = getROIFromEditText(roi);
        tenure_in_months = getTenureMonthsFromEditText(tenure_in_months);
        monthly_income = getMonthlyFromEditText(monthly_income);

        // Need Tenure only in Months
        // double tenure_in_months = getTenure(tenureYears.get(), tenureTypeYear.get()) * (tenureTypeYear.get() ? 12 : 1);

        // ROI = ROI / 100 = 0.085
        // R = ROI / 12 monthly
        // 1 + R
        double roi_monthly = ( (roi/100F) / 12F );
        double one_plus_r =  1 +  roi_monthly ;
        double one_plus_r_raised_n =  Math.pow(one_plus_r, tenure_in_months);

        // PxRx(1+R)^N
        double loanAmountFigure = getLoanAmount(loanAmount.get());
        loanAmountFigure = 1_00_000;

        double secondLastStep = one_plus_r_raised_n * roi_monthly * loanAmountFigure;
        double lastStep = one_plus_r_raised_n - 1;
        double emi = secondLastStep/lastStep;

        // Fixed for Eligibility Calculator
        // double emi = 769;

        // double monthly_income = getMonthlyIncome(monthlyIncomeSeekBarProgress.get());
        double factor = monthly_income <= 25_000 ? 0.45D : 0.6D;

        // Only Rounding Off EMI
        // emi = Math.round(emi);


        Timber.i("Loan"+" EMI = "+ emi + " Monthly = "+monthly_income + " Factor = "+ factor + " Tenure = "+ tenure_in_months + " ROI : "+ roi);
        double result = ((monthly_income * factor)/ emi);
        Timber.i("Result = "+result);
        result = result * 1_00_000;
        Timber.i("Result * 100K = "+((int)result));

        // String loanAmountString = Utils.rupeeFormat(""+Math.round(result));
        String loanAmountString = Utils.rupeeFormat(""+ ((int)result));

        Timber.i("=== ELIGIBILITY AMOUNT = "+loanAmountString+" ===");
        Timber.i("ROI = "+ roi);
        Timber.i("ROI Monthly = "+ roi_monthly);
        Timber.i("Tenure Years = "+ tenure_in_months/12);
        Timber.i("Tenure Month = "+ tenure_in_months);
        Timber.i("1+R = "+ one_plus_r);
        Timber.i("1+R ^ N = "+ one_plus_r_raised_n);
        Timber.i("PxRx(1+R)^N = "+ secondLastStep);
        Timber.i("(1+R)^N-1 = "+ lastStep);
        Timber.i("EMI = "+ emi);
        Timber.i("Final Result = "+ loanAmountString);

        return  "₹ "+ loanAmountString;
    }

    private double getMonthlyFromEditText(double input){
        String monthlyIncome = strMonthlyIncome.get()
                                .replace("₹","")
                                .replace(",","")
                                .trim();
        if (Utils.isDigitsOnly(monthlyIncome)) {
            return Double.parseDouble(monthlyIncome);
        }

        return input;
    }

    // region [Misc Methods]
    public String parseLoanAmount(int seekbarProgress) {
        return "₹ " + Utils.rupeeFormat(String.valueOf( getLoanAmount(seekbarProgress) ));
    }

    public int getLoanAmount(int progress) {
        // Default 10 Lakhs
        // return 1_000 * (progress + 5);
        return getMonthlyIncome(progress);
    }

    public String parseTenure(int seekbarProgress){
        boolean isYear = tenureTypeYear.get();
        return getTenure(seekbarProgress, isYear) + (isYear? " Yrs": " Months");
    }

    public int getTenure(int seekbarProgress, boolean isYear) {
        // Default is 1 year / 12 Months
        int min = TENURE_MIN;
        int division = (isYear ? min : min * 12);
        Timber.i("Division : " + division + " SeekBar : " + seekbarProgress);

        if (seekbarProgress == getTenureMax())
            return seekbarProgress * division;

        return division + (seekbarProgress  * division);
    }

    public String parseRateOfInterest(int seekbarProgress){
        return getRateOfInterest(seekbarProgress) + " %";
    }

    public int getRateOfInterest(int seekbarProgress) {
        return (RATEOI_MIN + (seekbarProgress * 1));
    }

    public String parseMonthlyIncome(int seekbarProgress){
        return "₹ " + Utils.rupeeFormat(String.valueOf(getMonthlyIncome(seekbarProgress)));
    }

    public int getMonthlyIncome(int seekbarProgress) {
        if (seekbarProgress == getMonthlyMax())
            return seekbarProgress * MONTHLY_MIN;
        return (MONTHLY_MIN + (seekbarProgress * MONTHLY_MIN));
    }

    // Below methods are added by JUDE on 16-07-2018
    private void initPropertyChangeListeners() {
        rateOfInterestCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                strRateOfIncome.set(getRateOfInterest(rateOfInterest.get()) +" %");
                eligibleAmt.set(calculate(getMonthlySeekbar(), getRateOfInterestSeekbar(), getTenureSeekbar()));
            }
        };

        LoanTenureCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                strLoanTenure.set(parseTenure(tenureYears.get()));
                eligibleAmt.set(calculate(getMonthlySeekbar(), getRateOfInterestSeekbar(), getTenureSeekbar()));
            }
        };

        monthlyIncomeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                strMonthlyIncome.set(parseLoanAmount(monthlyIncomeSeekBarProgress.get()));
                eligibleAmt.set(calculate(getMonthlySeekbar(), getRateOfInterestSeekbar(), getTenureSeekbar()));
            }
        };

        eligibleAmt.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                eligibleAmtShow.set(!TextUtils.isEmpty(eligibleAmt.get()));
            }
        });
    }

    public void setMonthlyIncomePropertyChangedCallbacks(boolean addCallbacks) {
        if (addCallbacks) {
            monthlyIncomeSeekBarProgress.addOnPropertyChangedCallback(monthlyIncomeCallback);
        } else {
            monthlyIncomeSeekBarProgress.removeOnPropertyChangedCallback(monthlyIncomeCallback);
        }
    }

    public String parseMonthlyIncome() {
        String monthlyIncome = strMonthlyIncome.get();
        if (Utils.isDigitsOnly(monthlyIncome)) {
            Double amount = Double.parseDouble(monthlyIncome);
            if (amount < MONTHLY_MIN || amount > MONTHLY_MAX) {
                return "Monthly Income should be between ₹"+ Utils.rupeeFormat( String.valueOf(MONTHLY_MIN) )+" & ₹"+Utils.rupeeFormat( String.valueOf(MONTHLY_MAX) );
            }
            setMonthlyIncome();
            strMonthlyIncome.set("₹ " + Utils.rupeeFormat(monthlyIncome));
            eligibleAmt.set(calculate(amount, getRateOfInterestSeekbar(), getTenureSeekbar()));
        }
        return "";
    }

    private void setMonthlyIncome() {
        String monthlyIncome = strMonthlyIncome.get();
        if (Utils.isDigitsOnly(monthlyIncome)) {
            Integer amount = Integer.parseInt(monthlyIncome);
            amount = amount / MONTHLY_MIN;
            this.monthlyIncomeSeekBarProgress.set(amount);
        }
    }

    public void setROIPropertyChangedCallbacks(boolean addCallbacks) {
        if (addCallbacks) {
            rateOfInterest.addOnPropertyChangedCallback(rateOfInterestCallback);
        } else {
            rateOfInterest.removeOnPropertyChangedCallback(rateOfInterestCallback);
        }
    }

    public String parseRateOfInterest() {
        String rateOfInterest = strRateOfIncome.get();
        if (Utils.isDouble(rateOfInterest)) {
            Double roi = Double.parseDouble(rateOfInterest);
            if (roi < RATEOI_MIN || roi > RATEOI_MAX) {
                return "Rate of Interest should be between "+RATEOI_MIN+"% & "+RATEOI_MAX+"%";
            }

            setRateOfInterest();
            eligibleAmt.set(calculate(getMonthlySeekbar(), roi, getTenureSeekbar()));
        }
        return "";
    }

    private void setRateOfInterest() {
        String rateOfInterest = strRateOfIncome.get();
        if (Utils.isDouble(rateOfInterest)) {
            double roi = Double.parseDouble(rateOfInterest);
            roi = (int)roi - RATEOI_MIN;
            this.rateOfInterest.set((int)roi);
        }
    }

    public void setLoanTenurePropertyChangedCallbacks(boolean addCallbacks) {
        if (addCallbacks) {
            tenureYears.addOnPropertyChangedCallback(LoanTenureCallback);
        } else {
            tenureYears.removeOnPropertyChangedCallback(LoanTenureCallback);
        }
    }

    public String parseTenureYears() {
        String tenureYears = strLoanTenure.get();
        if (Utils.isDigitsOnly(tenureYears)) {
            Integer tenure = Integer.parseInt(tenureYears);
            if (tenureTypeYear.get()) {
                if (tenure < TENURE_MIN || tenure > TENURE_MAX) {
                    return "Loan Tenure should be between "+TENURE_MIN+" & "+TENURE_MAX+" years only";
                }
            } else {
                if (tenure < (TENURE_MIN * 12) || tenure > (TENURE_MAX * 12)) {
                    return "Loan Tenure should be between "+(TENURE_MIN*12)+" & "+(TENURE_MAX*12)+" months only";
                }
            }
            setLoanTenureYears();
            eligibleAmt.set(calculate(getMonthlySeekbar(), getRateOfInterestSeekbar(), getTenureSeekbar(tenure)));
            return "";
        }
        return "Enter Appropriate Loan Tenure";
    }

    private void setLoanTenureYears() {
        String tenureYears = strLoanTenure.get();

        if (Utils.isDigitsOnly(tenureYears)) {
            Integer tenure = Integer.parseInt(tenureYears);
            if (tenureTypeYear.get()) {
                tenure = tenure / TENURE_MIN;
            } else {
                tenure = (tenure / (TENURE_MIN * 12));
            }

            this.tenureYears.set(tenure);
        }
    }
}
