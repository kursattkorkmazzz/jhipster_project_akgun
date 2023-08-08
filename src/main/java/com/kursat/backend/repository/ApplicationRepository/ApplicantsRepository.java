package com.kursat.backend.repository.ApplicationRepository;


import com.kursat.backend.domain.Applicants;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
/**
 * Spring Data JPA repository for the Applicants entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicantsRepository extends JpaRepository<Applicants, Long>, CustomizedApplicantsRepository{

    
    @Query(value = "SELECT * FROM Applicants WHERE id = ?1 ",nativeQuery = true)
    Applicants findUniqueByApplicantId(Long applicantId);

}

