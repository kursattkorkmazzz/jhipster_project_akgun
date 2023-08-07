package com.kursat.backend.web.rest;

import com.kursat.backend.domain.Applicants;
import com.kursat.backend.repository.ApplicantsRepository;
import com.kursat.backend.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kursat.backend.domain.Applicants}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApplicantsResource {

    private final Logger log = LoggerFactory.getLogger(ApplicantsResource.class);

    private static final String ENTITY_NAME = "applicants";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicantsRepository applicantsRepository;

    public ApplicantsResource(ApplicantsRepository applicantsRepository) {
        this.applicantsRepository = applicantsRepository;
    }

    /**
     * {@code POST  /applicants} : Create a new applicants.
     *
     * @param applicants the applicants to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicants, or with status {@code 400 (Bad Request)} if the applicants has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/applicants")
    public ResponseEntity<Applicants> createApplicants(@RequestBody Applicants applicants) throws URISyntaxException {
        log.debug("REST request to save Applicants : {}", applicants);
        if (applicants.getId() != null) {
            throw new BadRequestAlertException("A new applicants cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Applicants result = applicantsRepository.save(applicants);
        return ResponseEntity
            .created(new URI("/api/applicants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /applicants/:id} : Updates an existing applicants.
     *
     * @param id the id of the applicants to save.
     * @param applicants the applicants to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicants,
     * or with status {@code 400 (Bad Request)} if the applicants is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicants couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/applicants/{id}")
    public ResponseEntity<Applicants> updateApplicants(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Applicants applicants
    ) throws URISyntaxException {
        log.debug("REST request to update Applicants : {}, {}", id, applicants);
        if (applicants.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicants.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Applicants result = applicantsRepository.save(applicants);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicants.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /applicants/:id} : Partial updates given fields of an existing applicants, field will ignore if it is null
     *
     * @param id the id of the applicants to save.
     * @param applicants the applicants to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicants,
     * or with status {@code 400 (Bad Request)} if the applicants is not valid,
     * or with status {@code 404 (Not Found)} if the applicants is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicants couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/applicants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Applicants> partialUpdateApplicants(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Applicants applicants
    ) throws URISyntaxException {
        log.debug("REST request to partial update Applicants partially : {}, {}", id, applicants);
        if (applicants.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicants.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Applicants> result = applicantsRepository
            .findById(applicants.getId())
            .map(existingApplicants -> {
                if (applicants.getName() != null) {
                    existingApplicants.setName(applicants.getName());
                }
                if (applicants.getSurname() != null) {
                    existingApplicants.setSurname(applicants.getSurname());
                }
                if (applicants.getAge() != null) {
                    existingApplicants.setAge(applicants.getAge());
                }
                if (applicants.getPhone() != null) {
                    existingApplicants.setPhone(applicants.getPhone());
                }
                if (applicants.getEmail() != null) {
                    existingApplicants.setEmail(applicants.getEmail());
                }

                return existingApplicants;
            })
            .map(applicantsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicants.getId().toString())
        );
    }

    /**
     * {@code GET  /applicants} : get all the applicants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicants in body.
     */
    @GetMapping("/applicants")
    public List<Applicants> getAllApplicants() {
        log.debug("REST request to get all Applicants");
        return applicantsRepository.findAll();
    }

    /**
     * {@code GET  /applicants/:id} : get the "id" applicants.
     *
     * @param id the id of the applicants to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicants, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/applicants/{id}")
    public ResponseEntity<Applicants> getApplicants(@PathVariable Long id) {
        log.debug("REST request to get Applicants : {}", id);
        Optional<Applicants> applicants = applicantsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicants);
    }

    /**
     * {@code DELETE  /applicants/:id} : delete the "id" applicants.
     *
     * @param id the id of the applicants to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/applicants/{id}")
    public ResponseEntity<Void> deleteApplicants(@PathVariable Long id) {
        log.debug("REST request to delete Applicants : {}", id);
        applicantsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
