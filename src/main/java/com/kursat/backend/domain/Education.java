package com.kursat.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Education.
 */
@Entity
@Table(name = "education")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "field")
    private String field;

    @Column(name = "degree")
    private String degree;

    @Column(name = "grade")
    private Float grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobs", "educations" }, allowSetters = true)
    private Applicants applicants;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Education id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Education name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return this.field;
    }

    public Education field(String field) {
        this.setField(field);
        return this;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDegree() {
        return this.degree;
    }

    public Education degree(String degree) {
        this.setDegree(degree);
        return this;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Float getGrade() {
        return this.grade;
    }

    public Education grade(Float grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public Applicants getApplicants() {
        return this.applicants;
    }

    public void setApplicants(Applicants applicants) {
        this.applicants = applicants;
    }

    public Education applicants(Applicants applicants) {
        this.setApplicants(applicants);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Education)) {
            return false;
        }
        return id != null && id.equals(((Education) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Education{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", field='" + getField() + "'" +
            ", degree='" + getDegree() + "'" +
            ", grade=" + getGrade() +
            "}";
    }
}
