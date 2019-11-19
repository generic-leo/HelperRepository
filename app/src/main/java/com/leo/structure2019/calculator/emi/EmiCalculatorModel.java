package com.leo.structure2019.calculator.emi;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.leo.homeloan.util.Utils;

import timber.log.Timber;

public class EmiCalculatorModel extends AndroidViewModel{

    private int TENURE_MIN = 1;
    private int TENURE_MAX = 30;

    public int RATEOI_MIN = 7;
    public int RATEOI_MAX = 20;
    public String SEPARATOR_PERCENT = "%";

    public int LOAN_AMOUNT_MIN = 3_00_000;
    public int LOAN_AMOUNT_MAX = 10_00_00_000;

    public ObservableInt loanAmount = new ObservableInt(0);
    public ObservableBoolean tenureTypeYear = new ObservableBoolean(true);
    public ObservableInt tenureStart = new ObservableInt(TENURE_MIN);
    public ObservableInt tenureEnd = new ObservableInt(TENURE_MAX);

    public ObservableInt tenureYears = new ObservableInt(0);
    public ObservableInt rateOfInterest = new ObservableInt(0);
    public ObservableInt monthlyIncome = new ObservableInt(0);

    public ObservableField<String> eligibleAmt = new ObservableField<>("");
    public ObservableField<String> strTenureStart = new ObservableField<>("1");
    public ObservableField<String> strTenureEnd = new ObservableField<>("30");
    public ObservableField<String> strMonthlyIncome = new ObservableField<>("");
    public ObservableField<String> strLoanTenure = new ObservableField<>("");
    public ObservableField<String> strRateOfIncome = new ObservableField<>("");
    private Observable.OnPropertyChangedCallback monthlyIncomeCallback;
    private Observable.OnPropertyChangedCallback LoanTenureCallback;
    private Observable.OnPropertyChangedCallback rateOfInterestCallback;

    public ObservableBoolean emiAmtShow = new ObservableBoolean(false);

