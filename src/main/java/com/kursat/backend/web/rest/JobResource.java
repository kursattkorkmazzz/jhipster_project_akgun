package com.kursat.backend.web.rest;

import com.kursat.backend.domain.Job;
import com.kursat.backend.repository.JobRepository;
import com.kursat.backend.repository.ApplicationRepository.ApplicantsRepository;
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
 * REST controller for managing {@link com.kursat.backend.domain.Job}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JobResource {

    private final Logger log = LoggerFactory.getLogger(JobResource.class);

    private static final String ENTITY_NAME = "job";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobRepository jobRepository;
private final ApplicantsRepository applicantsRepository;
    public JobResource(JobRepository jobRepository, ApplicantsRepository applicantsRepository) {
        this.jobRepository = jobRepository;
        this.applicantsRepository = applicantsRepository;
    }

    /**
     * POST  /applicants/{id}/jobs : Create a new job at specified ID applicant.
     *
     */
    @PostMapping("/applicants/{id}/job")
    public ResponseEntity<Job> createJob(@PathVariable Long id, @RequestBody Job job) throws URISyntaxException {

        if (job.getId() != null) {
            throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
        }

        job.setApplicantID(id);
        Job result = jobRepository.save(job);

        return ResponseEntity
            .created(new URI("/applicants/" + id + "/job"))
            .body(result);
    }

     /**
     * PUT  /applicants/{id}/job : Updates an existing job that is inside of {id} applicant.
     */
    @PutMapping("/applicants/{id}/job")
    public ResponseEntity<Job> updateJob(
        @PathVariable(required = false) final Long id,
        @RequestBody Job job
    ) throws URISyntaxException {

        if (!applicantsRepository.existsById(id)) {
           throw new BadRequestAlertException("The entitiy couldn't find with " + id, ENTITY_NAME, "idnotfound");
        }

        job.setApplicantID(id);

        Job result = jobRepository.save(job);
        return ResponseEntity
            .ok()
            .body(result);
    }

     /**
     * PATCH  /applicants/{id}/job : Partial updates given fields of an existing job, field will ignore if it is null
     */
    @PatchMapping(value = "/applicants/{id}/job", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Job> partialUpdateJob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Job job
    ) throws URISyntaxException {

        
        if (job.getId() == null) {
            throw new BadRequestAlertException("ID cannot be null!", ENTITY_NAME, "idinvalid");
        }

        if (!applicantsRepository.existsById(id)) {
            throw new BadRequestAlertException("The entitiy couldn't find with " + id, ENTITY_NAME, "idnotfound");
        }

           Job result = jobRepository.findUniqueByApplicantId(id,job.getId());
            log.debug(result.getClass().toString());
           
        if(result == null){
            return ResponseEntity.badRequest().build();
        }

        if(job.getPassion() != null){
            result.setPassion(job.getPassion());
        }
       if(job.getDuration() != null){
            result.setDuration(job.getDuration());
        }
        
        Job newResult = jobRepository.save(result);

        return ResponseEntity.ok().body(newResult);
      
    }

    
    /**
     * GET  /jobs : get all the jobs.
     */
    @GetMapping("/jobs")
    public List<Job> getAllJobs() {
        return jobRepository.getAllJob();
    }

    /**
     * GET  /applicants/{applicant_id}/job : get the jobs of applicant with id "id".
     */
    @GetMapping("/applicants/{applicant_id}/job")
    public ResponseEntity<List<Job>> getJob(@PathVariable("applicant_id") Long applicant_id) {
      List<Job> education = jobRepository.findByApplicantId(applicant_id);
      if(education == null){
        return ResponseEntity.noContent().build();
      }
        return ResponseEntity.ok().body(education);
    }


    /**
     * DELETE  /applicants/{applicant_id}/education : delete the all educations of applicant with id {id} if [jb_id] parameter not given.
     *                                                delete specified id if [jb_id] parameter given
     */
    @DeleteMapping("/applicants/{applicant_id}/job")
    public ResponseEntity<Integer> deleteAllJob(@PathVariable Long applicant_id, @RequestParam(name = "jb_id", required = false) Long job_id) {

        int result;
        if(job_id==null){
           result = jobRepository.deleteAllByApplicantId(applicant_id);
        }
        result = jobRepository.deleteUniqueByApplicantId(applicant_id, job_id);
        return ResponseEntity
            .ok()
            .body(result);
    }
}

