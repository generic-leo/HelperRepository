package com.leo.structure2019.main.utils;

import com.leo.homeloan.BuildConfig;
import com.leo.homeloan.appform.document.DocumentList;
import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.db.Applicant;
import com.leo.homeloan.data.pojo.db.LoanDetail;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantContactDetail;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantDocument;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantOccupation;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantPersonalDetail;
import com.leo.homeloan.data.pojo.remote.ApplicantDetail;
import com.leo.homeloan.data.pojo.remote.ApplicantList;
import com.leo.homeloan.data.pojo.remote.ContactDetailRemote;
import com.leo.homeloan.data.pojo.remote.DocumentDetailRemote;
import com.leo.homeloan.data.pojo.remote.EmploymentDetailRemote;
import com.leo.homeloan.data.pojo.remote.LoanDetailRemote;
import com.leo.homeloan.data.pojo.remote.PersonalDetailRemote;

import java.util.ArrayList;

import timber.log.Timber;

public class UtilUpdateApplicant {

    /**
     * @param loanID needs the Loan Application ID & not Applicant ID
     */
    public static void updateApplicantData(ApplicantList data, Repository.RoomRepository repository, int loanID){
        if(! isNull(data)){
            LoanDetailRemote detail = data.getLoanDetails();
            ArrayList<ApplicantDetail> appList = data.getAapList();

            // Update Loan Details
            if( ! isNull(detail) ){

                LoanDetail existingDetail = repository.getLoanDetail(loanID);

                LoanDetail loanDetail = new LoanDetail();

                if (existingDetail != null)
                    loanDetail.loanDetailID = existingDetail.loanDetailID;

                loanDetail.applicationNo = loanID;
                loanDetail.amountRequested = detail.getLoanamountRequired();
                loanDetail.term = detail.getTenureRequired();
                loanDetail.typeOfInterest = detail.getTypeofInterest();
                loanDetail.product = detail.getProduct();
                loanDetail.promotion = detail.getPromotion();
                loanDetail.purposeOfLoan = detail.getPurposeOfLoan();

                repository.insertLoanDetail(loanDetail);

                // Update Amount Of Loan
                repository.updateLoanApplication(loanID, detail.getLoanamountRequired());
            }

            // Update Applicant Details
            if(appList != null){
                ArrayList<Integer> applicants = new ArrayList<>();

                for (int i= 0; i < appList.size(); i++){
                    ApplicantDetail applicantDetail = appList.get(i);

                    if(applicantDetail != null){
                        PersonalDetailRemote personalDetailRemote = applicantDetail.getPersonalId();
                        ContactDetailRemote contactDetailRemote = applicantDetail.getContactId();
                        EmploymentDetailRemote employmentDetailRemote = applicantDetail.getEmpId();

                        updatePersonal(applicantDetail.getLoanId(), personalDetailRemote, repository);
                        updateContact(applicantDetail.getLoanId(), contactDetailRemote, repository);
                        updateEmployee(applicantDetail.getLoanId(), employmentDetailRemote, repository);
                        updateDocument(applicantDetail.getLoanId(), repository, applicantDetail.getDocumentsDetails());

                        updateApplicant(applicantDetail.getLoanId(), loanID,
                                personalDetailRemote, repository);
                    }

                    applicants.add(applicantDetail.getLoanId());
                }

                // Clear Applicant
                // if (applicants.size() > 0)
                    repository.deleteApplicants(loanID,  getIntArray(applicants) );
            }
        }
    }

