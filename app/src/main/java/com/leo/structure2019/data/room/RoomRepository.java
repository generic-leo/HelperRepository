package com.leo.structure2019.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.db.Applicant;
import com.leo.homeloan.data.pojo.db.Category;
import com.leo.homeloan.data.pojo.db.Company;
import com.leo.homeloan.data.pojo.db.Country;
import com.leo.homeloan.data.pojo.db.Disbursement;
import com.leo.homeloan.data.pojo.db.Lead;
import com.leo.homeloan.data.pojo.db.LoanApplication;
import com.leo.homeloan.data.pojo.db.LoanDetail;
import com.leo.homeloan.data.pojo.db.Occupation;
import com.leo.homeloan.data.pojo.db.Promotion;
import com.leo.homeloan.data.pojo.db.Qualification;
import com.leo.homeloan.data.pojo.db.Religion;
import com.leo.homeloan.data.pojo.db.Residence;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantContactDetail;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantDocument;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantFinancial;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantOccupation;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantPersonalDetail;

import java.util.List;

import timber.log.Timber;

public class RoomRepository implements Repository.RoomRepository {

    AppRoomDatabase database;

    public RoomRepository(Context appContext){
        database = AppRoomDatabase.getDB(appContext);
    }

    @Override
    public LiveData<List<Applicant>> getApplicants(int loanID) {
        return database.loanApplication().getAllApplicants(loanID);
    }

    @Override
    public LiveData<List<LoanApplication>> getAllLoanApplications(String loginID) {
        return database.loanApplication().getAll(loginID);
    }

    @Override
    public LiveData<List<LoanApplication>> getAllLoanApplications(String loginID, String level) {
        return database.loanApplication().getAll(loginID, level);
    }

    @Override
    public void deleteLoanApplications(String loginID, String[] ids) {
        database.loanApplication().deleteLoanApplications(loginID, ids);
    }

    @Override
    public void deleteLoanApplications(String loginID, String[] ids, String level) {
        database.loanApplication().deleteLoanApplications(loginID, ids, level);
    }

    @Override
    public LoanApplication getLoanApplication(String appID) {
        List<LoanApplication> loanApplications = database.loanApplication().getLoanApplication(appID);

        try{
            for (LoanApplication application: loanApplications){
                return application;
            }
        }
        catch (Exception e){
            Timber.e(e);
        }

        return null;
    }

    @Override
    public String addLoanApplication(LoanApplication application) {
        String applicationNo = application.applicationNo;
        database.loanApplication().insert(application);
        return applicationNo;
    }

    @Override
    public void updateLoanApplication(LoanApplication application) {
        database.loanApplication().insert(application);
    }

    @Override
    public LiveData<LoanAppJoinApplicant> getApplicantWithApp(int loanID) {
        return database.loanApplication().getDetailWithApplicants(loanID);
    }

    @Override
    public void insertPersonalDetails(ApplicantPersonalDetail personalDetail) {
        database.loanApplication().insertApplicantPersonalDetail(personalDetail);
    }

    @Override
    public void insertContactDetails(ApplicantContactDetail contactDetail) {
        database.loanApplication().insertAppplicantContactDetail(contactDetail);
    }

    @Override
    public void insertOccupationDetails(ApplicantOccupation occupation) {
        database.loanApplication().insertApplicantOccupation(occupation);
    }

    @Override
    public void insertFinancialDetails(ApplicantFinancial financial) {
        database.loanApplication().insertApplicantFinancial(financial);
    }

    @Override
    public LiveData<ApplicantPersonalDetail> getPersonalDetails(int applicantNumber) {
        List<ApplicantPersonalDetail> list = database.loanApplication().getPersonalDetail(applicantNumber);
        MutableLiveData<ApplicantPersonalDetail> liveData = new MutableLiveData<>();

        try{
            for (ApplicantPersonalDetail application: list){
                liveData.setValue(application);
                return liveData;
            }
        }
        catch (Exception e){
            Timber.e(e);
        }
        return liveData;
    }

    @Override
    public LiveData<ApplicantContactDetail> getContactDetails(int applicantNumber) {
        List<ApplicantContactDetail> list = database.loanApplication().getContactDetail(applicantNumber);
        MutableLiveData<ApplicantContactDetail> liveData = new MutableLiveData<>();

        try{
            for (ApplicantContactDetail application: list){
                liveData.setValue(application);
                return liveData;
            }
        }
        catch (Exception e){
            Timber.e(e);
        }
        return liveData;
    }

