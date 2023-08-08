package com.kursat.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kursat.backend.IntegrationTest;
import com.kursat.backend.domain.Education;
import com.kursat.backend.repository.EducationRepository;

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
 * Integration tests for the {@link EducationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EducationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_FIELD = "BBBBBBBBBB";

    private static final String DEFAULT_DEGREE = "AAAAAAAAAA";
    private static final String UPDATED_DEGREE = "BBBBBBBBBB";

    private static final Float DEFAULT_GRADE = 1F;
    private static final Float UPDATED_GRADE = 2F;

    private static final Long DEFAULT_APPLICANT_ID = 1L;
    private static final Long UPDATED_APPLICANT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/educations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEducationMockMvc;

    private Education education;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createEntity(EntityManager em) {
        Education education = new Education()
            .name(DEFAULT_NAME)
            .field(DEFAULT_FIELD)
            .degree(DEFAULT_DEGREE)
            .grade(DEFAULT_GRADE)
            .applicantID(DEFAULT_APPLICANT_ID);
        return education;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createUpdatedEntity(EntityManager em) {
        Education education = new Education()
            .name(UPDATED_NAME)
            .field(UPDATED_FIELD)
            .degree(UPDATED_DEGREE)
            .grade(UPDATED_GRADE)
            .applicantID(UPDATED_APPLICANT_ID);
        return education;
    }

    @BeforeEach
    public void initTest() {
        education = createEntity(em);
    }

    @Test
    @Transactional
    void createEducation() throws Exception {
        int databaseSizeBeforeCreate = educationRepository.findAll().size();
        // Create the Education
        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(education)))
            .andExpect(status().isCreated());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeCreate + 1);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEducation.getField()).isEqualTo(DEFAULT_FIELD);
        assertThat(testEducation.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testEducation.getApplicantID()).isEqualTo(DEFAULT_APPLICANT_ID);
    }

    @Test
    @Transactional
    void createEducationWithExistingId() throws Exception {
        // Create the Education with an existing ID
        education.setId(1L);

        int databaseSizeBeforeCreate = educationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(education)))
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEducations() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(education.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].field").value(hasItem(DEFAULT_FIELD)))
            .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.doubleValue())))
            .andExpect(jsonPath("$.[*].applicantID").value(hasItem(DEFAULT_APPLICANT_ID.intValue())));
    }

    @Test
    @Transactional
    void getEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get the education
        restEducationMockMvc
            .perform(get(ENTITY_API_URL_ID, education.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(education.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.field").value(DEFAULT_FIELD))
            .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.doubleValue()))
            .andExpect(jsonPath("$.applicantID").value(DEFAULT_APPLICANT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEducation() throws Exception {
        // Get the education
        restEducationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education
        Education updatedEducation = educationRepository.findById(education.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEducation are not directly saved in db
        em.detach(updatedEducation);
        updatedEducation
            .name(UPDATED_NAME)
            .field(UPDATED_FIELD)
            .degree(UPDATED_DEGREE)
            .grade(UPDATED_GRADE)
            .applicantID(UPDATED_APPLICANT_ID);

        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEducation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEducation.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testEducation.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testEducation.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testEducation.getApplicantID()).isEqualTo(UPDATED_APPLICANT_ID);
    }

    @Test
    @Transactional
    void putNonExistingEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, education.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(education))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(education))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(education)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation.grade(UPDATED_GRADE);

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEducation.getField()).isEqualTo(DEFAULT_FIELD);
        assertThat(testEducation.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testEducation.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testEducation.getApplicantID()).isEqualTo(DEFAULT_APPLICANT_ID);
    }

    @Test
    @Transactional
    void fullUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation
            .name(UPDATED_NAME)
            .field(UPDATED_FIELD)
            .degree(UPDATED_DEGREE)
            .grade(UPDATED_GRADE)
            .applicantID(UPDATED_APPLICANT_ID);

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEducation.getField()).isEqualTo(UPDATED_FIELD);
        assertThat(testEducation.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testEducation.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testEducation.getApplicantID()).isEqualTo(UPDATED_APPLICANT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, education.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(education))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(education))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(education))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeDelete = educationRepository.findAll().size();

        // Delete the education
        restEducationMockMvc
            .perform(delete(ENTITY_API_URL_ID, education.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
