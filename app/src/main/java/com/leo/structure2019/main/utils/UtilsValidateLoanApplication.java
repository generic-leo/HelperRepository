package com.leo.structure2019.main.utils;

import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;

import com.leo.homeloan.appform.document.DocumentList;
import com.leo.homeloan.appform.emlpoyeement.EmploymentFormViewModel;
import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.db.Applicant;
import com.leo.homeloan.data.pojo.db.LoanApplication;
import com.leo.homeloan.data.pojo.db.LoanDetail;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantContactDetail;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantDocument;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantOccupation;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantPersonalDetail;
import com.leo.homeloan.data.room.LoanAppJoinApplicant;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class UtilsValidateLoanApplication {

    public final static int NavigateToApplicant = -100;
    public final static int NavigateToLoanApplication = -300;
    public final static int NavigateToNothing = -400;

    public static Pair<String, Integer> validateApplication(String appID, Repository.RoomRepository repository){

        try{

            Integer noOfApplicantsGeneral = new Integer(0);

            LoanAppJoinApplicant loanAppJoinApplicant = repository.getLoanApplicationDetails(appID);
            LoanApplication loanApplication = loanAppJoinApplicant.loanApplication;
            List<Applicant> applicants = loanAppJoinApplicant.applicantList;

            if (applicants == null)
                return new Pair<>("No Applicants Are Added For This Loan Application.", NavigateToApplicant);

            if (applicants.size() == 0)
                return new Pair<>("No Applicants Are Added For This Loan Application.", NavigateToApplicant);

            for (int i = 0 ; i < applicants.size() ; i++){
                Applicant applicant = applicants.get(i);
                String applicantValidationError = validateApplicant(i+1, applicant, noOfApplicantsGeneral, repository);

                // If there is any error in the Applicant Data
                if(applicantValidationError.length() > 0)
                    return new Pair<>(applicantValidationError, applicant.applicantID);
            }

            LoanDetail loanDetail = repository.getLoanDetail(loanApplication.loanID);
            String validationLoanDetail = validateLoanDetail(loanDetail);

            if (validationLoanDetail.length() > 0)
                return new Pair<>(validationLoanDetail, NavigateToLoanApplication);

        }catch (Exception e){
            Timber.e(e);
            return new Pair<>("Failed To Validate : " + e.getMessage(), NavigateToNothing);
        }

        return new Pair<>("", NavigateToNothing);
    }

    private static String validateApplicant(int index, Applicant applicant, Integer noOfApplicantsGeneral, Repository.RoomRepository repository){

        String validationError;

        ApplicantPersonalDetail applicantPersonalDetail = repository.getPersonalDetailObject(applicant.applicantID);

        validationError = validatePersonal(index, applicantPersonalDetail);
        if(validationError.length() > 0)
            return validationError;

        ApplicantContactDetail applicantContactDetail = repository.getContactDetailObject(applicant.applicantID);

        validationError = validateContact(index, applicantContactDetail);
        if(validationError.length() > 0)
            return validationError;

        ApplicantOccupation applicantOccupation = repository.getOccupationDetailObject(applicant.applicantID);

        validationError = validateOccupation(index, applicantOccupation, noOfApplicantsGeneral);
        if (validationError.length() > 0)
            return validationError;

        validationError = validateDocument(index, applicantOccupation.natureOfEmployment, applicant, repository);
        if (validationError.length() > 0)
            return validationError;


        return "";
    }

    private static String validatePersonal(int index, ApplicantPersonalDetail pd){
        String applicantNo = "Applicant "+index + "\n";
        String prefix = applicantNo;

        if (pd == null)
            return prefix +" Personal Details Are Incomplete.";

        if (TextUtils.isEmpty(pd.panNo) || pd.panNo.length() != 10) {
            prefix += " PAN No";
        }
        else if(TextUtils.isEmpty(pd.aadharNo) || pd.aadharNo.length() != 12) {
            prefix += " Aadhar No";
        }
        else if (TextUtils.isEmpty(pd.firstName)) {
            prefix += " First Name";
        }

        /*
        else if (TextUtils.isEmpty(pd.middleName)) {
            prefix += " Middle Name";
        }*/

        else if (TextUtils.isEmpty(pd.lastName)) {
            prefix += " Last Name";
        }
        else if (TextUtils.isEmpty(pd.noOfDependantes_child)&& TextUtils.isEmpty(pd.noOfDependantes_other) ){
            prefix += " Number of Dependants";
        }
        else if (pd.maritalStatus.trim().length() ==0 || pd.maritalStatus.toLowerCase().contains("select")){
            prefix += " Marital Status";
        }
        else if (pd.category.trim().length() ==0 || pd.category.toLowerCase().contains("select") || TextUtils.equals(pd.category, "-1")){
            prefix += " Category";
        }
        else if (pd.religion.trim().length() ==0 || pd.religion.toLowerCase().contains("select") || TextUtils.equals(pd.religion, "-1")){
            prefix += " Religion";
        }
        else if (pd.qualification.trim().length() ==0 || pd.qualification.toLowerCase().contains("select") || TextUtils.equals(pd.qualification, "-1")){
            prefix += " Qualification";
        }

        if (prefix.length() != applicantNo.length())
            return prefix + " of Personal Details has not been Filled/Selected.";

        return "";
    }

    private static String validateContact(int index, ApplicantContactDetail cd){
        String applicantNo = "Applicant "+index + "\n";
        String prefix = applicantNo;

        if (cd == null)
            return prefix +"\n Contact Details Are Incomplete.";

        if (TextUtils.isEmpty(cd.residenceCode)) {
            prefix += " Residence STD Code";
        } else if(cd.residenceCode.length() < 2 || cd.residenceCode.length() > 8) {
            prefix += " Residence STD Code";
        } else if (TextUtils.isEmpty(cd.emailID) || !Patterns.EMAIL_ADDRESS.matcher(cd.emailID).matches()) {
            prefix += " Email ID";
        }else if(cd.residenceStatus.trim().length() ==0 || cd.residenceStatus.toLowerCase().contains("select")) {
            prefix += " Residence Status";
        }else if(cd.residenceLandline.length() < 8 || cd.residenceLandline.length() > 8) {
            prefix += " Residence LandLine Number";
        }else if(cd.mobileno.length() < 10 || cd.mobileno.length() > 10) {
            prefix += " Mobile Number";
        }else if(cd.mailingAddress.trim().length() ==0 || cd.mailingAddress.toLowerCase().contains("select")){
            prefix += " Preferred Mailing Address";
        }

        if (prefix.length() != applicantNo.length())
            return prefix + " of Contact Details has not been Filled/Selected.";

        return "";
    }

    private static String validateOccupation(int index, ApplicantOccupation ao, Integer noOfApplicantsGeneral){
        String applicantNo = "Applicant "+index + "\n";
        String prefix = applicantNo;

        if (ao == null)
            return prefix + "\n Occupation Details Are Incomplete.";

        if(ao.natureOfEmployment.trim().length() ==0 || ao.natureOfEmployment.toLowerCase().contains("select"))
            prefix += " Employment";

        /*
         * Only One Applicant can be General; No other applicant can
         */
        if (ao.natureOfEmployment.equals("G"))
            noOfApplicantsGeneral += 1;

        if (noOfApplicantsGeneral > 1)
            return prefix + " cannot have its occupation type as General.\nOnly 1 Applicant can be General";

        if(ao.organisationName.trim().length() ==0 || ao.organisationName.toLowerCase().contains("select"))
            prefix += " Organisation Name";

        else if(TextUtils.isEmpty(ao.dibYears))
            prefix += " Duration in Business in Years";

        else if(TextUtils.isEmpty(ao.dibMonths))
            prefix += " Duration in Business in Months";

        else if(!UtilsValidateLoanApplication.validateLessNumber(ao.dibMonths,12))
            return prefix + " Duration of Business in Months should be less than 12";

        else if(TextUtils.isEmpty(ao.tweYears))
            prefix += " Total Work Experience in Years";

        else if(TextUtils.isEmpty(ao.tweMonths))
            prefix += " Total Work Experience in Months";

        else if(!UtilsValidateLoanApplication.validateLessNumber(ao.tweMonths,12))
            return prefix + " Total Work Experience in Months should be less than 12";

        else if(TextUtils.isEmpty(ao.addressLineOne))
            prefix += " Office Address Line 1";

        else if(TextUtils.isEmpty(ao.addressLineTwo))
            prefix += " Office Address Line 2";

        else if(TextUtils.isEmpty(ao.pinCode))
            prefix += " Pincode";

        else if(TextUtils.isEmpty(ao.city))
            prefix += " City";

        else if(TextUtils.isEmpty(ao.state))
            prefix += " State";

        else if(TextUtils.isEmpty(ao.ofcLandlineNo))
            prefix += " Office Landline No.";

        else if (TextUtils.isEmpty(ao.ofcEmail))
            prefix += " Office Email";

        if (prefix.length() != applicantNo.length())
            return prefix + " of Occupation Details has not been Filled/Selected.";

        return "";
    }

    private static String validateDocument(int index, String natureOfEmployment, Applicant app, Repository.RoomRepository repository){
        String applicantNo = "Applicant "+index + "\n";
        String prefix = applicantNo;

        ArrayList<String> documents = new ArrayList<>();

        try{
            for (ApplicantDocument applicantDocument: repository.getAllDocs(app.applicantID)){
                documents.add(applicantDocument.type);
            }
        }catch (Exception e){
            Timber.e(e);
        }

        // =========================================================================================
        // Check Personal Documents
        // =========================================================================================
        if (!documents.contains(DocumentList.aadhaarImage))
            return prefix + "Aadhaar Document has not been uploaded.";

        if (!documents.contains(DocumentList.PANCardImage))
            return prefix + "PAN Card Document has not been uploaded.";

        if (!documents.contains(DocumentList.IDProofImage))
            return prefix + "ID Proof Document has not been uploaded.";

        if (!documents.contains(DocumentList.permanentAddressProof))
            return prefix + "Permanent Address Document has not been uploaded.";

        if (!documents.contains(DocumentList.currentAddressProof))
            return prefix + "Current Address Proof Document has not been uploaded.";

        // =========================================================================================
        // Check Occupation Documents
        // =========================================================================================
        switch (natureOfEmployment){
            case EmploymentFormViewModel.EMP_GENERAL_CODE:
                break;

            case EmploymentFormViewModel.EMP_SELF_EMPLOYED_CODE:

                if(!documents.contains(DocumentList.salariedBanksStatment))
                    prefix += " Bank Statement of 6 Months";

                else if(!documents.contains(DocumentList.ITR))
                    prefix += " ITR";

                else if(!documents.contains(DocumentList.PNLStatment))
                    prefix += " PNL Statement";

                else if(!documents.contains(DocumentList.balanceSheet))
                    prefix += " Balance Sheet";

                break;

            case EmploymentFormViewModel.EMP_SALARIED_CODE:

                if(!documents.contains(DocumentList.salarySlip))
                    prefix += " Salary Slip of 3 Months";

                else if(!documents.contains(DocumentList.salariedBanksStatment))
                    prefix += " Bank Statement of 6 Months";

                else if(!documents.contains(DocumentList.form16))
                    prefix += " Form16";

                else if(!documents.contains(DocumentList.ITR))
                    prefix += " ITR";

                break;

            case EmploymentFormViewModel.EMP_SELF_EMPLOYED_PROFESSIONAL_CODE:

                if(!documents.contains(DocumentList.salariedBanksStatment))
                    prefix += " Bank Statement of 6 Months";

                else if(!documents.contains(DocumentList.ITR))
                    prefix += " ITR";

                else if(!documents.contains(DocumentList.PNLStatment))
                    prefix += " PNL Statement";

                else if(!documents.contains(DocumentList.balanceSheet))
                    prefix += " Balance Sheet";

                break;
        }

        if (prefix.length() != applicantNo.length())
            return prefix + " Document has not been uploaded.";

        return "";
    }

    private static String validateLoanDetail(LoanDetail ld){
        String prefix = "";

        if (ld == null)
            return "Loan Details Are Incomplete.";

        if(TextUtils.isEmpty(ld.amountRequested))
            prefix += " Amount Requested";
        else if(TextUtils.isEmpty(ld.term))
            prefix += " Term";
        else if (termValidation(ld.term))
            return "Loan Details Are Incomplete. Term should have be minimum 12 Months";
        else if(TextUtils.isEmpty(ld.typeOfInterest))
            prefix += " Type of Interest";

        /*
        else if(TextUtils.isEmpty(ld.product))
            prefix += " Product Name";
        */

        else if(TextUtils.isEmpty(ld.promotion))
            prefix += " Promotion Name";
        else if(TextUtils.isEmpty(ld.purposeOfLoan))
            prefix += " Purpose of Loan";

        if (prefix.length() > 0)
            return prefix + " of Loan Details has not been Filled/Selected.";

        return "";
    }

    private static boolean termValidation(String value){
        try {
            int term  = Integer.parseInt(value);
            if (term < 12)
                return true;
        }catch (Exception e){
            return true;
        }

        return false;
    }

    public static boolean validateLessNumber(String value, int limit){
        try {
            int number = Integer.parseInt(value);
            if (number < limit)
                return true;

            return false;
        }catch (Exception e){
            Timber.e(e);
            return true;
        }
    }
}
