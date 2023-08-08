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
    

    @GetMapping("/applicants/test")
    public List<Applicants> testAPI(){
        return applicantsRepository.runSQL("WHERE id=0");
    }

    /**
     * GET  /applicants : get all the applicants by doing paging,sorting and filtering.
     */
    @GetMapping("/applicants")
    public List<Applicants> getAllApplicants(PagingDTO paging, SortingDTO sorting, FilterDTO filter) {
        
        String paginationSQL = applicantsRepository.getPaginationSQLQuery(paging);
        String orderingSQL = applicantsRepository.getSortingSQLQuery(sorting);

        return applicantsRepository.runSQL(orderingSQL + paginationSQL);
        /* 
        String SQLQuery = "WHERE";
        if(filter != null){
            SQLQuery += filter.getFilterSQLQuery() + "\n";
        }
        return applicantsRepository.findAllBySQL("WHERE age=20").toString();*/
    }

    /**
     * GET /applicants/:id : get the "id" applicants.
    */
    @GetMapping("/applicants/{id}")
    public ResponseEntity<Applicants> getApplicants(@PathVariable Long id) {
        Optional<Applicants> applicants = applicantsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicants);
    }

}

