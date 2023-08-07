package com.kursat.backend.repository;

import com.kursat.backend.domain.Job;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Job entity.
 */

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    @Query(value = "SELECT * FROM job WHERE applicants_id = ?1",nativeQuery = true)
    Job findByApplicantId(Long applicantId);
}
