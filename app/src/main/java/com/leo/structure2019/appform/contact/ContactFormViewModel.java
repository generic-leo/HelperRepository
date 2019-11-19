package com.leo.structure2019.appform.contact;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Patterns;

import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.db.Residence;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantContactDetail;
import com.leo.homeloan.data.pojo.remote.ContactDetailRemote;
import com.leo.homeloan.data.pojo.remote.SaveRemote;
import com.leo.homeloan.data.shared.SharedPrefRepository;
import com.leo.homeloan.main.NotifyEventTag;
import com.leo.homeloan.main.di.Injector;
import com.leo.homeloan.main.helpers.BaseViewModel;
import com.leo.homeloan.main.helpers.NotifyEvent;
import com.leo.homeloan.main.utils.UtilUpdateApplicant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ContactFormViewModel extends BaseViewModel {

    private String mailingAddress = "Select Preferred Mailing Address";
    private MutableLiveData<List<String>> liveMailingAddressList;

    private String residenceStatus = "Select Residence Status";

    public ObservableField<String> stdCode = new ObservableField<>("");
    public ObservableField<String> residenceCode = new ObservableField<>("");
    public ObservableField<String> residenceLandline = new ObservableField<>("");
    public ObservableField<String> emailId = new ObservableField<>("");
    public ObservableField<String> mobileno = new ObservableField<>("0");

    public ObservableBoolean isEditable = new ObservableBoolean();

    private Repository.RoomRepository repository;
    private Repository.RemoteRepository remoteRepository;
    private SharedPrefRepository sharedPrefRepository;

    public ContactFormViewModel(@NotNull Application appContext) {
        super(appContext);
        this.repository = Injector.provideRoomRepository(appContext);
        this.remoteRepository = Injector.provideRemoteRepository();
        this.sharedPrefRepository = Injector.provideSharedRepository(appContext);

        liveMailingAddressList = new MutableLiveData<>();
    }

    public LiveData<List<String>> getMailingAddressList() {
        List<String> strings = new ArrayList<>();
        strings.add("Select Mailing Address");
        strings.add("Current Address");
        strings.add("Permanent Address");
        liveMailingAddressList.setValue(strings);
        return liveMailingAddressList;
    }
    public void setLiveMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public LiveData<List<Residence>> getResidenceStatusList() {
        return repository.getAllResidences();
    }
    public void setResidenceStatus(String residenceStatus) {
        this.residenceStatus = residenceStatus;
    }

    /*
     * DB Logic
     */

    public String validateContact() {
        String prefix = "You haven't ";
        String postfix = ".\n\nDo you wish to save this page as a Draft with the partial data entered ?";

        if(mobileno.get().length() < 10 || mobileno.get().length() > 10) {
            return prefix + "entered valid Mobile Number"+ postfix;
        }

        if (TextUtils.isEmpty(emailId.get()) || !Patterns.EMAIL_ADDRESS.matcher(emailId.get()).matches()) {
            return prefix + "entered Email ID"+ postfix;
        }

        if(residenceStatus.trim().length() ==0 || residenceStatus.toLowerCase().contains("select") || TextUtils.equals(residenceStatus,"-1")) {
            return prefix + "selected Residence Status"+ postfix;
        }

        if(residenceLandline.get().length() < 8 || residenceLandline.get().length() > 8) {
            return prefix + "entered valid Residence Landline Number"+ postfix;
        }

        if (TextUtils.isEmpty(stdCode.get())) {
            return prefix + "entered STD Code"+ postfix;
        }

        if(stdCode.get().length() < 3 || stdCode.get().length() > 8) {
            return prefix + "entered 3 Digit STD Code"+ postfix;
        }

        if(mailingAddress.trim().length() ==0 || mailingAddress.toLowerCase().contains("select")){
            return prefix + "selected Preferred Mailing Address"+ postfix;
        }

        else {
            return "Valid";
        }
    }

    public String validateAndSave(final boolean validate, final String appFormID, final int applicantNumber){
        String msg = validateContact();
        if (validate && !msg.equalsIgnoreCase("valid")){
            notifyEvent(new NotifyEvent(NotifyEventTag.MESSAGE_DIALOG, 1, msg));
            return msg;
        }else{

            boolean userExists = applicantNumber >= 0;
            notifyEvent(new NotifyEvent(NotifyEventTag.LOADER_SHOW, 0, ""));
            remoteRepository.saveContact(appFormID,
                    sharedPrefRepository.getUserType(),
                    UtilUpdateApplicant.getApplicantStatus(applicantNumber, userExists),
                    userExists, applicantNumber, getRemotePojo(), new Repository.RemoteCallback<SaveRemote>() {

                @Override
                public void onStart() {}

                @Override
                public void onSuccess(SaveRemote response) {
                    try{
                        int applicantID = response.parseLoanID();
                        saveData(applicantID);
                        notifyEvent(new NotifyEvent(NotifyEventTag.LOADER_HIDE_MESSAGE_NAVIGATE, 0, applicantID,"Details Saved Successfully"));
                    }catch (Exception e){
                        Timber.e(e);
                        notifyEvent(new NotifyEvent(NotifyEventTag.LOADER_HIDE_ERROR_MESSAGE, 0, "Failed To Save Response"));
                    }
                }

                @Override
                public void onFailure(String error_code, String error_message) {
                    notifyEvent(new NotifyEvent(NotifyEventTag.LOADER_HIDE_ERROR_MESSAGE, 0, error_message));
                }
            });

            return msg;
        }
    }

    private ContactDetailRemote getRemotePojo(){
        ContactDetailRemote contactDetailRemote = new ContactDetailRemote(
                mailingAddress,
                mobileno.get(),
                emailId.get(),
                residenceStatus,
                stdCode.get(),
                residenceLandline.get()
        );

        return contactDetailRemote;
    }

    private void saveData(int applicantNumber){
        ApplicantContactDetail contactDetail = repository.getContactDetails(applicantNumber).getValue();

        ApplicantContactDetail applicantContactDetail = new ApplicantContactDetail();

        if(contactDetail != null)
            applicantContactDetail.contactID = contactDetail.contactID;

        applicantContactDetail.mailingAddress = mailingAddress;
        applicantContactDetail.applicantID = applicantNumber;
        applicantContactDetail.stdCode = stdCode.get();
        applicantContactDetail.residenceCode = residenceCode.get();
        applicantContactDetail.emailID = emailId.get();
        applicantContactDetail.mobileno = mobileno.get();
        applicantContactDetail.residenceStatus = residenceStatus;
        applicantContactDetail.residenceLandline = residenceLandline.get();

        repository.insertContactDetails(applicantContactDetail);
    }

    public LiveData<ApplicantContactDetail> loadContactDetail(int applicantNumber){
        return repository.getContactDetails(applicantNumber);
    }

    public void updateContactDetail(ApplicantContactDetail input){

        mailingAddress = input.mailingAddress;
        mobileno.set( input.mobileno );
        emailId.set( input.emailID );
        residenceStatus = input.residenceStatus;
        residenceLandline.set( input.residenceLandline );
        residenceCode.set( input.residenceCode );
        stdCode.set( input.stdCode  );
    }
}
