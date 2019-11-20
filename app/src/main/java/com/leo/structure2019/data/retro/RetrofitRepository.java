package com.leo.structure2019.data.retro;

import android.databinding.ObservableField;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.homeloan.appform.document.DocumentList;
import com.leo.homeloan.data.Repository;
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
import com.leo.homeloan.data.retro.FCApiService;
import com.leo.homeloan.data.retro.RetrofitConfiguration;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import timber.log.Timber;

public class RetrofitRepository implements Repository.RemoteRepository {

    public FCApiService fcApiService;
    private ArrayList<WeakReference<Call>> retrofitQueues = new ArrayList<>();
    Type outputType;
    String request = "";

    public RetrofitRepository(){
        fcApiService = RetrofitConfiguration.INSTANCE.retrofitClient()
                .create(FCApiService.class);
    }

    @Override
    public void login(int type, String username, String password, Repository.RemoteCallback<LoginRemote> responseListener) {
        JSONObject request = new JSONObject();

        try{
            switch (type){
                case 0:
                    request.put("employeeId",username);
                    request.put("password",password);
                    break;

                case 1:
                    request.put("mobno",username);
                    request.put("mpin",password);
                    break;

                case 3:
                    request.put("dma_mobno",username);
                    request.put("mpin",password);
                    break;

                default:
            }
        }catch (Exception e){}

        // Timber.i("Request : " + request.toString());

        Type outputType = new TypeToken<LoginRemote>(){}.getType();
        fcApiService.authenticate(addRequestAndroid(request))
                .enqueue(new FCResponseCallback<LoginRemote>
                        (responseListener, true, outputType));
    }

    @Override
    public void performanceReport(String userID, String loginID, Repository.RemoteCallback<PerformanceRemote> remoteCallback) {
        //JSONObject jsonObject = getJSON("userID", userID, getJSON("loginID", loginID, null));
        JSONObject jsonObject = getJSON(new String[]{"userID", "loginID"},
                                        new String[]{userID, loginID});

        // Timber.i("Request : " + jsonObject.toString());

        Type outputType = new TypeToken<PerformanceRemote>(){}.getType();
        fcApiService.performanceReport(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<>(remoteCallback, true, outputType));
    }

    @Override
    public void registerMobile(int type, String action, String userID, Repository.RemoteCallback<String> remoteCallback) {

        String flag = "";

        if (type == 1){
            flag = "ResendOTP";
        }

        /*
        JSONObject jsonObject = getJSON("len", "6",
                                        getJSON("mobno", userID,
                                        getJSON("flag", flag,
                                        getJSON("loginAction", action,
                                        getJSON("login_type","customer",null)))));

        */

        JSONObject jsonObject = getJSON(new String[]{"len", "mobno", "flag", "loginAction", "login_type"},
                                        new String[]{ "6", userID, flag, action, "customer"});

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.registerMobile(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<String>(remoteCallback));
    }

    @Override
    public void newLoanApplication(String login, Repository.RemoteCallback<String> remoteCallback) {
        // JSONObject jsonObject = getJSON("userid", login, getJSON("platform","ANDROID", null));
        JSONObject jsonObject = getJSON(new String[]{"userid", "platform"},
                                        new String[]{ login, "android"});


        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.newLoanApplication(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<String>(remoteCallback));
    }

    @Override
    public void dashboard(String userID, String login_type, String emp_id, String level, Repository.RemoteCallback<ArrayList<DashboardItem>> remoteCallback) {
        /*
        JSONObject jsonObject = getJSON("userid", userID,
                getJSON("login_type", login_type,
                        getJSON("emp_id", emp_id,
                                getJSON("level", level, null))));
        */

        JSONObject jsonObject = getJSON(new String[]{"userid", "login_type", "emp_id", "level"},
                                        new String[]{ userID, login_type, emp_id, level});

        // Timber.i("Request : " + jsonObject.toString());

        Type outputType = new TypeToken<ArrayList<DashboardItem>>(){}.getType();

        fcApiService.dashboard(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<ArrayList<DashboardItem>>
                        (remoteCallback, true , outputType));
    }

    @Override
    public void applicant(String appFormID, Repository.RemoteCallback<ApplicantList> remoteCallback) {

        JSONObject jsonObject = getJSON("appid", appFormID, null);
        Type outputType = new TypeToken<ApplicantList>(){}.getType();

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.applicantData(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<ApplicantList>(remoteCallback, true, outputType));

    }

