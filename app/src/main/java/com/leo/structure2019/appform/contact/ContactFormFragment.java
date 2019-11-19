package com.leo.structure2019.appform.contact;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.leo.homeloan.BR;
import com.leo.homeloan.R;
import com.leo.homeloan.appform.ApplicationFormFragment;
import com.leo.homeloan.appform.ApplicationFormViewModel;
import com.leo.homeloan.data.pojo.db.Residence;
import com.leo.homeloan.data.pojo.db.applicant_info.ApplicantContactDetail;
import com.leo.homeloan.databinding.FragmentContactFormBinding;
import com.leo.homeloan.main.MainActivity;
import com.leo.homeloan.main.NotifyEventTag;
import com.leo.homeloan.main.helpers.ArgumentData;
import com.leo.homeloan.main.helpers.BaseActivity;
import com.leo.homeloan.main.helpers.BaseFragment;
import com.leo.homeloan.main.helpers.NotifyEvent;
import com.leo.homeloan.main.helpers.StringSpinnerAdapter;
import com.leo.homeloan.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactFormFragment extends BaseFragment<MainActivity> {

    public static ContactFormFragment newInstance(HashMap<String,ArgumentData> parameters) {
        ContactFormFragment contactFormFragment = new ContactFormFragment();
        contactFormFragment.addValues(parameters);
        return contactFormFragment;
    }
    private ContactFormViewModel model;
    private FragmentContactFormBinding binding;
    private StringSpinnerAdapter mailingAddressAdapter;
    private StringSpinnerAdapter<Residence> residenceStatusAdapter;

    private int spinnerReady = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        model = new ContactFormViewModel(
                activity().getApplication());

        binding = FragmentContactFormBinding.inflate(inflater, container, false);
        binding.setVariable(BR.model, model);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mailingAddressAdapter = new StringSpinnerAdapter(getContext(), R.layout.layout_spinner_textview, new ArrayList<String>());
        residenceStatusAdapter = new StringSpinnerAdapter(getContext(), R.layout.layout_spinner_textview, new ArrayList<String>());

        binding.spinnerMailingAddress.setAdapter(mailingAddressAdapter);
        binding.spinnerResidenceStatus.setAdapter(residenceStatusAdapter);

        model.isEditable.set(true);

        model.getMailingAddressList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                mailingAddressAdapter.addItems(strings);

                spinnerReady += 1;
                loadPreviousData();
            }
        });

        model.getResidenceStatusList().observe(this, new Observer<List<Residence>>() {
            @Override
            public void onChanged(@Nullable List<Residence> residences) {
                String select = "Select Residence";
                List<Residence> list = new ArrayList<>();
                list.add(new Residence(-1, select, select));
                list.addAll(residences);

                residenceStatusAdapter.addItems(list);

                spinnerReady += 1;
                loadPreviousData();
            }
        });

        initListeners();
        registerEvent();
    }

    private void loadPreviousData(){
        if (spinnerReady < 2)
            return;

        int applicantNumber = getParent().getApplicantNo(ApplicationFormViewModel.RETURN);
        model.loadContactDetail(applicantNumber).observe(this, new Observer<ApplicantContactDetail>() {
            @Override
            public void onChanged(@Nullable ApplicantContactDetail contactDetail) {
                if(contactDetail != null){
                    model.updateContactDetail(contactDetail);
                    Utils.setSpinnerValue(binding.spinnerMailingAddress, contactDetail.mailingAddress);
                    Utils.setSpinnerValue(binding.spinnerResidenceStatus, getResidence(contactDetail.residenceStatus));
                }
            }
        });
    }

    private void initListeners() {

        binding.btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(true);
            }
        });

        binding.spinnerMailingAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0){
                    model.setLiveMailingAddress(binding.spinnerMailingAddress.getSelectedItem().toString());
                }else {
                    model.setLiveMailingAddress("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerResidenceStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0){
                    Residence religion = residenceStatusAdapter.getItem(binding.spinnerResidenceStatus.getSelectedItemPosition());
                    model.setResidenceStatus(""+religion.getResidence_status_name());
                }else {
                    model.setResidenceStatus("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void validate(boolean validate){
        int applicantNumber = getParent().getApplicantNo(ApplicationFormViewModel.RETURN);
        int loanID = getParent().getLoanID();
        String appID = getParent().getAppID();

        model.validateAndSave(validate, appID, applicantNumber);
    }

    private void registerEvent(){
        model.events().observe(this, new Observer<NotifyEvent>() {
            @Override
            public void onChanged(@Nullable NotifyEvent notifyEvent) {
                switch (notifyEvent.type()){
                    case NotifyEventTag.LOADER_SHOW:
                        showLoader();
                        break;

                    case NotifyEventTag.LOADER_HIDE:
                        dismissLoader();
                        break;

                    case NotifyEventTag.MESSAGE_DIALOG:
                    case NotifyEventTag.LOADER_HIDE_ERROR_MESSAGE:
                        dismissLoader();

                        switch (notifyEvent.subType()){
                            case 0:
                                showErrorDialog(notifyEvent.getValue(), null);
                                break;

                            case 1:
                                showMessageDialog(notifyEvent.getValue(), "Save As Draft", new BaseActivity.DialogDismiss() {
                                    @Override
                                    public void onDismiss() {
                                        validate(false);
                                    }
                                }, "No", null);
                                break;
                        }

                        break;

                    case NotifyEventTag.LOADER_HIDE_MESSAGE_NAVIGATE:
                        dismissLoader();

                        // Update the Applicant ID
                        getParent().setApplicantNo(notifyEvent.getIntValue());

                        showMessageDialog(notifyEvent.getValue(),
                                "OK", new BaseActivity.DialogDismiss() {
                                    @Override
                                    public void onDismiss() {
                                        getParent().loadEmployment();
                                    }
                                }, "", null);
                        break;
                }
            }
        });
    }

    private ApplicationFormFragment getParent(){
        return (ApplicationFormFragment) getParentFragment();
    }

    // Spinner Index
    private int getResidence(String value){
        if (value == null)
            return 0;

        List<Residence> categories = residenceStatusAdapter.getItems();
        for (int i=0 ; i < categories.size(); i++){
            Residence category = categories.get(i);
            if(value.equalsIgnoreCase(""+category.component1()) ||
                    value.equalsIgnoreCase(category.component2()) ||
                    value.equalsIgnoreCase(category.component3())){
                return i;
            }
        }

        return 0;
    }
    // end
}
