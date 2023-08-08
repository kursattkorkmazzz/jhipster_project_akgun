package com.kursat.backend.web.rest;

import com.kursat.backend.domain.Applicants;
import com.kursat.backend.repository.ApplicationRepository.ApplicantsRepository;
import com.kursat.backend.utils.DTO.FilterDTO;
import com.kursat.backend.utils.DTO.PagingDTO;
import com.kursat.backend.utils.DTO.SortingDTO;

import java.util.Optional;
import java.net.URI;
import java.net.URISyntaxException;
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
     * POST /applicants : Create a new applicant.
     */
    @PostMapping("/applicant")
    public ResponseEntity<Applicants> createApplicants(@RequestBody Applicants applicant) throws URISyntaxException {

        if (applicant.getId() != null) {
            return ResponseEntity.badRequest().header("APPLICANT-ERROR","You cannot specify the applicant ID").build();
        }

        
        Applicants result = applicantsRepository.save(applicant);

        return ResponseEntity
            .created(new URI("/applicant/" + result.getId()))
            .body(result);
    }

    /**
     * PATCH  /applicants/{applicant_id} : Partial updates given fields of an existing applicant, field will ignore if it is null
     */
    @PatchMapping(value = "/applicant/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Applicants> partialUpdateEducation(
        @PathVariable(value = "id", required = true) final Long id,
        @RequestBody Applicants applicant
    ) throws URISyntaxException {

        
        if (applicant.getId() == null) {
            return ResponseEntity.badRequest().header("APPLICANT-ERROR","Applicant ID cannot be null!").build();

        }

        if (!applicantsRepository.existsById(id)) {
             return ResponseEntity.badRequest().header("APPLICANT-ERROR","Applicant couldn't find!").build();
        }

        Applicants result = applicantsRepository.findUniqueByApplicantId(id);

        if(result == null){
            return ResponseEntity.badRequest().build();
        }

        if(applicant.getName() != null){
            result.setName(applicant.getName());
        }
       if(applicant.getSurname() != null){
            result.setSurname(applicant.getSurname());
        }
        if(applicant.getAge() != null){
            result.setAge(applicant.getAge());
        }
        if(applicant.getEmail() != null){
            result.setEmail(applicant.getEmail());
        } 
        if(applicant.getPhone() != null){
            result.setPhone(applicant.getPhone());
        } 

        Applicants newResult = applicantsRepository.save(result);

        return ResponseEntity.ok().body(newResult);
      
    }

    /**
     * GET  /applicants : get all the applicants by doing paging,sorting and filtering.
     */
    @GetMapping("/applicants")
    public List<Applicants> getAllApplicants(PagingDTO paging, SortingDTO sorting, FilterDTO filter) {
        
        String paginationSQL = applicantsRepository.getPaginationSQLQuery(paging);
        String orderingSQL = applicantsRepository.getSortingSQLQuery(sorting);
        String filterSQL = applicantsRepository.getFilterSQLQuery(filter);

        return applicantsRepository.runSQL(filterSQL + orderingSQL + paginationSQL);
    }
    

    /**
     * GET /applicants/:id : get the "id" applicants.
    */
    @GetMapping("/applicant/{id}")
    public ResponseEntity<Applicants> getApplicants(@PathVariable Long id) {
        Optional<Applicants> applicants = applicantsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicants);
    }


    /**
     * DELETE  /applicant/{applicant_id} : delete the applicant with id {id}
     *
     */
    @DeleteMapping("/applicant/{id}")
    public ResponseEntity<Integer> deleteApplicants(@PathVariable(name = "id") Long applicant_id) {
        if(applicant_id == null){
            return ResponseEntity.badRequest().header("APPLICANT_ERROR",   "The id is requred for deleting").build();
        }
    
        int result = applicantsRepository.deleteUniqueByApplicantId(applicant_id);
        return ResponseEntity
            .ok()
            .body(result);
    }

    /**
     * DELETE  /applicants: delete all applicants
     *
     */
    @DeleteMapping("/applicants")
    public ResponseEntity<Integer> deleteAllApplicants() {
        int result = applicantsRepository.deleteAllApplicant();
        return ResponseEntity
            .ok()
            .body(result);
    }

}