    @Override
    public void savePersonal(String appFormID, String primary, boolean applicantExists, int applicantID, PersonalDetailRemote personalDetailRemote, Repository.RemoteCallback<SaveRemote> remoteCallback) {

        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject = new JSONObject( gson.toJson(personalDetailRemote) );
            jsonObject.put("verifyPanno", ""+jsonObject.getString("verifyPanno").equalsIgnoreCase("Y"));
            jsonObject.put("verifyAadharNo", ""+jsonObject.getString("verifyAadharNo").equalsIgnoreCase("Y"));
            jsonObject.put("appid", appFormID);
            jsonObject.put("userExists", ""+applicantExists);
            jsonObject.put("loanid", ""+applicantID);
            jsonObject.put("applicationStatus", primary);

        }catch (Exception e){
            Timber.e(e);
        }

        // Timber.i("Personal Request : " + jsonObject.toString());

        Type outputType = new TypeToken<SaveRemote>(){}.getType();
        fcApiService.savePersonal(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<SaveRemote>(remoteCallback, true, outputType));
    }

    @Override
    public void saveContact(String appFormID, String userType, String primary, boolean applicantExists, int applicantID, ContactDetailRemote contactDetailRemote, Repository.RemoteCallback<SaveRemote> remoteCallback) {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject = new JSONObject( gson.toJson(contactDetailRemote) );
            jsonObject.put("appid", appFormID);
            jsonObject.put("login_type", userType);
            jsonObject.put("userExists", ""+applicantExists);
            jsonObject.put("loanid", ""+applicantID);
            jsonObject.put("applicationStatus", primary);
        }catch (Exception e){
            Timber.e(e);
        }

        // Timber.i("Contact Request : " + jsonObject.toString());

        Type outputType = new TypeToken<SaveRemote>(){}.getType();
        fcApiService.saveContact(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<String>(remoteCallback, true, outputType));
    }

    @Override
    public void saveOccupation(String appFormID, String primary, boolean applicantExists, int applicantID, EmploymentDetailRemote employmentDetailRemote, Repository.RemoteCallback<SaveRemote> remoteCallback) {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject = new JSONObject( gson.toJson(employmentDetailRemote) );
            jsonObject.put("appid", appFormID);
            jsonObject.put("userExists", ""+applicantExists);
            jsonObject.put("loanid", ""+applicantID);
            jsonObject.put("applicationStatus", primary);

        }catch (Exception e){
            Timber.e(e);
        }

        // Timber.i("Occupation Request : " + jsonObject.toString());

        Type outputType = new TypeToken<SaveRemote>(){}.getType();
        fcApiService.saveOccupation(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<String>(remoteCallback, true, outputType));
    }

    @Override
    public void createLead(int userType, String userTypeValue, CreateLeadRequest leadRequest, Repository.RemoteCallback<String> remoteCallback) {
        Gson gson = new Gson();

        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject = new JSONObject( gson.toJson(leadRequest));

            switch (userType){
                case 0:
                    jsonObject.put("cust", userTypeValue);
                    break;

                case 1:
                    jsonObject.put("empcode", userTypeValue);
                    jsonObject.put("type", "Employee");
                    break;

                case 2:
                    jsonObject.put("channelpartner", userTypeValue);
                    jsonObject.put("type", "Channel Partner");
                    break;
            }

        }catch (Exception e){}

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.createLead(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<String>(remoteCallback));
    }

    @Override
    public void saveDocuments(String cacheDirectory, String appFormID, String primary, boolean applicantExists, int applicantID, HashMap<String, String> documents, Repository.RemoteCallback<SaveRemote> remoteCallback) {
        JSONObject jsonObject =
                getJSON("appid", appFormID,
                getJSON("userExists", ""+applicantExists,
                        getJSON("loanid", applicantID,
                                getJSON("applicationStatus", primary, null))));

        // APPLICANT ID IS INTEGER
        /*
        JSONObject jsonObject = getJSON(new String[]{"appid", "userExists", "loanid", "applicationStatus"},
                                        new String[]{appFormID, ""+applicantExists, ""+applicantID, primary});

        */

        // Timber.i("Request : " + jsonObject.toString());

        RequestBody data = RequestBody.create(MediaType.parse("text/plain"), addRequestAndroid(jsonObject));
        MultipartBody.Part[] arrayList = new MultipartBody.Part[DocumentList.array.length];

        for (int i = 0; i < DocumentList.array.length ; i++){
            try {

                String filePath = documents.get(DocumentList.array[i]);
                File temp = new File( cacheDirectory,"temp.tmp" );
                temp.createNewFile();

                if (filePath == null)
                    filePath = temp.getAbsolutePath();

                MediaType mediaType = MediaType.parse("*/*");

                if (filePath.contains(".jpg"))
                    mediaType = MediaType.parse("image/*");

                if (filePath.contains(".pdf"))
                    mediaType = MediaType.parse("application/pdf");

                File uploadFile = new File(filePath);
                RequestBody file_body = RequestBody.create(mediaType, uploadFile);

                arrayList[i] = MultipartBody.Part.createFormData(DocumentList.array[i], uploadFile.getName(), file_body);

                // Timber.i("Document  "+DocumentList.array[i] + " Exists: "+uploadFile.exists()+ " Type " + mediaType.type()+ "  Document Path : " + uploadFile.getAbsolutePath());
                Timber.i("Upload Request Header : "+ arrayList[i].headers());

            }catch (Exception e){
                Timber.e(e);
            }
        }

        Type outputType = new TypeToken<SaveRemote>(){}.getType();
        fcApiService.saveDocument(data, arrayList)
                .enqueue(new FCResponseCallback<SaveRemote>(remoteCallback, true, outputType));
    }

    @Override
    public void acceptReview(String appID, Repository.RemoteCallback<String> remoteCallback) {
        // String jsonString = getJSONString("appid", appID, getJSONString("level", "1", null));
        JSONObject jsonObject = getJSON(new String[]{"appid", "level"},
                                        new String[]{appID, "1"});


        // Timber.i("Request : " + jsonString.toString());
        fcApiService.saveReviewStatus(addRequestAndroid(jsonObject.toString())).enqueue(new FCResponseCallback<String>(remoteCallback));
    }

    @Override
    public void updateStatus(String appID, String action, String status, Repository.RemoteCallback<String> remoteCallback) {
        /*
        String jsonString = getJSONString("appid", appID,
                getJSONString("action", action,
                        getJSONString("status", "true", null)));
        */

        JSONObject jsonObject = getJSON(new String[]{"appid", "action", "status"},
                                        new String[]{ appID, action, "true"});

        // Timber.i("Request : " + jsonString.toString());
        fcApiService.updateStatus(addRequestAndroid(jsonObject.toString())).enqueue(new FCResponseCallback<String>(remoteCallback));
    }

    @Override
    public void saveLoanDetail(String appID, LoanDetailRequest loanDetailRequest, Repository.RemoteCallback<String> remoteCallback) {
        Gson gson = new Gson();
        String jsonString =  getJSONString("appid", appID, gson.toJson(loanDetailRequest));

        // Timber.i("Request : " + jsonString);

        Timber.i(jsonString);
        fcApiService.saveLoanDetail(addRequestAndroid(jsonString)).enqueue(new FCResponseCallback<String>(remoteCallback));
    }

    @Override
    public void stateMaster(int countryID, Repository.RemoteCallback<MastersState> remoteCallback) {}

    @Override
    public void cityMaster(int stateID, Repository.RemoteCallback<MastersCity> remoteCallback) {}

    @Override
    public void zipcodeMaster(int cityID, Repository.RemoteCallback<MastersZip> remoteCallback) {}

    @Override
    public void getCityState(int countryID, String pincode, Repository.RemoteCallback<MastersCityState> remoteCallback) {
        cancelAll();

        JSONObject jsonObject = getJSON("country_id", countryID, getJSON("zip_id", pincode , null));

        // Timber.i("Request : " + jsonObject.toString());

        Call request = fcApiService.zipcodeCityState(addRequestAndroid(jsonObject));
        request.enqueue(new FCPlainResponseCallback<MastersCityState>(remoteCallback));

        addToQueue(request);
    }

    @Override
    public void fetchLeads(String type, String keyID, String empID, Repository.RemoteCallback<ArrayList<LeadRemote>> remoteCallback) {
        // JSONObject jsonObject = getJSON(keyID, empID, null);
        JSONObject jsonObject = getJSON(new String[]{keyID,"type"},
                                        new String[]{empID, type});

        // Timber.i("Request : " + jsonObject.toString());

        Type outputType = new TypeToken<ArrayList<LeadRemote>>(){}.getType();

        fcApiService.fetchLeadDetails(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<>(remoteCallback, true, outputType));
    }

    @Override
    public void downloadMaster(Repository.RemoteCallback<MastersRemote> remoteRemoteCallback) {
        fcApiService.masters("ANYTHING CAN BE TYPED HERE").enqueue
                (new FCPlainResponseCallback<MastersRemote>(remoteRemoteCallback));
    }

    @Override
    public void submitApplication(String appFormID, Repository.RemoteCallback<String> remoteCallback) {
        JSONObject jsonObject = getJSON("appid",appFormID, null);

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.submitApplication(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<>(remoteCallback));
    }

    @Override
    public void logout(String session_id, String used_id, Repository.RemoteCallback<String> remoteCallback) {

    }

    @Override
    public void verifyPan(String panCard, String appID, String loanID,boolean applicantExists, Repository.RemoteCallback<String> remoteCallback) {
        /*
        JSONObject jsonObject = getJSON("panno",panCard,
                getJSON("loanid",loanID,
                        getJSON("userExists", String.valueOf(applicantExists), getJSON("appid", appID, null) )));

        // Timber.i("Request : " + jsonObject.toString());
        */

        JSONObject jsonObject1 = getJSON(
                new String[]{"panno", "loanid", "userExists", "appid"},
                new String[]{panCard, loanID, String.valueOf(applicantExists), appID});

        fcApiService.verifyPanCard(addRequestAndroid(jsonObject1))
                .enqueue(new FCResponseCallback<>(remoteCallback));
    }

    @Override
    public void verifyAdhaar(String adhaarCard, Repository.RemoteCallback<APIResponse> remoteCallback) {
        JSONObject jsonObject = getJSON("adhaarCard",adhaarCard, null);

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.verifyAdhaarCard(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<>(remoteCallback));
    }

    @Override
    public void verifyOTP(String mobNo, String otp, Repository.RemoteCallback<String> remoteCallback) {
        JSONObject jsonObject = getJSON("mobno", mobNo,
                                getJSON("otp", otp,
                                getJSON("login_type","customer", null)));

        Timber.i("Request : " + jsonObject.toString());

        fcApiService.verifyOTP(addRequestAndroid(jsonObject)).enqueue(new FCResponseCallback<>(remoteCallback));
    }

    @Override
    public void sendOTP(String mobNo, String length, Repository.RemoteCallback<APIResponse> remoteCallback) {
        JSONObject jsonObject = getJSON("mobno", mobNo, getJSON("len", length, null ));

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.sendOTP(addRequestAndroid(jsonObject)).enqueue(new FCResponseCallback<>(remoteCallback));
    }

    @Override
    public void setMpin(String mobNo, String confirmMpin, String Mpin, Repository.RemoteCallback<String> remoteCallback) {
        /*
        JSONObject jsonObject = getJSON("mobno", mobNo,
                                getJSON("conformMPin", confirmMpin,
                                getJSON("mpin", Mpin, getJSON("login_type","customer", null))));

        */

        JSONObject jsonObject = getJSON(new String[]{"mobno", "conformMPin", "mpin", "login_type"},
                                        new String[]{ mobNo, confirmMpin, Mpin, "customer"});

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.setMPIN(addRequestAndroid(jsonObject)).enqueue(new FCResponseCallback<>(remoteCallback));
    }

    @Override
    public void searchApplicant(String appFormNo, Repository.RemoteCallback<ArrayList<LoanAppItem>> remoteCallback) {
        Type outputType = new TypeToken<ArrayList<LoanAppItem>>(){}.getType();

        JSONObject jsonObject = getJSON("appid", appFormNo, null);

        // Timber.i("Request : " + jsonObject.toString());

        fcApiService.searchApplicant(addRequestAndroid(jsonObject)).enqueue(new FCResponseCallback<>(remoteCallback, true, outputType));
    }

    @Override
    public void createApplicant(String appFormID, Repository.RemoteCallback<SaveRemote> remoteCallback) {

        /*
         * This Service Calls Personal API, calling on an existing applicant, will replace its data.
         */

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("appid", appFormID);
            jsonObject.put("userExists", "false");

        }catch (Exception e){
            Timber.e(e);
        }

        // Timber.i("Create Applicant Request : " + jsonObject.toString());

        Type outputType = new TypeToken<SaveRemote>(){}.getType();
        fcApiService.savePersonal(addRequestAndroid(jsonObject))
                .enqueue(new FCResponseCallback<SaveRemote>(remoteCallback, true, outputType));
    }

    public void sendOTPToCustomer(String appFormNo, String employeeId, String len, Repository.RemoteCallback<String> remoteCallback){
        // Type outputType = new TypeToken<otpRemote>(){}.getType();
        // JSONObject jsonObject = getJSON("appid", appFormNo, getJSON("employeeid", employeeId, getJSON("len", len,null)));

        JSONObject jsonObject = getJSON(new String[]{"appid", "employeeid", "len"},
                                        new String[]{appFormNo, employeeId, len});

        fcApiService.sendOTPToCustomer(addRequestAndroid(jsonObject)).enqueue(new FCResponseCallback<>(remoteCallback));

    }

    public void setCustomerfromConformation(String appFormNo, String employeeId, String otp, Repository.RemoteCallback<String> remoteCallback){
        // Type outputType = new TypeToken<ArrayList<LoanApplication>>(){}.getType();
        // JSONObject jsonObject = getJSON("appid", appFormNo, getJSON("employeeId", employeeId, getJSON("otp",otp, null)));

        JSONObject jsonObject = getJSON(new String[]{"appid", "employeeId", "otp"},
                                        new String[]{appFormNo, employeeId, otp});

        fcApiService.setCustomerfromConformation(addRequestAndroid(jsonObject)).enqueue(new FCResponseCallback<>(remoteCallback));
    }

    public  void UpdateLeadStatus(int leadid, String status, Repository.RemoteCallback<String> remoteCallback){
        JSONObject jsonObject = getJSON("leadid", leadid, getJSON("status", status, null));


        // LEAD ID IS INTEGER
        /*
        JSONObject jsonObject = getJSON(new String[]{"leadid", "status"},
                                        new String[]{""+leadid, status});
        */

        fcApiService.UpdateLeadStatus(addRequestAndroid(jsonObject)).enqueue(new FCResponseCallback<>(remoteCallback));
    }

    @Override
    public void generateEasyPayURL(String appFormNo, String deviceInfo, String paymentMode, Repository.RemoteCallback<String> remoteCallback) {
        JSONObject jsonObject = getJSON(
                new String[]{"appid","deviceInfo","paymode"},
                new String[]{appFormNo, deviceInfo, paymentMode});

        fcApiService.EasyPaymentUrl(jsonObject.toString()).enqueue(new FCResponseCallback<>(remoteCallback));
    }

    /*
     * MISC
     */
    private JSONObject getJSON(String[] keys, String[] values){
        JSONObject jsonObject = new JSONObject();

        if (keys.length != values.length)
            throw new RuntimeException("Custom Exception : Keys and Value length don't match.");

        try{
            for (int i = 0 ; i < keys.length ; i++){
                jsonObject.put(keys[i], values[i]);
            }
        }catch (Exception e){
            Timber.e(e);
        }

        return jsonObject;
    }
    private JSONObject getJSON(String key, ObservableField<String> value, JSONObject jsonObject){
        return getJSON(key, ""+value, jsonObject);
    }
    private JSONObject getJSON(String key, int value, JSONObject jsonObject){
        return getJSON(key, ""+value, jsonObject);
    }
    private JSONObject getJSON(String key, String value, JSONObject jsonObject){
        JSONObject op = new JSONObject();

        try{
            if (jsonObject != null)
                op = jsonObject;
            op.put(key, value);
        }catch (Exception e){
            return op;
        }
        return op;
    }
    private String getJSONString(String key, String value, String jsonString){
        JSONObject op = new JSONObject();

        try{
            if (jsonString != null)
                op = new JSONObject(jsonString);

            op.put(key, value);
        }catch (Exception e){
            return op.toString();
        }
        return op.toString();
    }

    private String addRequestAndroid(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.put("platform", "android");

            String jsonString = jsonObject.toString();
            Timber.i("Request : "+ jsonString);

            return  jsonString;
        }catch (Exception e){
            Timber.e(e);
        }
        return json;
    }

    private String addRequestAndroid(JSONObject jsonObject){
        return addRequestAndroid(jsonObject.toString());
    }

    private void addToQueue(Call httpRequest){
        // Add It
        retrofitQueues.add(new WeakReference<>(httpRequest));
    }

    private void cancelAll(){
        for (WeakReference<Call> requestWrapper: retrofitQueues){
            Call httpRequest = requestWrapper.get();

            try{
                if (httpRequest != null)
                    httpRequest.cancel();
            }
            catch (Exception e){
                Timber.e(e);
            }
        }
    }
}
