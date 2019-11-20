package com.leo.structure2019.data.pojo.db.applicant_info;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "applicant_document")
public class ApplicantDocument {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "document_id")
    public int documentID;

    @ColumnInfo(name = "applicant_id")
    public int applicantID;

    public String type = "";
    public String path = "";
    public boolean uploaded = false;
}
