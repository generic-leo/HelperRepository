package com.leo.structure2019.main;

import android.app.Dialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.leo.homeloan.R;
import com.leo.homeloan.appform.ApplicationFormFragment;
import com.leo.homeloan.appform.applicant.AddApplicantFragment;
import com.leo.homeloan.appform.contact.ContactFormFragment;
import com.leo.homeloan.appform.financial.FinancialFormFragment;
import com.leo.homeloan.appform.loandetail.LoanDetailFragment;
import com.leo.homeloan.appform.personal.PersonalFormFragment;
import com.leo.homeloan.calculator.CalculatorFragment;
import com.leo.homeloan.calculator.eligibility.EligibilityCalculatorFragment;
import com.leo.homeloan.calculator.emi.EmiCalculatorFragment;
import com.leo.homeloan.camera.CaptureActivity;
import com.leo.homeloan.camera.ViewImageActivity;
import com.leo.homeloan.createlead.CreateLeadFragment;
import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.shared.SharedPrefRepository;
import com.leo.homeloan.databinding.ActivityMainBinding;
import com.leo.homeloan.loan.application.BranchLocator.AddressFragment;
import com.leo.homeloan.loan.application.BranchLocator.BranchLocatorFragment;
import com.leo.homeloan.loan.application.MyLoanAppEmployeeFragment;
import com.leo.homeloan.loan.application.MyLoanApplicationFragment;
import com.leo.homeloan.loan.application.approvals.ApprovalDashboardFragment;
import com.leo.homeloan.loan.application.approvals.ReviewFormFragment;
import com.leo.homeloan.loan.application.approvals.ViewTCFragment;
import com.leo.homeloan.loan.application.payment.PaymentFragment;
import com.leo.homeloan.loan.application.payment.PaymentGatewayFragment;
import com.leo.homeloan.loan.application.updates.DisbursementRequestFormFragment;
import com.leo.homeloan.loan.application.updates.SanctionFragment;
import com.leo.homeloan.login.ChannelLoginFragment;
import com.leo.homeloan.login.CustomerLoginFragment;
import com.leo.homeloan.login.CustomerTypeFragment;
import com.leo.homeloan.login.EmployeeLoginFragment;
import com.leo.homeloan.main.di.Injector;
import com.leo.homeloan.main.helpers.ArgumentData;
import com.leo.homeloan.main.helpers.BaseActivity;
import com.leo.homeloan.main.helpers.ViewModelFactory;
import com.leo.homeloan.menu.MenuFragment;
import com.leo.homeloan.newuser.RegisterUserFragment;
import com.leo.homeloan.session.FCSessionTimer;
import com.leo.homeloan.session.SessionTimer;
import com.leo.homeloan.setmpin.SetMpinFragment;
import com.leo.homeloan.splash.SplashFragment;
import com.leo.homeloan.util.ActivityUtils;
import com.leo.homeloan.util.CustomTextViewBold;
import com.leo.homeloan.verifyotp.OTPVerificationFragment;