    private static  void updateApplicant(int applicantID, int loanID, PersonalDetailRemote personal, Repository.RoomRepository repository){
        Applicant applicant = new Applicant();
        applicant.applicantID = applicantID;
        applicant.applicationNo = loanID;

        if (personal != null){
            applicant.name = setApplicantName(personal.getFirstName(), personal.getMiddleName(), personal.getLastName());
        }

        repository.updateApplicant(applicant);
    }
    private static  void updatePersonal(int applicantID, PersonalDetailRemote personalDetailRemote, Repository.RoomRepository repository){

        if ( isNull( personalDetailRemote ) )
            return;

        Timber.i("Personal Data : " + personalDetailRemote);

        ApplicantPersonalDetail personalDetail = repository.getPersonalDetails(applicantID).getValue();

        // Meaning Valid; Save To DB
        ApplicantPersonalDetail applicantPersonalDetail = new ApplicantPersonalDetail();

        if(personalDetail != null)
            applicantPersonalDetail.personalID = personalDetail.personalID;

        applicantPersonalDetail.verifyAadharNo = personalDetailRemote.getVerifyAadharNo();
        applicantPersonalDetail.verifyPanno = personalDetailRemote.getVerifyPanno();

        applicantPersonalDetail.applicantID = applicantID;
        applicantPersonalDetail.panNo = personalDetailRemote.getPanno();
        applicantPersonalDetail.aadharNo= personalDetailRemote.getAadharNo();
        applicantPersonalDetail.firstName = personalDetailRemote.getFirstName();
        applicantPersonalDetail.middleName = personalDetailRemote.getMiddleName();
        applicantPersonalDetail.lastName = personalDetailRemote.getLastName();
        applicantPersonalDetail.child = personalDetailRemote.getNoOfDependants();
        applicantPersonalDetail.other = personalDetailRemote.getNoOfDependants();
        applicantPersonalDetail.maritalStatus = personalDetailRemote.getMaritalStatus();
        applicantPersonalDetail.category = personalDetailRemote.getCategory();
        applicantPersonalDetail.religion = personalDetailRemote.getReligion();
        applicantPersonalDetail.qualification = personalDetailRemote.getQualification();

        applicantPersonalDetail.category_desc = personalDetailRemote.getCategory_desc();
        applicantPersonalDetail.qualification_desc = personalDetailRemote.getQualification_desc();
        applicantPersonalDetail.religion_desc = personalDetailRemote.getReligion_desc();

        applicantPersonalDetail.noOfDependantes_child = personalDetailRemote.getNoOfDependants_child();
        applicantPersonalDetail.noOfDependantes_other = personalDetailRemote.getNoOfDependants_other();

        repository.insertPersonalDetails(applicantPersonalDetail);

    }
    private static  void updateContact(int applicantID, ContactDetailRemote contactDetailRemote, Repository.RoomRepository repository){
        if ( isNull(contactDetailRemote) )
            return;

        Timber.i("Contact Data : " + contactDetailRemote);

        ApplicantContactDetail contactDetail = repository.getContactDetails(applicantID).getValue();

        ApplicantContactDetail applicantContactDetail = new ApplicantContactDetail();

        if(contactDetail != null)
            applicantContactDetail.contactID = contactDetail.contactID;

        applicantContactDetail.mailingAddress = contactDetailRemote.getPreferredMailingAddress();
        applicantContactDetail.applicantID = applicantID;
        applicantContactDetail.stdCode = contactDetailRemote.getResidenceSTD();
        applicantContactDetail.residenceCode = contactDetailRemote.getResidenceSTD();
        applicantContactDetail.emailID = contactDetailRemote.getEmailid();
        applicantContactDetail.mobileno = contactDetailRemote.getMobileNumber();
        applicantContactDetail.residenceStatus = contactDetailRemote.getResidenceStatus();
        applicantContactDetail.residenceLandline = contactDetailRemote.getResidenceLandline();

        repository.insertContactDetails(applicantContactDetail);
    }
    private static  void updateEmployee(int applicantID, EmploymentDetailRemote employmentDetailRemote, Repository.RoomRepository repository){
        if ( isNull(employmentDetailRemote) )
            return;

        ApplicantOccupation occupation = repository.getOccupationDetails(applicantID).getValue();
        Timber.i("Applicant ID "+applicantID+ " "+ occupation);

        ApplicantOccupation applicantOccupation = new ApplicantOccupation();

        if(occupation != null)
            applicantOccupation.occupationID = occupation.occupationID;

        applicantOccupation.applicantID = applicantID;
        applicantOccupation.organisationName = employmentDetailRemote.getCompanyName();

        applicantOccupation.tweYears = employmentDetailRemote.getTWEYears();
        applicantOccupation.tweMonths = employmentDetailRemote.getTWEMonths();
        applicantOccupation.dibMonths = employmentDetailRemote.getDIBMonths();
        applicantOccupation.dibYears = employmentDetailRemote.getDIBYears();

        applicantOccupation.country =  employmentDetailRemote.getOfficeCountry() ;
        applicantOccupation.stdCode = employmentDetailRemote.getOfficeStdcode();

        applicantOccupation.addressLineOne = employmentDetailRemote.getOfficeAddressLine1();
        applicantOccupation.addressLineTwo = employmentDetailRemote.getOfficeAddressLine2();
        applicantOccupation.addressLineThree = employmentDetailRemote.getOfficeAddressLine3();
        applicantOccupation.pinCode = employmentDetailRemote.getOfficePincode();
        applicantOccupation.city = employmentDetailRemote.getOfficeCity();
        applicantOccupation.city_desc = employmentDetailRemote.getOfficeCity_desc();
        applicantOccupation.state = employmentDetailRemote.getOfficeState();
        applicantOccupation.state_desc = employmentDetailRemote.getOfficeState_desc();
        applicantOccupation.ofcLandlineNo = employmentDetailRemote.getOfficeLandlineNo();
        applicantOccupation.ofcEmail = employmentDetailRemote.getOfficeOfficialEmail();
        applicantOccupation.natureOfEmployment = employmentDetailRemote.getOccupationType();

        applicantOccupation.officeCountry_desc = employmentDetailRemote.getOfficeCountry_desc();
        applicantOccupation.companyName_desc = employmentDetailRemote.getCompanyName_desc();

        repository.insertOccupationDetails(applicantOccupation);
    }
    private static  void updateDocument(int applicantID, Repository.RoomRepository repository, ArrayList<DocumentDetailRemote> documentList){

        Timber.i("Document List : " + documentList);

        if(documentList == null)
            return;

        Timber.i("Document Size : " + documentList.size());

        // Clear All Documents
        repository.deleteDocuments(applicantID);

        for (int i =0; i < documentList.size() ; i++){
            DocumentDetailRemote documentDetailRemote = documentList.get(i);

            String documentType = documentDetailRemote.getName();
            String value = documentDetailRemote.getPath().replace("\\","/");

            Timber.i("Document Type : " + documentType+ " -> "+value);

            switch (documentType){
                case DocumentList.applicantImage:
                case DocumentList.aadhaarImage:
                case DocumentList.PANCardImage:
                case DocumentList.IDProofImage:
                case DocumentList.visitingCardImage:
                case DocumentList.chequeImage:
                case DocumentList.salarySlip:
                case DocumentList.salariedBanksStatment:
                case DocumentList.form16:
                case DocumentList.ITR:
                case DocumentList.PNLStatment:
                case DocumentList.balanceSheet:
                case DocumentList.permanentAddressProof:
                case DocumentList.currentAddressProof:
                case DocumentList.selfEmpBanksStatment:
                case DocumentList.additionalDocument1:
                case DocumentList.additionalDocument2:
                case DocumentList.additionalDocument3:
                case DocumentList.additionalDocument4:
                    saveDocument(applicantID, documentType, value, repository);
                    break;

                default:
                    Timber.i("Document Key Changed or New Added !");

            }
        }
    }
    private static  void saveDocument(int applicantID, String tag, String value, Repository.RoomRepository repository){
        ApplicantDocument applicantDocument = new ApplicantDocument();
        applicantDocument.applicantID = applicantID;
        applicantDocument.type = tag;

        // Append BASE URL
        applicantDocument.path = getBaseURL(BuildConfig.SERVER_URL) + value;

        Timber.i("Applicant ID : "+applicantID+" Document Path : " + applicantDocument.path);

        repository.insertDocument(applicantDocument);
    }


