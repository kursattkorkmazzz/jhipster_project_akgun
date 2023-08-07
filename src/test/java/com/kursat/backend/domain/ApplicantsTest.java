package com.kursat.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kursat.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicantsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Applicants.class);
        Applicants applicants1 = new Applicants();
        applicants1.setId(1L);
        Applicants applicants2 = new Applicants();
        applicants2.setId(applicants1.getId());
        assertThat(applicants1).isEqualTo(applicants2);
        applicants2.setId(2L);
        assertThat(applicants1).isNotEqualTo(applicants2);
        applicants1.setId(null);
        assertThat(applicants1).isNotEqualTo(applicants2);
    }
}
