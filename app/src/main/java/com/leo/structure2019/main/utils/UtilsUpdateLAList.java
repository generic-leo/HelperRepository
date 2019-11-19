package com.leo.structure2019.main.utils;

import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.db.LoanApplication;
import com.leo.homeloan.data.pojo.remote.DashboardItem;

import java.util.ArrayList;

import timber.log.Timber;

public class UtilsUpdateLAList {
    public static void updateLAList(String userID, ArrayList<DashboardItem> response, Repository.RoomRepository repository, String level){
        ArrayList<String> ids = new ArrayList<>();

        if (response != null){
            for (int i = 0 ; i < response.size() ; i++){
                DashboardItem dashboardItem = response.get(i);

                LoanApplication loanApplication = repository.getLoanApplication(dashboardItem.appid);

                if(loanApplication == null){
                    loanApplication = new LoanApplication();
                }

                loanApplication.loginID = userID;
                loanApplication.applicationNo = dashboardItem.appid;

                loanApplication.loanType = dashboardItem.loanType;
                loanApplication.amount = dashboardItem.loanAmount;
                loanApplication.time = dashboardItem.loanDate;
                loanApplication.level = dashboardItem.level;
                loanApplication.status = dashboardItem.status;

                repository.insertLoanApplication(loanApplication);

                // Add IDS
                ids.add(dashboardItem.appid);

                Timber.i("Dashboard : "+ dashboardItem.toString());
            }

            //  if (ids.size() > 0)
            repository.deleteLoanApplications(userID, ids.toArray(new String[ids.size()]), level);
        }
    }
}