    public static String getApplicantStatus(int applicantNo, boolean userExists){
        return "primary";
    }


    /*
     MISC METHODS
     */

    public static String setApplicantName(String firstName, String middleName, String lastName){
        String name = "Unnamed";

        if (firstName != null || middleName != null || lastName != null)
            name = "";

        if (firstName != null)
            name += firstName + " ";

        if (middleName != null)
            name += middleName + " ";

        if (lastName != null)
            name += lastName;

        return name;
    }

    public static String getBaseURL(String value){
        return value.replace("LEO/","LEO_Document/");
        // return value;
    }

    private static int[] getIntArray(ArrayList<Integer> integerArrayList){
        if (integerArrayList != null && integerArrayList.size() > 0){
            int[] array = new int[integerArrayList.size()];
            for (int i = 0; i < array.length ; i++)
                array[i] = integerArrayList.get(i);

            return array;
        }

        return new int[0];
    }

    public static boolean isNull(ApplicantDetail detail){
        if (detail.component1() == null &&
                detail.component2() == null &&
                detail.component3() == null &&
                detail.component4() == null)
            return true;

        return false;
    }

    public static boolean isNull(ApplicantList list){
        if (list.component1() == null && list.component2() == null)
            return true;

        return false;
    }

    public static boolean isNull(LoanDetailRemote remote){
        if (remote.component1() == null &&
                remote.component2() == null &&
                remote.component3() == null &&
                remote.component4() == null &&
                remote.component5() == null &&
                remote.component6() == null)
            return true;

        return false;
    }

