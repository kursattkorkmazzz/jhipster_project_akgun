package com.kursat.backend.repository;


import com.kursat.backend.domain.Job;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Job entity.
 */

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    @Query(value = "SELECT * FROM Job WHERE applicants_id = ?1",nativeQuery = true)
    List<Job> findByApplicantId(Long applicantId);

    @Query(value = "SELECT * FROM Job WHERE applicants_id = ?1 AND id = ?2",nativeQuery = true)
    Job findUniqueByApplicantId(Long applicantId, Long jobID);

    @Query(value = "DELETE FROM Job WHERE applicants_id = ?1",nativeQuery = true)
    Job deleteByApplicantId(Long applicantId);

    @Query(value = "DELETE FROM Job WHERE applicants_id = ?1 AND id = ?2",nativeQuery = true)
    Job deleteUniqueByApplicantId(Long applicantId, Long education_id);

    @Query(value = "SELECT * FROM Job", nativeQuery = true)
    List<Job> getAllJob();
}

