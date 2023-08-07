package com.kursat.backend.customRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomApplicantRepository {
    public String findBySql();
}