    @Override
    public LiveData<ApplicantOccupation> getOccupationDetails(int applicantNumber) {
        List<ApplicantOccupation> list = database.loanApplication().getOccupationDetail(applicantNumber);
        MutableLiveData<ApplicantOccupation> liveData = new MutableLiveData<>();

        try{
            for (ApplicantOccupation application: list){
                liveData.setValue(application);
                return liveData;
            }
        }
        catch (Exception e){
            Timber.e(e);
        }
        return liveData;
    }

    @Override
    public LiveData<ApplicantFinancial> getFinancialDetails(int applicantNumber) {
        return database.loanApplication().getFinancialDetail(applicantNumber);
    }

    @Override
    public int createApplicant(int loanID) {
        Applicant applicant = new Applicant();
        applicant.setApplicationNo(loanID);
        int rowID = (int)database.loanApplication().insert(applicant);
        return rowID;
    }

    @Override
    public int insertLoanApplication(LoanApplication application) {
        int rowID = (int)database.loanApplication().insert(application);
        return rowID;
    }

    @Override
    public void updateApplicant(Applicant applicant) {
        database.loanApplication().insertApplicant(applicant);
    }

    @Override
    public void deleteApplicants(int loanID, int[] applicants) {
        if (applicants != null)
            database.loanApplication().deleteApplicants(loanID, applicants);
    }

    @Override
    public LoanApplication getLoanApplication(int loanID) {
        List<LoanApplication> applications = database.loanApplication().getLoanApplication(loanID);

        if(applications != null && applications.size() > 0 ){
            return applications.get(0);
        }

        return null;
    }

    @Override
    public ApplicantPersonalDetail getPersonalDetailObject(int applicantNumber) {
        List<ApplicantPersonalDetail> applications = database.loanApplication().getPersonalDetailObject(applicantNumber);

        if(applications != null && applications.size() > 0 ){
            return applications.get(0);
        }

        return null;
    }

    @Override
    public ApplicantContactDetail getContactDetailObject(int applicantNumber) {
        List<ApplicantContactDetail> applications = database.loanApplication().getContactDetailObject(applicantNumber);

        if(applications != null && applications.size() > 0 ){
            return applications.get(0);
        }

        return null;
    }

    @Override
    public ApplicantOccupation getOccupationDetailObject(int applicantNumber) {
        List<ApplicantOccupation> applications = database.loanApplication().getOccupationDetailObject(applicantNumber);

        if(applications != null && applications.size() > 0 ){
            return applications.get(0);
        }

        return null;
    }

    @Override
    public ApplicantFinancial getFinancialDetailObject(int applicantNumber) {
        List<ApplicantFinancial> applications = database.loanApplication().getFinancialDetailObject(applicantNumber);

        if(applications != null && applications.size() > 0 ){
            return applications.get(0);
        }

        return null;
    }

    @Override
    public LoanAppJoinApplicant getLoanApplicationDetails(String appFormID) {
        return database.loanApplication().getLoanApplicationDetails(appFormID);
    }

    @Override
    public Disbursement getDisbursementObject(int applicationNo) {
        return database.loanApplication().getDisbursementObject(applicationNo);
    }

    @Override
    public void insertDisbursement(Disbursement disbursement) {
        database.loanApplication().insertDisbursement(disbursement);
    }

    @Override
    public LoanAppJoinApplicant getDetailWithApplicantsObject(int loanID) {
        return database.loanApplication().getDetailWithApplicantsObject(loanID);
    }

    @Override
    public LoanDetail getLoanDetail(int loanID) {
        try{
            for (LoanDetail application: database.loanApplication().getLoanDetail(loanID)){
                return application;
            }
        }
        catch (Exception e){
            Timber.e(e);
        }

        return null;
    }

    @Override
    public LiveData<List<Lead>> getAllLeads(String loginID) {
        return database.loanApplication().getAllLeads(loginID);
    }

    @Override
    public void insertLoanDetail(LoanDetail loanDetail) {
        database.loanApplication().insertLoanDetail(loanDetail);
    }

    @Override
    public void insertDocument(ApplicantDocument document) {
        database.loanApplication().insertDocument(document);
    }

    @Override
    public void deleteDocuments(int applicantID) {
        database.loanApplication().deleteDocuments(applicantID);
    }