    public static boolean isNull(EmploymentDetailRemote employmentDetailRemote){
        if (employmentDetailRemote.component1() == null &&
                employmentDetailRemote.component2() == null &&
                employmentDetailRemote.component3() == null &&
                employmentDetailRemote.component4() == null &&
                employmentDetailRemote.component5() == null &&
                employmentDetailRemote.component6() == null &&
                employmentDetailRemote.component7() == null &&
                employmentDetailRemote.component8() == null &&
                employmentDetailRemote.component9() == null &&
                employmentDetailRemote.component10() == null &&
                employmentDetailRemote.component11() == null &&
                employmentDetailRemote.component12() == null &&
                employmentDetailRemote.component13() == null &&
                employmentDetailRemote.component14() == null &&
                employmentDetailRemote.component15() == null &&
                employmentDetailRemote.component16() == null)
            return true;

        return false;
    }

    public static boolean isNull(PersonalDetailRemote personalDetailRemote){
        if (personalDetailRemote.component1() == null &&
                personalDetailRemote.component2() == null &&
                personalDetailRemote.component3() == null &&
                personalDetailRemote.component4() == null &&
                personalDetailRemote.component5() == null &&
                personalDetailRemote.component6() == null &&
                personalDetailRemote.component7() == null &&
                personalDetailRemote.component8() == null &&
                personalDetailRemote.component9() == null &&
                personalDetailRemote.component10() == null)
            return true;

        return false;
    }

    public static boolean isNull(ContactDetailRemote contactDetailRemote){
        if (contactDetailRemote.component1() == null &&
                contactDetailRemote.component2() == null &&
                contactDetailRemote.component3() == null &&
                contactDetailRemote.component4() == null &&
                contactDetailRemote.component5() == null &&
                contactDetailRemote.component6() == null)
            return true;

        return false;
    }

    public static boolean isNull(DocumentDetailRemote documentDetailRemote){
        if (documentDetailRemote.component1() == null &&
                documentDetailRemote.component2() == null)
            return true;

        return false;
    }

}
