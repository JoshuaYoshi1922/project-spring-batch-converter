package com.example.BatchApp.Enity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "radiology_exams")
public class RadiologyExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    private String description;
    private String cptCode;
    private String internalCode;
    private String setting;
    private String patientClass;
    private BigDecimal grossCharge;
    private BigDecimal discountCashPrice;
    private String payerName;
    private String planName;
    private BigDecimal negotiatedRate;
    private String methodology;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCptCode() {
        return cptCode;
    }

    public void setCptCode(String cptCode) {
        this.cptCode = cptCode;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getPatientClass() {
        return patientClass;
    }

    public void setPatientClass(String patientClass) {
        this.patientClass = patientClass;
    }

    public BigDecimal getGrossCharge() {
        return grossCharge;
    }

    public void setGrossCharge(BigDecimal grossCharge) {
        this.grossCharge = grossCharge;
    }

    public BigDecimal getDiscountCashPrice() {
        return discountCashPrice;
    }

    public void setDiscountCashPrice(BigDecimal discountCashPrice) {
        this.discountCashPrice = discountCashPrice;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getNegotiatedRate() {
        return negotiatedRate;
    }

    public void setNegotiatedRate(BigDecimal negotiatedRate) {
        this.negotiatedRate = negotiatedRate;
    }

    public String getMethodology() {
        return methodology;
    }

    public void setMethodology(String methodology) {
        this.methodology = methodology;
    }
}
