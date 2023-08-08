package com.kursat.backend.repository.ApplicationRepository;
import jakarta.persistence.EntityManager;

import com.kursat.backend.domain.Applicants;
import com.kursat.backend.utils.DTO.FilterDTO;
import com.kursat.backend.utils.DTO.PagingDTO;
import com.kursat.backend.utils.DTO.SortingDTO;

import java.util.List;

import jakarta.persistence.Query;


public class CustomizedApplicantsRepositoryImpl implements CustomizedApplicantsRepository {
    
    private final EntityManager entityManager;

    public CustomizedApplicantsRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public String getPaginationSQLQuery(PagingDTO paging){
        Integer page = paging.getPage() * paging.getSize();
        return " LIMIT "+paging.getSize()+" OFFSET " + page + " ";
    }

    public String getSortingSQLQuery(SortingDTO sorting){
        return " ORDER BY " + sorting.getSortBy() + " " +sorting.getSortType().toUpperCase() + " ";
    }




    public String getFilterSQLQuery(FilterDTO filter){
        
        if (filter.getFilter().size() == 0){
            return "";
        }
        String filterSQL = filter.getFilterSQLQuery();
        return " WHERE " + filterSQL + " ";
    }





    public List<Applicants> runSQL(String SQLQuery){
        Query query = entityManager.createNativeQuery("SELECT * from APPLICANTS " + SQLQuery, Applicants.class);
        return  query.getResultList();
    }

    @Override
    public int deleteAllApplicant() {
        Query deleteAllEducationQuery = entityManager.createNativeQuery("DELETE FROM Education");
        Query deleteAllJobQuery = entityManager.createNativeQuery("DELETE FROM Job");
        Query deleteAllApplicantQuery = entityManager.createNativeQuery("DELETE FROM Applicants");

        deleteAllEducationQuery.executeUpdate();
        deleteAllJobQuery.executeUpdate();
        int result = deleteAllApplicantQuery.executeUpdate();

        return result;
    }

    @Override
    public int deleteUniqueByApplicantId(Long applicantID) {
       
        Query deleteEducationQuery = entityManager.createNativeQuery("DELETE FROM Education WHERE applicants_id = " + applicantID);
        Query deleteJobQuery = entityManager.createNativeQuery("DELETE FROM Job WHERE applicants_id = " + applicantID);
        Query deleteApplicantQuery = entityManager.createNativeQuery("DELETE FROM Applicants WHERE id = " + applicantID);

        deleteEducationQuery.executeUpdate();
        deleteJobQuery.executeUpdate();
        int result = deleteApplicantQuery.executeUpdate();

        return result;
    }
}
