package com.leo.structure2019.main.utils;

import com.leo.homeloan.data.Repository;
import com.leo.homeloan.data.pojo.db.Category;
import com.leo.homeloan.data.pojo.db.Company;
import com.leo.homeloan.data.pojo.db.Country;
import com.leo.homeloan.data.pojo.db.Occupation;
import com.leo.homeloan.data.pojo.db.Promotion;
import com.leo.homeloan.data.pojo.db.Qualification;
import com.leo.homeloan.data.pojo.db.Religion;
import com.leo.homeloan.data.pojo.db.Residence;
import com.leo.homeloan.data.pojo.remote.CategoryRemote;
import com.leo.homeloan.data.pojo.remote.CompanyRemote;
import com.leo.homeloan.data.pojo.remote.CountryRemote;
import com.leo.homeloan.data.pojo.remote.MastersRemote;
import com.leo.homeloan.data.pojo.remote.OccupationRemote;
import com.leo.homeloan.data.pojo.remote.PromotionRemote;
import com.leo.homeloan.data.pojo.remote.QualificationRemote;
import com.leo.homeloan.data.pojo.remote.ReligionRemote;
import com.leo.homeloan.data.pojo.remote.ResidenceRemote;
import com.leo.homeloan.data.shared.SharedPrefRepository;

import java.util.List;

import timber.log.Timber;

public class UtilsUpdateMaster {
    public static void updateMaster(SharedPrefRepository sharedPrefRepository, Repository.RoomRepository repository, MastersRemote remote){
        if (remote != null){
            String version = sharedPrefRepository.getMasterVersion();
            // Only Fetch & Save When The Version Doesn't Match
            if (!remote.getVersion().equalsIgnoreCase(version)){
                saveOccupation(repository, remote.getOccupationMaster());
                saveReligion(repository, remote.getReligionMaster());
                saveCompany(repository, remote.getCompanyMaster());
                saveResidence(repository, remote.getResidenceMaster());
                saveQualification(repository, remote.getQualificationMaster());
                saveCategory(repository, remote.getCategoryMaster());
                saveCountry(repository, remote.getCountry());
                savePromotion(repository, remote.getPromotion());
            }
        }else {
            Timber.i("Master Value Is Null");
        }
    }

    private static void saveOccupation(Repository.RoomRepository repository, List<OccupationRemote> list){
        repository.deleteOccupation();

        for (OccupationRemote or: list){
            Occupation occupation = new Occupation(
                    or.getOccupation_id(),
                    or.getOccupation_name(),
                    or.getOccupation_desc());

            repository.insertOccupation(occupation);
        }
    }

    private static void saveReligion(Repository.RoomRepository repository, List<ReligionRemote> list){
        repository.deleteReligion();

        for (ReligionRemote rr: list){
            Religion religion  = new Religion(
                    rr.getReligion_id(),
                    rr.getReligion_name(),
                    rr.getReligion_desc());

            repository.insertReligion(religion);
        }
    }

    private static void saveCompany(Repository.RoomRepository repository, List<CompanyRemote> list){
        repository.deleteCompany();

        for (CompanyRemote cr: list){
            Company company = new Company(cr.getCompany_id(),
                    cr.getIndustry_id(),
                    cr.getIndustry_desc());

            repository.insertCompany(company);
        }
    }

    private static void saveResidence(Repository.RoomRepository repository, List<ResidenceRemote> list){
        repository.deleteResidence();

        for (ResidenceRemote rr: list){
            Residence residence = new Residence(rr.getResidence_status_id(),
                    rr.getResidence_status_name(),
                    rr.getResidence_status_desc());

            repository.insertResidence(residence);
        }
    }

    private static void saveQualification(Repository.RoomRepository repository, List<QualificationRemote> list){
        repository.deleteQualification();

        for (QualificationRemote qr: list){
            Qualification qualification = new Qualification(
                    qr.getQualification_id(),
                    qr.getQualification_name(),
                    qr.getQualification_desc());

            repository.insertQualification(qualification);
        }
    }

    private static void saveCategory(Repository.RoomRepository repository, List<CategoryRemote> list){
        repository.deleteCategory();

        for (CategoryRemote cr : list){
            Category category = new Category(
                    cr.getCategory_id(),
                    cr.getCategory_name(),
                    cr.getCategory_desc()
            );

            repository.insertCategory(category);
        }
    }

    private static void saveCountry(Repository.RoomRepository repository, List<CountryRemote> list){
        repository.deleteCountry();

        for (CountryRemote cr: list){
            Country country = new Country(cr.getCountry_id(), cr.getCountry_name());
            repository.insertCountry(country);
        }
    }

    private static void savePromotion(Repository.RoomRepository repository, List<PromotionRemote> list){
        repository.deletePromotion();

        for (PromotionRemote cr: list){
            Promotion promotion = new Promotion(cr.getPromotion_id(), cr.getPromotion_desc());
            repository.insertPromotion(promotion);
        }
    }
}
