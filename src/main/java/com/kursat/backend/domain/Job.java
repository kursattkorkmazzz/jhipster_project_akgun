package com.kursat.backend.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "passion")
    private String passion;

    @Column(name = "duration")
    private Float duration;

    @Column(name = "applicants_id")
    private Long applicantID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Job id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassion() {
        return this.passion;
    }

    public Job passion(String passion) {
        this.setPassion(passion);
        return this;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }

    public Float getDuration() {
        return this.duration;
    }

    public Job duration(Float duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public Long getApplicantID() {
        return this.applicantID;
    }

    public Job applicantID(Long applicantID) {
        this.setApplicantID(applicantID);
        return this;
    }

    public void setApplicantID(Long applicantID) {
        this.applicantID = applicantID;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", passion='" + getPassion() + "'" +
            ", duration=" + getDuration() +
            ", applicantID=" + getApplicantID() +
            "}";
    }
}
