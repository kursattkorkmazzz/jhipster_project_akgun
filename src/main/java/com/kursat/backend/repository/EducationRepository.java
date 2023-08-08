package com.kursat.backend.repository;

import java.util.List;
import com.kursat.backend.domain.Education;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Education entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    @Query(value = "SELECT * FROM Education WHERE applicants_id = ?1",nativeQuery = true)
    List<Education> findByApplicantId(Long applicantId);

     @Query(value = "SELECT * FROM Education WHERE applicants_id = ?1 AND id = ?2",nativeQuery = true)
    Education findUniqueByApplicantId(Long applicantId, Long educationID);

    @Query(value = "DELETE FROM Education WHERE applicants_id = ?1",nativeQuery = true)
    Education deleteByApplicantId(Long applicantId);

    @Query(value = "DELETE FROM Education WHERE applicants_id = ?1 AND id = ?2",nativeQuery = true)
    Education deleteUniqueByApplicantId(Long applicantId, Long education_id);

    @Query(value = "SELECT * FROM Education", nativeQuery = true)
    List<Education> getAllEducation();



}
