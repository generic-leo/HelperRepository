package com.leo.structure2019.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

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

@Dao
public abstract class LoanApplicationDao {

    @Query("SELECT * FROM loan_applications")
    public abstract LiveData<List<LoanApplication>> getAll();

    @Query("SELECT * FROM loan_applications WHERE login_id = :loginID  ORDER BY application_no DESC")
    public abstract LiveData<List<LoanApplication>> getAll(String loginID);

    @Query("SELECT * FROM loan_applications WHERE login_id = :loginID AND level = :level")
    public abstract LiveData<List<LoanApplication>> getAll(String loginID, String level);

    @Transaction
    @Query("SELECT * FROM loan_applications WHERE loan_id = :loanID")
    public abstract LiveData<LoanApplication> getDetail(int loanID);

    @Query("SELECT * FROM loan_applications WHERE loan_id = :loanID")
    public abstract List<LoanApplication> getLoanApplication(int loanID);

    @Query("SELECT * FROM loan_applications WHERE application_no = :appID")
    public abstract List<LoanApplication> getLoanApplication(String appID);

    @Transaction
    @Query("SELECT * FROM loan_details WHERE application_no = :loanID")
    public abstract List<LoanDetail> getLoanDetail(int loanID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertLoanDetail(LoanDetail loanDetail);

    @Query("UPDATE loan_applications SET amount = :amount WHERE loan_id = :loanID")
    public abstract void updateLoanApplication(int loanID, String amount );

    @Transaction
    @Query("SELECT * FROM loan_applications WHERE loan_id = :loanID")
    public abstract LiveData<LoanAppJoinApplicant> getDetailWithApplicants(int loanID);

    @Transaction
    @Query("SELECT * FROM loan_applications WHERE loan_id = :loanID")
    public abstract LoanAppJoinApplicant getDetailWithApplicantsObject(int loanID);

    @Transaction
    @Query("SELECT * FROM loan_applications WHERE application_no = :appFormID")
    public abstract LoanAppJoinApplicant getLoanApplicationDetails(String appFormID);

    @Query("SELECT * FROM applicant WHERE application_no = :loanID")
    public abstract LiveData<List<Applicant>> getAllApplicants(int loanID);

    @Query("SELECT * FROM applicant")
    public abstract LiveData<List<Applicant>> getAllApplicants();

    @Query("SELECT * FROM applicant WHERE applicant_id = :applicantID LIMIT 1")
    public abstract LiveData<Applicant> getApplicant(int applicantID);

    public void insertApplicant(LoanApplication application, Applicant applicant){
        applicant.setApplicationNo(application.loanID);
        insert(applicant);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(LoanApplication loanApplication);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Applicant applicant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertApplicantPersonalDetail(ApplicantPersonalDetail personalDetail);

    @Query("SELECT * FROM personal_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public List<ApplicantPersonalDetail> getPersonalDetail(int applicantNumber);

    @Query("SELECT * FROM personal_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public List<ApplicantPersonalDetail> getPersonalDetailObject(int applicantNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertAppplicantContactDetail(ApplicantContactDetail contactDetail);

    @Query("SELECT * FROM contact_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public List<ApplicantContactDetail> getContactDetail(int applicantNumber);

    @Query("SELECT * FROM contact_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public List<ApplicantContactDetail> getContactDetailObject(int applicantNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertApplicantOccupation(ApplicantOccupation applicantOccupation);

    @Query("SELECT * FROM occupation_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public List<ApplicantOccupation> getOccupationDetail(int applicantNumber);

    @Query("SELECT * FROM occupation_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public List<ApplicantOccupation> getOccupationDetailObject(int applicantNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertApplicantFinancial(ApplicantFinancial applicantFinancial);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertApplicant(Applicant applicant);

    @Query("SELECT * FROM lead WHERE login_id = :loginID")
    abstract public LiveData<List<Lead>> getAllLeads(String loginID);

    @Query("SELECT * FROM financial_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public LiveData<ApplicantFinancial> getFinancialDetail(int applicantNumber);

    @Query("SELECT * FROM financial_detail WHERE applicant_id = :applicantNumber LIMIT 1")
    abstract public List<ApplicantFinancial> getFinancialDetailObject(int applicantNumber);

    @Query("SELECT * FROM disbursement WHERE application_no = :applicationNo LIMIT 1")
    abstract public Disbursement getDisbursementObject(int applicationNo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertDisbursement(Disbursement disbursement);

    // Upload Document
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertDocument(ApplicantDocument document);

    @Query("DELETE FROM applicant_document WHERE applicant_id=:applicantID")
    abstract public void deleteDocuments(int applicantID);

    @Query("SELECT * FROM applicant_document WHERE applicant_id=:applicantID")
    abstract public LiveData<List<ApplicantDocument>> getAllDocuments(int applicantID);

    @Query("SELECT * FROM applicant_document WHERE applicant_id=:applicantID")
    abstract public List<ApplicantDocument> getAllDocs(int applicantID);

    // region [ Masters ]


    @Query("SELECT * FROM occupation WHERE Occupation_id = :Occupation_id")
    abstract public LiveData<List<Occupation>> getAllOccupation(String Occupation_id);

    @Query("SELECT * FROM religion WHERE religion_id = :religion_id")
    abstract public LiveData<List<Religion>> getAllReligion(String religion_id);

    @Query("SELECT * FROM company WHERE company_id =:company_id")
    abstract public LiveData<List<Company>> getAllCompany(String company_id);

    @Query("SELECT * FROM residence WHERE residence_status_id =:residence_status_id")
    abstract public LiveData<List<Residence>> getAllResidences(String residence_status_id);

    @Query("SELECT * FROM qualification WHERE qualification_id=:qualification_id")
    abstract public LiveData<List<Qualification>> getAllQualification(String qualification_id);

    @Query("SELECT * FROM category WHERE category_id=:category_id")
    abstract public LiveData<List<Category>> getAllCategory(String category_id);

    @Query("SELECT * FROM occupation")
    abstract public LiveData<List<Occupation>> getAllOccupation();

    @Query("SELECT * FROM religion")
    abstract public LiveData<List<Religion>> getAllReligion();

    @Query("SELECT * FROM company")
    abstract public LiveData<List<Company>> getAllCompany();

    @Query("SELECT * FROM residence")
    abstract public LiveData<List<Residence>> getAllResidences();

    @Query("SELECT * FROM qualification")
    abstract public LiveData<List<Qualification>> getAllQualification();

    @Query("SELECT * FROM category")
    abstract public LiveData<List<Category>> getAllCategory();

    @Query("SELECT * FROM country")
    abstract public LiveData<List<Country>> getAllCountry();

    @Query("SELECT * FROM promotion")
    abstract public LiveData<List<Promotion>> getAllPromotion();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertOccupation(Occupation occupation);

    @Query("DELETE FROM occupation")
    abstract public void deleteOccupation();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertReligion(Religion religion);

    @Query("DELETE FROM religion")
    abstract public void deleteReligion();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertCompany(Company company);

    @Query("DELETE FROM company")
    abstract public void deleteCompany();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertResidence(Residence residence);

    @Query("DELETE FROM residence")
    abstract public void deleteResidence();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertQualification(Qualification qualification);

    @Query("DELETE FROM qualification")
    abstract public void deleteQualification();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertCategory(Category category);

    @Query("DELETE FROM category")
    abstract public void deleteCategory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertCountry(Country country);

    @Query("DELETE FROM country")
    abstract public void deleteCountry();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertLead(Lead lead);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertPromotion(Promotion promotion);

    @Query("DELETE FROM promotion")
    abstract public void deletePromotion();

    // endregion

    // Delete Queries
    @Query("DELETE FROM loan_applications WHERE login_id ==:loginID AND application_no NOT IN (:ids)")
    abstract public void deleteLoanApplications(String loginID, String[] ids);

    @Query("DELETE FROM loan_applications WHERE login_id ==:loginID AND level= :level AND application_no NOT IN (:ids)")
    abstract public void deleteLoanApplications(String loginID, String[] ids, String level);

    @Query("DELETE FROM applicant WHERE  application_no ==:loanID AND applicant_id NOT IN (:applicants)")
    abstract public void deleteApplicants(int loanID, int[] applicants);
}
