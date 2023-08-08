package com.kursat.backend.web.rest;

import com.kursat.backend.domain.Education;
import com.kursat.backend.repository.ApplicantsRepository;
import com.kursat.backend.repository.EducationRepository;
import com.kursat.backend.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.kursat.backend.domain.Education}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EducationResource {

    private final Logger log = LoggerFactory.getLogger(EducationResource.class);

    private static final String ENTITY_NAME = "education";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationRepository educationRepository;
    private final ApplicantsRepository applicantsRepository;

    public EducationResource(EducationRepository educationRepository, ApplicantsRepository applicantsRepository) {
        this.educationRepository = educationRepository;
        this.applicantsRepository = applicantsRepository;
    }

    /**
     * POST /applicants/{id}/education : Create a new education at applicant with {id}.
     */
    @PostMapping("/applicants/{id}/education")
    public ResponseEntity<Education> createEducation(@PathVariable Long id, @RequestBody Education education) throws URISyntaxException {

        if (education.getId() != null) {
            throw new BadRequestAlertException("A new education cannot already have an ID", ENTITY_NAME, "idexists");
        }

        education.setApplicantID(id);
        Education result = educationRepository.save(education);

        return ResponseEntity
            .created(new URI("/applicants/" + id + "/education"))
            .body(result);
    }

    /**
     * PUT  /applicants/{id}/education : Updates an existing education that is inside of {id} applicant.
     */
    @PutMapping("/applicants/{id}/education")
    public ResponseEntity<Education> updateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Education education
    ) throws URISyntaxException {

        if (!applicantsRepository.existsById(id)) {
           throw new BadRequestAlertException("The entitiy couldn't find with " + id, ENTITY_NAME, "idnotfound");
        }

        education.setApplicantID(id);

        Education result = educationRepository.save(education);
        return ResponseEntity
            .ok()
            .body(result);
    }

    /**
     * PATCH  /applicants/{applicant_id}/education : Partial updates given fields of an existing education, field will ignore if it is null
     */
    @PatchMapping(value = "/applicants/{id}/education", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Education> partialUpdateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Education education
    ) throws URISyntaxException {

        
        if (education.getId() == null) {
            throw new BadRequestAlertException("ID cannot be null!", ENTITY_NAME, "idinvalid");
        }

        if (!applicantsRepository.existsById(id)) {
            throw new BadRequestAlertException("The entitiy couldn't find with " + id, ENTITY_NAME, "idnotfound");
        }

           Education result = educationRepository.findUniqueByApplicantId(id,education.getId());
            log.debug(result.getClass().toString());
           
        if(result == null){
            return ResponseEntity.badRequest().build();
        }

        if(education.getName() != null){
            result.setName(education.getName());
        }
       if(education.getField() != null){
            result.setField(education.getField());
        }
        if(education.getDegree() != null){
            result.setDegree(education.getDegree());
        }
        if(education.getGrade() != null){
            result.setGrade(education.getGrade());
        } 
        Education newResult = educationRepository.save(result);

        return ResponseEntity.ok().body(newResult);
      
    }

    /**
     * GET  /educations : get all the educations.
     */
    @GetMapping("/educations")
    public List<Education> getAllEducations() {
        return educationRepository.getAllEducation();
    }

    /**
     * GET  /applicants/{applicant_id}/education : get the education of applicant with id "id".
     */
    @GetMapping("/applicants/{applicant_id}/education")
    public ResponseEntity<List<Education>> getEducation(@PathVariable("applicant_id") Long applicant_id) {
      List<Education> education = educationRepository.findByApplicantId(applicant_id);
      if(education == null){
        return ResponseEntity.noContent().build();
      }
        return ResponseEntity.ok().body(education);
    }

    /**
     * DELETE  /applicants/{applicant_id}/education : delete the all educations of applicant with id {id}
     *
     */
    @DeleteMapping("/applicants/{applicant_id}/education")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long applicant_id) {
        educationRepository.deleteByApplicantId(applicant_id);
        return ResponseEntity
            .ok()
            .build();
    }

    /**
     * DELETE  /applicants/{applicant_id}/education/{education_id} : delete the all educations of applicant with id {id}
     */
    @DeleteMapping("/applicants/{applicant_id}/education/{education_id} ")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long applicant_id, @PathVariable Long education_id) {
        log.debug("DELETE MTHOD CALLED");

        //educationRepository.deleteUniqueByApplicantId(id, education_id);
        return ResponseEntity
            .ok()
            .build();
    }

}

