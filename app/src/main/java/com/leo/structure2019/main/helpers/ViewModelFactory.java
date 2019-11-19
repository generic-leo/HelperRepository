package com.leo.structure2019.main.helpers;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.leo.homeloan.appform.applicant.AddApplicantViewModel;
import com.leo.homeloan.appform.contact.ContactFormViewModel;
import com.leo.homeloan.appform.emlpoyeement.EmploymentFormViewModel;
import com.leo.homeloan.appform.financial.FinancialFormViewModel;
import com.leo.homeloan.appform.personal.PersonalFormViewModel;
import com.leo.homeloan.calculator.CalculatorModel;
import com.leo.homeloan.calculator.eligibility.EligibilityCalculatorModel;
import com.leo.homeloan.calculator.emi.EmiCalculatorModel;
import com.leo.homeloan.createlead.CreateLeadViewModel;
import com.leo.homeloan.loan.application.MyLoanAppEmployeeModel;
import com.leo.homeloan.loan.application.MyLoanApplicationViewModel;
import com.leo.homeloan.loan.application.updates.DRFViewModel;
import com.leo.homeloan.loan.application.updates.SanctionListViewModel;
import com.leo.homeloan.loan.application.updates.UpdateDashboardViewModel;
import com.leo.homeloan.login.CustomerLoginViewModel;
import com.leo.homeloan.newuser.RegisterUserViewModel;
import com.leo.homeloan.setmpin.SetMPinViewModel;
import com.leo.homeloan.verifyotp.OTPVerificationViewModel;


public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;
    private Application application;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    private ViewModelFactory(Application application){
        this.application = application;
    }

    /*
     * Copied from todo-mvvm-live
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EligibilityCalculatorModel.class)) {
            return (T) new EligibilityCalculatorModel(application);
        }

        if (modelClass.isAssignableFrom(EmiCalculatorModel.class)) {
            return (T) new EmiCalculatorModel(application);
        }

        if (modelClass.isAssignableFrom(CalculatorModel.class)) {
            return (T) new CalculatorModel(application);
        }

        if (modelClass.isAssignableFrom(CreateLeadViewModel.class)) {
            return (T) new CreateLeadViewModel(application);
        }

        if (modelClass.isAssignableFrom(CustomerLoginViewModel.class)) {
            return (T) new CustomerLoginViewModel(application);
        }

        if (modelClass.isAssignableFrom(CreateLeadViewModel.class)) {
            return (T) new CreateLeadViewModel(application);
        }

        if (modelClass.isAssignableFrom(MyLoanApplicationViewModel.class)) {
            return (T) new MyLoanApplicationViewModel(application);
        }

        if (modelClass.isAssignableFrom(AddApplicantViewModel.class)) {
            return (T) new AddApplicantViewModel(application);
        }

        if (modelClass.isAssignableFrom(PersonalFormViewModel.class)) {
            return (T) new PersonalFormViewModel(application);
        }

        if (modelClass.isAssignableFrom(EmploymentFormViewModel.class)) {
            return (T) new EmploymentFormViewModel(application);
        }

        if (modelClass.isAssignableFrom(FinancialFormViewModel.class)) {
            return (T) new FinancialFormViewModel(application);
        }

        if (modelClass.isAssignableFrom(ContactFormViewModel.class)) {
            return (T) new ContactFormViewModel(application);
        }

        if (modelClass.isAssignableFrom(RegisterUserViewModel.class)) {
            return (T) new RegisterUserViewModel(application);
        }

        if (modelClass.isAssignableFrom(OTPVerificationViewModel.class)) {
            return (T) new OTPVerificationViewModel(application);
        }

        if (modelClass.isAssignableFrom(SetMPinViewModel.class)) {
            return (T) new SetMPinViewModel(application);
        }

        if (modelClass.isAssignableFrom(MyLoanAppEmployeeModel.class)) {
            return (T) new MyLoanAppEmployeeModel(application);
        }

        if (modelClass.isAssignableFrom(DRFViewModel.class)) {
            return (T) new DRFViewModel(application);
        }

        if (modelClass.isAssignableFrom(SanctionListViewModel.class)) {
            return (T) new SanctionListViewModel(application);
        }

        if (modelClass.isAssignableFrom(UpdateDashboardViewModel.class)) {
            return (T) new UpdateDashboardViewModel(application );
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
