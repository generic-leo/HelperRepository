package com.leo.structure2019.data;

import android.arch.lifecycle.LiveData;

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
import com.leo.homeloan.data.pojo.remote.APIResponse;
import com.leo.homeloan.data.pojo.remote.ApplicantList;
import com.leo.homeloan.data.pojo.remote.ContactDetailRemote;
import com.leo.homeloan.data.pojo.remote.CreateLeadRequest;
import com.leo.homeloan.data.pojo.remote.DashboardItem;
import com.leo.homeloan.data.pojo.remote.EmploymentDetailRemote;
import com.leo.homeloan.data.pojo.remote.LeadRemote;
import com.leo.homeloan.data.pojo.remote.LoanAppItem;
import com.leo.homeloan.data.pojo.remote.LoanDetailRequest;
import com.leo.homeloan.data.pojo.remote.LoginRemote;
import com.leo.homeloan.data.pojo.remote.MastersCity;
import com.leo.homeloan.data.pojo.remote.MastersCityState;
import com.leo.homeloan.data.pojo.remote.MastersRemote;
import com.leo.homeloan.data.pojo.remote.MastersState;
import com.leo.homeloan.data.pojo.remote.MastersZip;
import com.leo.homeloan.data.pojo.remote.PerformanceRemote;
import com.leo.homeloan.data.pojo.remote.PersonalDetailRemote;
import com.leo.homeloan.data.pojo.remote.SaveRemote;
import com.leo.homeloan.data.room.LoanAppJoinApplicant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Repository {

    interface RemoteRepository{
        void login(int type, String username, String password, RemoteCallback<LoginRemote> responseListener);
        void registerMobile(int type, String action, String userId, RemoteCallback<String> remoteCallback);
        void newLoanApplication(String login, RemoteCallback<String> remoteCallback);
        void dashboard(String userID, String login_type, String emp_id, String level, RemoteCallback<ArrayList<DashboardItem>> remoteCallback);
        void applicant(String appFormID, RemoteCallback<ApplicantList> remoteCallback);
        void savePersonal(String appFormID, String primary, boolean applicantExists, int applicantID, PersonalDetailRemote personalDetailRemote, RemoteCallback<SaveRemote> remoteCallback);
        void saveContact(String appFormID, String userType, String primary, boolean applicantExists, int applicantID, ContactDetailRemote contactDetailRemote, RemoteCallback<SaveRemote> remoteCallback);
        void saveOccupation(String appFormID, String primary, boolean applicantExists, int applicantID, EmploymentDetailRemote employmentDetailRemote, RemoteCallback<SaveRemote> remoteCallback);
        void saveDocuments(String cacheDirectory, String appFormID, String primary, boolean applicantExists, int applicantID, HashMap<String, String> documents, RemoteCallback<SaveRemote> remoteCallback);
        void createApplicant(String appFormID, RemoteCallback<SaveRemote> remoteCallback);

        void createLead(int userType, String userTypeValue, CreateLeadRequest leadRequest, RemoteCallback<String> remoteCallback);
        void saveLoanDetail(String appID, LoanDetailRequest loanDetailRequest, RemoteCallback<String> remoteCallback);
        void fetchLeads(String type, String keyID, String empID, RemoteCallback<ArrayList<LeadRemote>> remoteCallback);
        void UpdateLeadStatus(int leadid, String status, RemoteCallback<String> remoteCallback);
        void acceptReview(String appID, RemoteCallback<String> remoteCallback);
        void updateStatus(String appID, String action, String status, RemoteCallback<String> remoteCallback);
        void downloadMaster(RemoteCallback<MastersRemote> remoteRemoteCallback);

        void stateMaster(int countryID, RemoteCallback<MastersState> remoteCallback);
        void cityMaster(int stateID, RemoteCallback<MastersCity> remoteCallback);
        void zipcodeMaster(int cityID, RemoteCallback<MastersZip> remoteCallback);
        void getCityState(int countryID, String pincode, RemoteCallback<MastersCityState> remoteCallback);

        void submitApplication(String appFormID, RemoteCallback<String> remoteCallback);
        void performanceReport(String userID, String loginID, RemoteCallback<PerformanceRemote> remoteCallback);
        void verifyPan(String panCard, String appID, String loanID, boolean applicantExists, RemoteCallback<String> remoteCallback);
        void verifyAdhaar(String adhaarCard, RemoteCallback<APIResponse> remoteCallback);

        void verifyOTP(String mobNo, String otp, RemoteCallback<String> remoteCallback);
        void sendOTP(String mobNo, String length, RemoteCallback<APIResponse> remoteCallback);
        void setMpin(String mobNo, String confirmMpin, String Mpin, RemoteCallback<String> remoteCallback);

        void searchApplicant(String appFormNo, RemoteCallback<ArrayList<LoanAppItem>> remoteCallback);
        void sendOTPToCustomer(String appFormNo, String employeeId, String len, RemoteCallback<String> remoteCallback);
        void setCustomerfromConformation(String appFormNo, String employeeId, String otp, RemoteCallback<String> remoteCallback);
        void generateEasyPayURL(String appFormNo, String deviceInfo, String paymentMode, RemoteCallback<String> remoteCallback);
        void logout(String session_id, String used_id, RemoteCallback<String> remoteCallback);
    }

    interface RoomRepository{
        String ADD_APPLICANT = "Add Applicant";

        void updateLoanApplication(int loanID, String amount);

        // region [ Applicant ]
        LiveData<List<Applicant>> getApplicants(int loanID);
        void updateApplicant(Applicant applicant);
        int createApplicant(int loanID);
        void deleteApplicants(int loanID, int[] applicants);
        // endregion

        // region [ Applicant Personal; Contact; Occupation; Financial ]
        void insertPersonalDetails(ApplicantPersonalDetail personalDetail);
        LiveData<ApplicantPersonalDetail> getPersonalDetails(int applicantNumber);
        ApplicantPersonalDetail getPersonalDetailObject(int applicantNumber);
        void insertContactDetails(ApplicantContactDetail contactDetail);
        LiveData<ApplicantContactDetail> getContactDetails(int applicantNumber);
        ApplicantContactDetail getContactDetailObject(int applicantNumber);
        void insertOccupationDetails(ApplicantOccupation occupation);
        LiveData<ApplicantOccupation> getOccupationDetails(int applicantNumber);
        ApplicantOccupation getOccupationDetailObject(int applicantNumber);
        void insertFinancialDetails(ApplicantFinancial financial);
        LiveData<ApplicantFinancial> getFinancialDetails(int applicantNumber);
        ApplicantFinancial getFinancialDetailObject(int applicantNumber);
        // endregion

        // region [ Loan Applications ]
        LoanApplication getLoanApplication(int loanID);
        LoanApplication getLoanApplication(String appID);
        LiveData<List<LoanApplication>> getAllLoanApplications(String loginID);
        LiveData<List<LoanApplication>> getAllLoanApplications(String loginID, String level);
        String addLoanApplication(LoanApplication application);
        void updateLoanApplication(LoanApplication application);
        int insertLoanApplication(LoanApplication application);
        LoanAppJoinApplicant getDetailWithApplicantsObject(int loanID);
        LoanAppJoinApplicant getLoanApplicationDetails(String appFormID);
        void deleteLoanApplications(String loginID, String[] ids);
        void deleteLoanApplications(String loginID, String[] ids, String level);
        // endregion

        // region [ MISC ]
        LiveData<LoanAppJoinApplicant> getApplicantWithApp(int loanID);
        // endregion

        // region [ Disbursement ]
        Disbursement getDisbursementObject(int applicationNo);
        void insertDisbursement(Disbursement disbursement);
        // endregion

        // region
        LoanDetail getLoanDetail(int loanID);
        void insertLoanDetail(LoanDetail loanDetail);
        // endregion

        // region [ Documents ]
        void insertDocument(ApplicantDocument document);
        void deleteDocuments(int applicantID);
        LiveData<List<ApplicantDocument>> getAllDocuments(int applicantID);
        List<ApplicantDocument> getAllDocs(int applicantID);
        // endregion

        // region [Leads]
        LiveData<List<Lead>> getAllLeads(String loginID);
        // endregion

        // region [ Masters ]
        LiveData<List<Occupation>> getAllOccupation();
        LiveData<List<Religion>> getAllReligion();
        LiveData<List<Company>> getAllCompany();
        LiveData<List<Residence>> getAllResidences();
        LiveData<List<Qualification>> getAllQualification();
        LiveData<List<Category>> getAllCategory();
        LiveData<List<Country>> getAllCountry();
        LiveData<List<Promotion>> getAllPromotion();

        LiveData<List<Occupation>> getAllOccupation(String Occupation_id);
        LiveData<List<Religion>> getAllReligion(String religion_id);
        LiveData<List<Company>> getAllCompany(String company_id);
        LiveData<List<Residence>> getAllResidences(String residence_status_id);
        LiveData<List<Qualification>> getAllQualification(String qualification_id);
        LiveData<List<Category>> getAllCategory(String category_id);

        void insertOccupation(Occupation occupation);
        void insertReligion(Religion religion);
        void insertCompany(Company company);
        void insertResidence(Residence residence);
        void insertQualification(Qualification qualification);
        void insertCategory(Category category);
        void insertCountry(Country category);
        void insertLead(Lead lead);
        void insertPromotion(Promotion promotion);

        void deleteOccupation();
        void deleteReligion();
        void deleteCompany();
        void deleteResidence();
        void deleteQualification();
        void deleteCategory();
        void deleteCountry();
        void deletePromotion();

        // endregion
    }

    interface RemoteCallback<T> {
        void onStart();
        void onSuccess(T response);
        void onFailure(String error_code, String error_message);
    }
}