import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class MainActivity extends BaseActivity implements SessionTimer.SessionTimerCallback{

    public String STORAGE_PATH = Environment.getExternalStorageDirectory()+ "/LEO";

    public enum Module{
        SplashScreen,
        CalculatorScreen,
        EligibilityCalculatorScreen,
        EmiCalculatorScreen,
        MenuScreen,
        AppFormScreen,
        LoanDetailsScreen,
        CreateLead,
        CustomerLogin,
        CustomerType,
        MyLoanApplication,
        Dashboard,
        Personal,
        Contact,
        Financial,
        AddApplicant,
        RegisterUser,
        ForgotUser,
        Otp,
        SetMpin,
        EmployeeLogin,
        ChannelLogin,
        MyLoanAppEmployeeScreen,
        ApprovalDashboardScreen,
        ReviewFormScreen,
        PaymentScreen,
        ViewTCScreen,
        SanctionInfoScreen,
        DRFormScreen,
        BranchLocator,
        Address,
        PaymentGatewayScreen
    }

    Stack<String> headerStack = new Stack<>();
    SharedPrefRepository sharedPrefRepository;

    ObservableBoolean header = new ObservableBoolean(false);
    ObservableField<String> headerName = new ObservableField<>("My Loan");
    ActivityMainBinding binding;

    BottomSheetBehavior sheetBehavior;
    SessionTimer sessionTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHeader(header);
        binding.setHeaderName(headerName);

        init();

        // Check if fragment exists
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(fragment == null)
            openModule(Module.SplashScreen,null);

        registerDrawerClicks();
        initBottomSheet();
        initListener();
        initSession();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        if (sessionTimer != null)
            sessionTimer.resetTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sessionTimer != null)
            sessionTimer.stopTimer();
    }

    @Override
    public void onBackPressed() {
        if (isCurrent(MenuFragment.class)){

            showMessageDialog("Are you sure you want to exit ?", "Yes", new DialogDismiss() {
                @Override
                public void onDismiss() {
                    finish();
                }
            },"No",new DialogDismiss(){
                @Override
                public void onDismiss() {

                }
            });
        }
        else if(isCurrent(SplashFragment.class)){
            finish();
        }
        else if (isCurrent(PaymentGatewayFragment.class)){
            showMessageDialog("Are you sure you want to cancel payment ?", "Yes", new DialogDismiss() {
                @Override
                public void onDismiss() {
                    setDrawerState( hideHeader() );
                    MainActivity.super.onBackPressed();
                }
            },"No", null);
        }
        else if( isCurrent(ApplicationFormFragment.class) ) {
            showMessageDialog("Are you sure you want to go back ? Any unsaved changes will be lost.", "Yes", new DialogDismiss() {
                @Override
                public void onDismiss() {
                    setDrawerState( hideHeader() );
                    MainActivity.super.onBackPressed();
                }
            },"No", null);
        }
        else if( isCurrent(MyLoanAppEmployeeFragment.class) || isCurrent(MyLoanApplicationFragment.class) ) {
            showMessageDialog("Are you sure you want you want to log out ?", "Yes", new DialogDismiss() {
                @Override
                public void onDismiss() {
                    logout();
                    setDrawerState( hideHeader() );
                    MainActivity.super.onBackPressed();
                }
            },"No", null);
        }
        else{
            super.onBackPressed();
            setDrawerState( hideHeader() );
        }
    }

    public void performSuperBackClick(){
        MainActivity.super.onBackPressed();
    }

    private void init(){
        sharedPrefRepository = Injector.provideSharedRepository(getApplication());
    }

    private void initListener(){
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (header.get())
                    onBackPressed();
            }
        });

        binding.navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (header.get())
                    binding.drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    // region [ Navigation Drawer ]
    private void registerDrawerClicks(){

        CustomTextViewBold versionCode = binding.navView.getHeaderView(0).findViewById(R.id.versionCode);
        try {
            PackageInfo pInfo =  getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_my_home:
                        //Do some thing here
                        // add navigation drawer item onclick method here
                        closeDrawer();
                        showToast("Home");
                        break;

                    case R.id.nav_my_dashboard:
                        //Do some thing here
                        // add navigation drawer item onclick method here
                        closeDrawer();
                        // showToast("Dashboard");
                        break;

                    case R.id.nav_my_branch_locator:
                        //Do some thing here
                        // add navigation drawer item onclick method here
                        closeDrawer();

                        if (! isCurrent(BranchLocatorFragment.class)){
                            openModule(Module.BranchLocator,null);
                        }

                        break;

                    case R.id.nav_my_log_out:
                        //Do some thing here
                        // add navigation drawer item onclick method here
                        closeDrawer();
                        logout();
                        openModule(Module.MenuScreen, null);
                        break;
                }
                return false;
            }
        });
    }
    private void setDrawerState(boolean enable){
        binding.drawerLayout.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void closeDrawer(){
        binding.drawerLayout.closeDrawers();
    }

    // endregion

    // region [ Bottom Sheet ]
    private void initBottomSheet(){
         sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
         binding.bottomSheet.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                     sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                 } else {
                     sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                 }
             }
         });
    }
    // endregion

    // region [ Header ]
    private void showHeader(String name){
        if(name.length() > 0 ){
            header.set(true);
            headerName.set(name);

            // Show Drawer
            setDrawerState(true);
        }
        else{
            header.set(false);

            // Hide Drawer
            setDrawerState(false);
        }

        headerStack.push(name);
        Timber.i(MainActivity.class.getSimpleName(), "Show -> Stack["+headerStack.size()+"] -> " + headerStack);
    }

    private boolean hideHeader(){

        Timber.i(MainActivity.class.getSimpleName(), "Hide -> Stack["+headerStack.size()+"] -> " + headerStack);

        try {
            String currentValue = "";

            if(! headerStack.empty()){
                headerStack.pop();
            }

            if(! headerStack.empty()){
                currentValue = headerStack.peek();
            }

            if (currentValue.length() == 0) {
                header.set(false);
                return false;
            }
            else{
                headerName.set(currentValue);
                header.set(true);
                return true;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean isCurrent(Class classVariable){
        try{
            return getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame)
                    .getClass() == classVariable ? true : false;

        }catch (Exception e){
            // FOR A APPLICATION STATE WHERE,
            // FragmentManager / Fragment / TypeCast / Null Checks
        }

        return false;
    }
    // endregion

    // region [ Fragments & Stuff ]
    public void openModule(Module module,HashMap<String,ArgumentData> parameters){
        Fragment fragment = null;
        String headerTitle = "";

        switch (module){
            case EligibilityCalculatorScreen:
                fragment = EligibilityCalculatorFragment.newInstance();
                break;

            case MenuScreen:
                fragment = MenuFragment.newInstance();
                break;

            case AppFormScreen:
                fragment = ApplicationFormFragment.newInstance(parameters);
                headerTitle = "My Loan Applications";
                break;

            case LoanDetailsScreen:
                fragment = LoanDetailFragment.newInstance(parameters);
                headerTitle ="Loan Detail";
                break;

            case EmiCalculatorScreen:
                fragment = EmiCalculatorFragment.newInstance();
                break;

            case SplashScreen:
                fragment = SplashFragment.newInstance();
                break;

            case CalculatorScreen:
                fragment = CalculatorFragment.newInstance();
                headerTitle ="Calculator";
                break;

            case CreateLead:
                fragment = CreateLeadFragment.newInstance();
                headerTitle ="Create A Lead";
                break;

            case CustomerLogin:
                fragment = CustomerLoginFragment.newInstance();
                headerTitle ="Customer Login";
                break;

            case EmployeeLogin:
                fragment = EmployeeLoginFragment.newInstance();
                headerTitle ="Employee Login";
                break;

            case ChannelLogin:
                fragment = ChannelLoginFragment.newInstance();
                headerTitle ="Channel Login";
                break;

            case CustomerType:
                fragment = CustomerTypeFragment.newInstance();
                headerTitle ="Login";
                break;

            case MyLoanApplication:
                fragment = MyLoanApplicationFragment.newInstance(parameters);
                headerTitle ="My Loan Application";

                startSession();

                break;

            case MyLoanAppEmployeeScreen:
                fragment = MyLoanAppEmployeeFragment.newInstance(parameters);
                headerTitle ="My Loan Application";

                startSession();

                break;


            case AddApplicant:
                fragment = AddApplicantFragment.newInstance(parameters);
                headerTitle ="My Loan Application";
                break;

            case Personal:
                fragment = PersonalFormFragment.newInstance(parameters);
                break;

            case Contact:
                fragment = ContactFormFragment.newInstance(parameters);
                break;

            case Financial:
                fragment = FinancialFormFragment.newInstance(parameters);
                break;

            case RegisterUser:
                fragment = RegisterUserFragment.newInstance(parameters);
                headerTitle = "Create Customer Login";
                break;

            case ForgotUser:
                fragment = RegisterUserFragment.newInstance(parameters);
                headerTitle = "Forgot MPIN";
                break;

            case Otp:
                fragment = OTPVerificationFragment.newInstance(parameters);
                headerTitle = "Create Customer Login";
                break;

            case SetMpin:
                fragment = SetMpinFragment.newInstance(parameters);
                headerTitle = "Create Customer Login";
                break;

            case ApprovalDashboardScreen:
                fragment = ApprovalDashboardFragment.newInstance(parameters);
                headerTitle ="My Loan Application";
                break;

            case ReviewFormScreen:
                fragment = ReviewFormFragment.newInstance(parameters);
                headerTitle ="Review Loan Application";
                break;

            case PaymentScreen:
                fragment = PaymentFragment.newInstance(parameters);
                headerTitle ="Payment ";
                break;

            case ViewTCScreen:
                fragment = ViewTCFragment.newInstance(parameters);
                headerTitle ="Declaration";
                break;

            case SanctionInfoScreen:
                fragment = SanctionFragment.newInstance(parameters);
                headerTitle ="Sanction Letter";
                break;

            case DRFormScreen:
                fragment = DisbursementRequestFormFragment.newInstance(parameters);
                headerTitle ="Disbursement Request Form";
                break;
            case BranchLocator:
                fragment = BranchLocatorFragment.newInstance();
                headerTitle = "Branch Locator";
                break;
            case Address:
                fragment = AddressFragment.newInstance(parameters);
                headerTitle = "Address";
                break;

            case PaymentGatewayScreen:
                fragment = PaymentGatewayFragment.newInstance(parameters);
                // headerTitle = "Payment";
                break;
        }

        if(fragment == null)
            return;

        ActivityUtils.addToStackFragment(getSupportFragmentManager(), fragment, R.id.contentFrame);

        // Maintain & Show Header If Required
        showHeader(headerTitle);
        showLogoutButton();
    }

    /*
     * Don't Ask Me What I Did Here !
     */
    public static <T extends AndroidViewModel> T getModel(FragmentActivity fragmentActivity, Class modelClass){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance(fragmentActivity.getApplication());
        return (T) ViewModelProviders.of(fragmentActivity, viewModelFactory).get(modelClass);
    }
    // endregion

    // region [ Image Options ]
    Dialog imageOptionDialog;
    public void showImageOptions(final Fragment fragment, final int requestCameraCode, final int requestViewCode, final String storagePath, final String filePath){
        dismissImageOptions();

        if(filePath.length() == 0){
            startCamera(fragment, requestCameraCode, storagePath);
            return;
        }

        imageOptionDialog = new Dialog(fragment.getContext());
        imageOptionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageOptionDialog.setCancelable(false);
        imageOptionDialog.setContentView(R.layout.dialog_image_option);
        imageOptionDialog.show();

        imageOptionDialog.findViewById(R.id.btnRecapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissImageOptions();
                startCamera(fragment, requestCameraCode, storagePath);
            }
        });

        imageOptionDialog.findViewById(R.id.btnView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissImageOptions();
                previewImage(fragment, requestViewCode, filePath);
            }
        });

        imageOptionDialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissImageOptions();
            }
        });
    }

    private void startCamera(Fragment fragment, int requestCode, String filepath){
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra("storage_path",filepath);
        fragment.startActivityForResult(intent, requestCode);
    }

    private void previewImage(Fragment fragment, int requestCode, String filepath){
        Intent intent = new Intent(this, ViewImageActivity.class);
        intent.putExtra("path",filepath);
        fragment.startActivityForResult(intent, requestCode);
    }

    public void dismissImageOptions(){
        try{
            imageOptionDialog.dismiss();
        }catch (Exception e){}
    }
    // endregion

    // region [ Session Management ]

    private void initSession(){
        sessionTimer = new FCSessionTimer(this, Injector.provideSharedRepository(getApplication()));
        sessionTimer.setTime(15, TimeUnit.SECONDS);
    }

    private void startSession(){
        if (sessionTimer != null)
            sessionTimer.startTimer();
    }

    @Override
    public void onTimerStarted() {
        Timber.i("onTimerStarted");
    }

    @Override
    public void onTimerReset() {
        Timber.i("onTimerReset");
    }

    @Override
    public void onTimeFinished() {
        Timber.i("onTimeFinished | isSessionValid " + sessionTimer.isSessionValid());

        // Stop Timer
        sessionTimer.stopTimer();

        // Log Out User
        logoutUser();
    }

    private void logoutUser(){
        if (!isUserLoggedIn())
            return;

        if (sessionTimer.isSessionValid())
            return;

        if (isCurrent(ApplicationFormFragment.class)){
            showMessageDialog("You are about to be logged out because of Inactivity. Do you wish to save your current changes ?",
                    "Yes", null, "No", new DialogDismiss() {
                        @Override
                        public void onDismiss() {
                            navigateLogout();
                        }
                    });

        }else {
            showErrorDialog("You have been logged out because of inactivity", new DialogDismiss() {
                @Override
                public void onDismiss() {
                    navigateLogout();
                }
            });
        }
    }

    private boolean isUserLoggedIn(){
        return sharedPrefRepository.getLoginStatus();
    }

    private void navigateLogout(){
        // flag
        sharedPrefRepository.setLoginStatus(false);

        switch (sharedPrefRepository.getUserType()){

            case FCConstants.USER_TYPE_CUSTOMER:
                openModule(Module.CustomerLogin, null);
                break;
            case FCConstants.USER_TYPE_EMPLOYEE:
                openModule(Module.EmployeeLogin, null);
                break;
            case FCConstants.USER_TYPE_CHANNEL:
                openModule(Module.ChannelLogin, null);
                break;

            default:
                break;
        }
    }

    // endregion

    // region [ logout ]
    private void showLogoutButton(){
        binding.navView.getMenu().findItem(R.id.nav_my_log_out).setVisible(isUserLoggedIn());
    }

    public void logout(){
        Repository.RemoteRepository remoteRepository = Injector.provideRemoteRepository();
        SharedPrefRepository sharedPrefRepository = Injector.provideSharedRepository(getApplication());

        sharedPrefRepository.setLoginStatus(false);


        /*
        String session_id = "";
        String used_id = "";

        remoteRepository.logout(session_id, used_id, new Repository.RemoteCallback<String>() {
            @Override
            public void onStart() {
                showLoader();
            }

            @Override
            public void onSuccess(String response) {
                dismissLoader();
            }

            @Override
            public void onFailure(String error_code, String error_message) {
                dismissLoader();
            }
        });
        */
    }
    // endregion
}
