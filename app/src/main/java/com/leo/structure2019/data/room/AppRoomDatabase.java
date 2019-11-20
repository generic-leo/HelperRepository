package com.leo.structure2019.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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

@Database(entities = {  LoanApplication.class,
                        Applicant.class,
                        ApplicantPersonalDetail.class,
                        ApplicantContactDetail.class,
                        ApplicantOccupation.class,
                        ApplicantFinancial.class,
                        ApplicantDocument.class,
                        Lead.class,


                        Occupation.class,
                        Religion.class,
                        Company.class,
                        Residence.class,
                        Qualification.class,
                        Category.class,
                        Country.class,
                        Promotion.class,

                        LoanDetail.class,
                        Disbursement.class}, version = 10, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase{
    private static AppRoomDatabase INSTANCE;

    // region [ Singleton ]
    public static AppRoomDatabase getDB(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "fc_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
    public static void kill(){
        INSTANCE = null;
    }
    // endregion

    public abstract LoanApplicationDao loanApplication();
}
