package com.example.BatchApp.Repository;

import com.example.BatchApp.Enity.RadiologyExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RadiologyExamRepository extends JpaRepository<RadiologyExam, Long> {
    // Find all exams by CPT code (core search feature for your app)
    List<RadiologyExam> findByCptCode(String cptCode);

    // Find by CPT code + specific hospital
    List<RadiologyExam> findByCptCodeAndHospitalId(String cptCode, Long hospitalId);

    // Find by CPT code + patient class (inpatient vs outpatient)
    List<RadiologyExam> findByCptCodeAndPatientClass(String cptCode, String patientClass);

    // Find by CPT code + payer (useful for insurance comparisons)
    List<RadiologyExam> findByCptCodeAndPayerName(String cptCode, String payerName);
}
