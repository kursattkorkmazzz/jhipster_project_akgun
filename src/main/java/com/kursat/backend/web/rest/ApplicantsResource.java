package com.kursat.backend.web.rest;

import com.kursat.backend.domain.Applicants;

import com.kursat.backend.repository.ApplicantsRepository;
import com.kursat.backend.utils.DTO.FilterDTO;
import com.kursat.backend.utils.DTO.PagingDTO;
import com.kursat.backend.utils.DTO.SortingDTO;

import java.util.Optional;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;


@RestController
@RequestMapping("/api")
@Transactional
public class ApplicantsResource {

    private final Logger log = LoggerFactory.getLogger(ApplicantsResource.class);

    private final ApplicantsRepository applicantsRepository;

    public ApplicantsResource(ApplicantsRepository applicantsRepository) {
        this.applicantsRepository = applicantsRepository;
    }

    /**
     * {@code GET  /applicants} : get all the applicants.
     */
    @GetMapping("/applicants")
    public List<Applicants> getAllApplicants(PagingDTO paging, SortingDTO sorting, FilterDTO filter) {
        Integer page = paging.getPage() * paging.getSize();
        return applicantsRepository.getApplicantsByPaging(paging.getSize(), page);
        /* 
        String SQLQuery = "WHERE";
        if(filter != null){
            SQLQuery += filter.getFilterSQLQuery() + "\n";
        }
        return applicantsRepository.findAllBySQL("WHERE age=20").toString();*/
    }

    /**
     * {@code GET  /applicants/:id} : get the "id" applicants.
    */
    @GetMapping("/applicants/{id}")
    public ResponseEntity<Applicants> getApplicants(@PathVariable Long id) {
        Optional<Applicants> applicants = applicantsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicants);
    }

}