    public EmiCalculatorModel(Application application){
        super(application);
        emiAmtShow.set(false);
        tenureTypeYear.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(tenureTypeYear.get()) {
                    tenureStart.set(0);
                    tenureEnd.set(getTenureMax());
                    strTenureStart.set(String.valueOf(TENURE_MIN));
                    strTenureEnd.set(String.valueOf(TENURE_MAX));
                }
                else {
                    tenureStart.set(0);
                    tenureEnd.set(getTenureMax());
                    strTenureStart.set(String.valueOf(TENURE_MIN*12));
                    strTenureEnd.set(String.valueOf(TENURE_MAX*12));
                }

                tenureYears.notifyChange();
            }
        });

        initPropertyChangeListeners();

        strMonthlyIncome.set("₹ " + Utils.rupeeFormat(String.valueOf(LOAN_AMOUNT_MIN)));
        strLoanTenure.set(TENURE_MIN + " Yrs");
        strRateOfIncome.set(RATEOI_MIN+" %");
        loanAmount.addOnPropertyChangedCallback(monthlyIncomeCallback);
        tenureYears.addOnPropertyChangedCallback(LoanTenureCallback);
        rateOfInterest.addOnPropertyChangedCallback(rateOfInterestCallback);

        loanAmount.set(0);
        tenureYears.set(0);
        rateOfInterest.set(0);
    }

    public int getRateOfInterestMax(){
        return RATEOI_MAX - RATEOI_MIN;
    }

    public int getTenureMax(){
        if (tenureTypeYear.get()){
            return TENURE_MAX / TENURE_MIN;
        }else {
            return (TENURE_MAX*12)/ (TENURE_MIN*12);
        }
    }

    public int getLoanAmountMax(){
        return (int)Math.ceil( Double.valueOf(LOAN_AMOUNT_MAX) / LOAN_AMOUNT_MIN );
    }

    public double getTenure(){
        return getTenure(tenureYears.get(), tenureTypeYear.get()) * (tenureTypeYear.get() ? 12 : 1);
    }

    public double getROI(){
        return getRateOfInterest(rateOfInterest.get());
    }

    private double getROIFromEditText(double roi){
        String rateOfInterest = strRateOfIncome.get()
                                .replace("%","");
        if (Utils.isDouble(rateOfInterest)) {
            return Double.parseDouble(rateOfInterest);
        }

        return roi;
    }

    public String calculateEMI(double tenure_in_months, double roi){
        roi = getROIFromEditText(roi);
        tenure_in_months = getTenureFromEditText();

        // Need Tenure only in Months
        // double tenure_in_months = getTenure(tenureYears.get(), tenureTypeYear.get()) * (tenureTypeYear.get() ? 12 : 1);

        // ROI = ROI / 100 = 0.085
        // R = ROI / 12 monthly
        // 1 + R
        double roi_monthly = ( ( roi /100F) / 12F );
        double one_plus_r =  1 +  roi_monthly ;
        // double one_plus_r_raised_n = new BigDecimal(one_plus_r).pow((int)tenure_in_months).doubleValue();
        // double one_plus_r_raised_n =  Math.pow(1.0125F, 240.0);
        double one_plus_r_raised_n =  Math.pow(one_plus_r, tenure_in_months);
        // double one_plus_r_raised_n =  Math.pow(one_plus_r, tenure_in_months);

        // PxRx(1+R)^N
        double loanAmountFigure = getLoanAmount(loanAmount.get());
        loanAmountFigure = getLoanAmountFromEditText();
        // loanAmountFigure = 1_00_000;

        Timber.i("Calculate Tenure : "+ tenure_in_months + " ROI : "+ roi+ " Loan Amount : "+ loanAmountFigure);

        double secondLastStep = one_plus_r_raised_n * roi_monthly * loanAmountFigure;
        double lastStep = one_plus_r_raised_n - 1;
        double emi = secondLastStep/lastStep;
        // Timber.i("Calculate EMI : " + emi);
        String emiString = Utils.rupeeFormat(""+Math.round(emi));

        Timber.i("==================================");
        Timber.i("=== EMI AMOUNT = "+emiString+" ===");
        Timber.i("Loan Amount = "+ loanAmountFigure);
        Timber.i("ROI = "+ roi);
        Timber.i("ROI Monthly = "+ roi_monthly);
        Timber.i("Tenure Years = "+ tenure_in_months/12);
        Timber.i("Tenure Month = "+ tenure_in_months);
        Timber.i("1+R = "+ one_plus_r);
        Timber.i("1+R ^ N = "+ one_plus_r_raised_n);
        Timber.i("PxRx(1+R)^N = "+ secondLastStep);
        Timber.i("(1+R)^N-1 = "+ lastStep);
        Timber.i("EMI = "+ emi);
        Timber.i("Final Result = "+ emiString);

        return "₹ " + emiString;
    }

    private double getLoanAmountFromEditText(){
        String monthlyIncome = strMonthlyIncome.get()
                                .replace("₹","")
                                .replace(",","")
                                .trim();

        if (Utils.isDigitsOnly(monthlyIncome)) {
            return Double.parseDouble(monthlyIncome);
        }
        return getLoanAmount(loanAmount.get());
    }

    private double getTenureFromEditText(){
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

        return getTenure();
    }

    // region [Misc Methods]
    public String parseLoanAmount(int seekbarProgress){
        return "₹ " + Utils.rupeeFormat(String.valueOf( getLoanAmount(seekbarProgress) ));
    }

    public int getLoanAmount(int progress){
        int result;

        if (progress == getLoanAmountMax())
            result = progress * LOAN_AMOUNT_MIN;
        else
            result = LOAN_AMOUNT_MIN + (progress  * LOAN_AMOUNT_MIN);

        if (result > LOAN_AMOUNT_MAX)
            result = LOAN_AMOUNT_MAX;

        Timber.i("Progress : " + progress+ " Result : "+ result);
        return result;
    }

    public String parseTenure(int seekbarProgress){
        boolean isYear = tenureTypeYear.get();
        return getTenure(seekbarProgress,isYear) + (isYear? " Yrs": " Months");
    }

    public int getTenure(int seekbarProgress, boolean isYear){
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

    public int getRateOfInterest(int seekbarProgress){
        return (RATEOI_MIN + (seekbarProgress * 1));
    }

    // Below methods are added by JUDE on 16-07-2018
    private void initPropertyChangeListeners() {
        rateOfInterestCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                strRateOfIncome.set(getRateOfInterest(rateOfInterest.get()) +" %");
                eligibleAmt.set(calculateEMI(getTenure(), getROI()));
            }
        };

        LoanTenureCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId)
            {
                strLoanTenure.set(parseTenure(tenureYears.get()));
                eligibleAmt.set(calculateEMI(getTenure(), getROI()));
            }
        };

        monthlyIncomeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                strMonthlyIncome.set(parseLoanAmount(loanAmount.get()));
                eligibleAmt.set(calculateEMI(getTenure(), getROI()));
            }
        };

        eligibleAmt.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                emiAmtShow.set(!TextUtils.isEmpty(eligibleAmt.get()));
            }
        });
    }

    public void setMonthlyIncomePropertyChangedCallbacks(boolean addCallbacks) {
        if (addCallbacks) {
            loanAmount.addOnPropertyChangedCallback(monthlyIncomeCallback);
        } else {
            loanAmount.removeOnPropertyChangedCallback(monthlyIncomeCallback);
        }
    }

    public String parseMonthlyIncome() {
        String monthlyIncome = strMonthlyIncome.get();
        if (Utils.isDigitsOnly(monthlyIncome)) {
            Double amount = Double.parseDouble(monthlyIncome);
            if (amount < LOAN_AMOUNT_MIN || amount > LOAN_AMOUNT_MAX) {
                return "Loan Amount should be between "+LOAN_AMOUNT_MIN+"L & "+LOAN_AMOUNT_MAX+"L.";
            }
            setMonthlyIncome();
            strMonthlyIncome.set("₹ " + Utils.rupeeFormat(monthlyIncome));
            eligibleAmt.set(calculateEMI(getTenure(), getROI()));
        }
        return "";
    }

    private void setMonthlyIncome() {
        String monthlyIncome = strMonthlyIncome.get();
        if (Utils.isDigitsOnly(monthlyIncome)) {
            Integer amount = Integer.parseInt(monthlyIncome);
            amount = amount / LOAN_AMOUNT_MIN;
            this.loanAmount.set(amount);
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
            eligibleAmt.set(calculateEMI(getTenure(), roi));
        }
        return "";
    }

    private void setRateOfInterest() {
        String rateOfInterest = strRateOfIncome.get();
        if (Utils.isDouble(rateOfInterest)) {
            double roi = Double.parseDouble(rateOfInterest);
            roi = (int)roi - RATEOI_MIN;
            this.rateOfInterest.set( (int) roi );
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
            eligibleAmt.set(calculateEMI(getTenure(), getROI()));
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
