package com.example.BatchApp.Enity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String address;

    @OneToOne(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<RadiologyExam> exams;

    public Hospital() {}

    public Hospital(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RadiologyExam> getExams() {
        return exams;
    }

    public void setExams(List<RadiologyExam> exams) {
        this.exams = exams;
    }
}
