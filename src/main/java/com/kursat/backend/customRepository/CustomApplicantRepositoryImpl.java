package com.kursat.backend.customRepository;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;

public class CustomApplicantRepositoryImpl implements CustomApplicantRepository {


    @Autowired  
    private EntityManager entityManager;

    @Override
    public String findBySql() {
        return "Test";
    }
    
}