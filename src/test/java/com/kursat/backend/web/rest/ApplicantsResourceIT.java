package com.kursat.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kursat.backend.IntegrationTest;
import com.kursat.backend.domain.Applicants;
import com.kursat.backend.repository.ApplicantsRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApplicantsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicantsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_AGE = 1L;
    private static final Long UPDATED_AGE = 2L;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/applicants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicantsRepository applicantsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicantsMockMvc;

    private Applicants applicants;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicants createEntity(EntityManager em) {
        Applicants applicants = new Applicants()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .age(DEFAULT_AGE)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL);
        return applicants;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicants createUpdatedEntity(EntityManager em) {
        Applicants applicants = new Applicants()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .age(UPDATED_AGE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        return applicants;
    }

    @BeforeEach
    public void initTest() {
        applicants = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicants() throws Exception {
        int databaseSizeBeforeCreate = applicantsRepository.findAll().size();
        // Create the Applicants
        restApplicantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicants)))
            .andExpect(status().isCreated());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeCreate + 1);
        Applicants testApplicants = applicantsList.get(applicantsList.size() - 1);
        assertThat(testApplicants.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicants.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testApplicants.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testApplicants.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testApplicants.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createApplicantsWithExistingId() throws Exception {
        // Create the Applicants with an existing ID
        applicants.setId(1L);

        int databaseSizeBeforeCreate = applicantsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicants)))
            .andExpect(status().isBadRequest());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplicants() throws Exception {
        // Initialize the database
        applicantsRepository.saveAndFlush(applicants);

        // Get all the applicantsList
        restApplicantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicants.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getApplicants() throws Exception {
        // Initialize the database
        applicantsRepository.saveAndFlush(applicants);

        // Get the applicants
        restApplicantsMockMvc
            .perform(get(ENTITY_API_URL_ID, applicants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicants.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingApplicants() throws Exception {
        // Get the applicants
        restApplicantsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicants() throws Exception {
        // Initialize the database
        applicantsRepository.saveAndFlush(applicants);

        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();

        // Update the applicants
        Applicants updatedApplicants = applicantsRepository.findById(applicants.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplicants are not directly saved in db
        em.detach(updatedApplicants);
        updatedApplicants.name(UPDATED_NAME).surname(UPDATED_SURNAME).age(UPDATED_AGE).phone(UPDATED_PHONE).email(UPDATED_EMAIL);

        restApplicantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicants.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicants))
            )
            .andExpect(status().isOk());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
        Applicants testApplicants = applicantsList.get(applicantsList.size() - 1);
        assertThat(testApplicants.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicants.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testApplicants.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testApplicants.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApplicants.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingApplicants() throws Exception {
        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();
        applicants.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicants.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicants() throws Exception {
        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();
        applicants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicants() throws Exception {
        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();
        applicants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicants)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicantsWithPatch() throws Exception {
        // Initialize the database
        applicantsRepository.saveAndFlush(applicants);

        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();

        // Update the applicants using partial update
        Applicants partialUpdatedApplicants = new Applicants();
        partialUpdatedApplicants.setId(applicants.getId());

        partialUpdatedApplicants.age(UPDATED_AGE).phone(UPDATED_PHONE);

        restApplicantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicants))
            )
            .andExpect(status().isOk());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
        Applicants testApplicants = applicantsList.get(applicantsList.size() - 1);
        assertThat(testApplicants.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicants.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testApplicants.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testApplicants.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApplicants.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateApplicantsWithPatch() throws Exception {
        // Initialize the database
        applicantsRepository.saveAndFlush(applicants);

        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();

        // Update the applicants using partial update
        Applicants partialUpdatedApplicants = new Applicants();
        partialUpdatedApplicants.setId(applicants.getId());

        partialUpdatedApplicants.name(UPDATED_NAME).surname(UPDATED_SURNAME).age(UPDATED_AGE).phone(UPDATED_PHONE).email(UPDATED_EMAIL);

        restApplicantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicants))
            )
            .andExpect(status().isOk());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
        Applicants testApplicants = applicantsList.get(applicantsList.size() - 1);
        assertThat(testApplicants.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicants.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testApplicants.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testApplicants.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApplicants.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingApplicants() throws Exception {
        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();
        applicants.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicants() throws Exception {
        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();
        applicants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicants() throws Exception {
        int databaseSizeBeforeUpdate = applicantsRepository.findAll().size();
        applicants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(applicants))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicants in the database
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicants() throws Exception {
        // Initialize the database
        applicantsRepository.saveAndFlush(applicants);

        int databaseSizeBeforeDelete = applicantsRepository.findAll().size();

        // Delete the applicants
        restApplicantsMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicants.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Applicants> applicantsList = applicantsRepository.findAll();
        assertThat(applicantsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
