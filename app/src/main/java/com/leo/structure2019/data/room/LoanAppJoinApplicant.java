package com.leo.structure2019.data.room;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.leo.homeloan.data.pojo.db.Applicant;
import com.leo.homeloan.data.pojo.db.LoanApplication;

import java.util.List;

public class LoanAppJoinApplicant {
    @Embedded
    public LoanApplication loanApplication;

    @Relation(  parentColumn = "loan_id",
                entityColumn = "application_no",
                entity = Applicant.class)
    public List<Applicant> applicantList;
}
