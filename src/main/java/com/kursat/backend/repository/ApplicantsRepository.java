package com.kursat.backend.repository;

import java.util.List;

import com.kursat.backend.domain.Applicants;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
/**
 * Spring Data JPA repository for the Applicants entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicantsRepository extends JpaRepository<Applicants, Long>{

    @Query(value = "SELECT * FROM Applicants LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Applicants> getApplicantsByPaging(Integer limit, Integer offset);
}
