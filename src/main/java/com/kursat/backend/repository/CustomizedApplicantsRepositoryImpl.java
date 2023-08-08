package com.kursat.backend.repository;
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
        Query query = entityManager.createNativeQuery("SELECT * FROM Applicants ap JOIN Job jb ON ap.id = jb.applicant_id;");

        return "WHERE ";
    }

    public List<Applicants> runSQL(String SQLQuery){
        Query query = entityManager.createNativeQuery("SELECT * FROM Applicants " + SQLQuery, Applicants.class);
        return  query.getResultList();
    }
}
