package com.kursat.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Applicants.
 */
@Entity
@Table(name = "applicants")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Applicants implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private Long age;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicants")
    @JsonIgnoreProperties(value = { "applicants" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicants")
    @JsonIgnoreProperties(value = { "applicants" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Applicants id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Applicants name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Applicants surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getAge() {
        return this.age;
    }

    public Applicants age(Long age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getPhone() {
        return this.phone;
    }

    public Applicants phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Applicants email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.setApplicants(null));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.setApplicants(this));
        }
        this.jobs = jobs;
    }

    public Applicants jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Applicants addJob(Job job) {
        this.jobs.add(job);
        job.setApplicants(this);
        return this;
    }

    public Applicants removeJob(Job job) {
        this.jobs.remove(job);
        job.setApplicants(null);
        return this;
    }

    public Set<Education> getEducations() {
        return this.educations;
    }

    public void setEducations(Set<Education> educations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setApplicants(null));
        }
        if (educations != null) {
            educations.forEach(i -> i.setApplicants(this));
        }
        this.educations = educations;
    }

    public Applicants educations(Set<Education> educations) {
        this.setEducations(educations);
        return this;
    }

    public Applicants addEducation(Education education) {
        this.educations.add(education);
        education.setApplicants(this);
        return this;
    }

    public Applicants removeEducation(Education education) {
        this.educations.remove(education);
        education.setApplicants(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Applicants)) {
            return false;
        }
        return id != null && id.equals(((Applicants) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Applicants{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", age=" + getAge() +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
