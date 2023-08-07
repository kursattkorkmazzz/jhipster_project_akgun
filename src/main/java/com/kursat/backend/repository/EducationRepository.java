package com.kursat.backend.repository;

import com.kursat.backend.domain.Education;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Education entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    @Query(value = "SELECT * FROM Education where applicants_id = ?1",nativeQuery = true)
    Education findByApplicantId(Long applicantId);

    @Query(value = "DELETE FROM Education where applicants_id = ?1",nativeQuery = true)
    Education deleteByApplicantId(Long applicantId);
}