    @Override
    public LiveData<List<ApplicantDocument>> getAllDocuments(int applicantID) {
        return database.loanApplication().getAllDocuments(applicantID);
    }

    @Override
    public void updateLoanApplication(int loanID, String amount) {
        database.loanApplication().updateLoanApplication(loanID, amount);
    }

    // region [ Master's ]
    @Override
    public LiveData<List<Occupation>> getAllOccupation() {
       return database.loanApplication().getAllOccupation();
       // return null;
    }

    @Override
    public LiveData<List<Religion>> getAllReligion() {
        return database.loanApplication().getAllReligion();
        // return null;
    }

    @Override
    public LiveData<List<Company>> getAllCompany() {
        return database.loanApplication().getAllCompany();
        // return null;
    }

    @Override
    public LiveData<List<Residence>> getAllResidences() {
        return database.loanApplication().getAllResidences();
        // return null;
    }

    @Override
    public LiveData<List<Qualification>> getAllQualification() {
        return database.loanApplication().getAllQualification();
        // return null;
    }

    @Override
    public LiveData<List<Category>> getAllCategory() {
        return database.loanApplication().getAllCategory();
        // return null;
    }

    @Override
    public LiveData<List<Occupation>> getAllOccupation(String Occupation_id) {
        return database.loanApplication().getAllOccupation(Occupation_id);
        // return null;
    }

    @Override
    public LiveData<List<Religion>> getAllReligion(String religion_id) {
        return database.loanApplication().getAllReligion(religion_id);
        // return null;
    }

    @Override
    public LiveData<List<Company>> getAllCompany(String company_id) {
        return database.loanApplication().getAllCompany(company_id);
        // return null;
    }

    @Override
    public LiveData<List<Residence>> getAllResidences(String residence_status_id) {
        return database.loanApplication().getAllResidences(residence_status_id);
        // return null;
    }

    @Override
    public LiveData<List<Qualification>> getAllQualification(String qualification_id) {
        return database.loanApplication().getAllQualification(qualification_id);
        // return null;
    }

    @Override
    public LiveData<List<Category>> getAllCategory(String category_id) {
        return database.loanApplication().getAllCategory(category_id);
        // return null;
    }

    @Override
    public void insertOccupation(Occupation occupation) {
        database.loanApplication().insertOccupation(occupation);
    }

    @Override
    public void insertReligion(Religion religion) {
        database.loanApplication().insertReligion(religion);
    }

    @Override
    public void insertCompany(Company company) {
        database.loanApplication().insertCompany(company);
    }

    @Override
    public void insertResidence(Residence residence) {
        database.loanApplication().insertResidence(residence);
    }

    @Override
    public void insertQualification(Qualification qualification) {
        database.loanApplication().insertQualification(qualification);
    }

    @Override
    public void insertCategory(Category category) {
        database.loanApplication().insertCategory(category);
    }

    @Override
    public LiveData<List<Country>> getAllCountry() {
        return database.loanApplication().getAllCountry();
    }

    @Override
    public void insertCountry(Country country) {
        database.loanApplication().insertCountry(country);
    }

    @Override
    public void insertLead(Lead lead) {
        database.loanApplication().insertLead(lead);
    }

    @Override
    public List<ApplicantDocument> getAllDocs(int applicantID) {
        return database.loanApplication().getAllDocs(applicantID);
    }

    @Override
    public LiveData<List<Promotion>> getAllPromotion() {
        return database.loanApplication().getAllPromotion();
    }

    @Override
    public void insertPromotion(Promotion promotion) {
        database.loanApplication().insertPromotion(promotion);
    }

    @Override
    public void deleteOccupation() {
        database.loanApplication().deleteOccupation();
    }

    @Override
    public void deleteReligion() {
        database.loanApplication().deleteReligion();
    }

    @Override
    public void deleteCompany() {
        database.loanApplication().deleteCompany();
    }

    @Override
    public void deleteResidence() {
        database.loanApplication().deleteResidence();
    }

    @Override
    public void deleteQualification() {
        database.loanApplication().deleteQualification();
    }

    @Override
    public void deleteCategory() {
        database.loanApplication().deleteCategory();
    }

    @Override
    public void deleteCountry() {
        database.loanApplication().deleteCountry();
    }

    @Override
    public void deletePromotion() {
        database.loanApplication().deletePromotion();
    }

    // endregion
}
