package com.leo.structure2019.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.db.Applicant;
import com.leo.homeloan.data.pojo.db.LoanApplication;

import java.util.ArrayList;
import java.util.List;

abstract public class FakeRepository implements Repository {

    private Context applicationContext;
    private MutableLiveData<List<Applicant>> applicants = new MutableLiveData<>();
    private MutableLiveData<List<LoanApplication>> loanApplications = new MutableLiveData<>();

    public FakeRepository(Context applicationContext){
        this.applicationContext = applicationContext;
        applicants.setValue(getDummyList());
        loanApplications.setValue(getDummyLoanList());
    }

    // region [ Applicant ]

    public LiveData<List<Applicant>> getApplicants(int loanID){
        return applicants;
    }

    public void addApplicant(String name) {
        List<Applicant> list = applicants.getValue();

        // Remove Add Applicant
        if(list.size() > 0)
            list.remove(list.size() - 1);

        list.add(new Applicant().setName(name));

        // Add Applicant
        list.add(new Applicant().setName(Repository.RoomRepository.ADD_APPLICANT));

        applicants.setValue( list );
    }

    public void removeAllApplicant()
    {
        applicants.setValue( new  ArrayList<Applicant>() );
    }

    public void removeLastApplicant(){
        if(applicants.getValue().size() > 1 ) {
            applicants.getValue().remove(applicants.getValue().size() - 2);
            applicants.setValue(applicants.getValue());
        }
    }

    public static ArrayList<Applicant> getDummyList(){
        ArrayList<Applicant> list = new ArrayList<>();
        list.add(new Applicant().setName(Repository.RoomRepository.ADD_APPLICANT));
        return list;
    }

    // endregion

    // region [ Loan Applications ]

    public MutableLiveData<List<LoanApplication>> getAllLoanApplications(){
        return loanApplications;
    }

    public String addLoanApplication(LoanApplication application){
        List<LoanApplication> applications = loanApplications.getValue();
        applications.add(application);
        loanApplications.setValue(applications);
        return application.applicationNo;
    }

    public LoanApplication getLoanApplication(String appNo){
        List<LoanApplication> applications = loanApplications.getValue();

        for(int i = 0 ; i < applications.size(); i++){
            LoanApplication app = applications.get(i);

            // Only, if the things match return that loan application
            if(app.applicationNo.equalsIgnoreCase(appNo)){
                return app;
            }
        }

        // If nothing is found
        return new LoanApplication();
    }

    public void updateLoanApplication(LoanApplication application){
        List<LoanApplication> applications = loanApplications.getValue();

        for(int i = 0 ; i < applications.size(); i++){
            LoanApplication app = applications.get(i);

            // Only, if the things match update that loan application
            if(app.applicationNo.equalsIgnoreCase(application.applicationNo)){
                applications.set(i, application);
            }
        }

        // In the end it doesn't even matter
        loanApplications.setValue(applications);
    }

    public void removeLastLoanApplication(){
        List<LoanApplication> applications = loanApplications.getValue();

        if(applications.size() > 0 )
            applications.remove(applications.size() - 1);

        loanApplications.setValue(applications);
    }

    public static ArrayList<LoanApplication> getDummyLoanList(){
        ArrayList<LoanApplication> applications = new ArrayList<>();

        /*
        applications.add(new LoanApplication()
                .amount = "50,00,000"
                .setStatus("Approved")
                .setTime("10th Jan 2018"));

        applications.add(new LoanApplication()
                .setAmount("60,00,000")
                .setStatus("Approved")
                .setTime("12th Jan 2018"));

        */

        return applications;
    }

    // endregion
}
