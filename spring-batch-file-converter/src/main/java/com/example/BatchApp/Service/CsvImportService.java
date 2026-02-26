package com.example.BatchApp.Service;


import com.example.BatchApp.Enity.Hospital;
import com.example.BatchApp.Enity.RadiologyExam;
import com.example.BatchApp.Repository.HospitalRepository;
import com.example.BatchApp.Repository.RadiologyExamRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static tools.jackson.core.internal.shaded.fdp.JavaBigDecimalParser.parseBigDecimal;

@Service
public class CsvImportService {

    @Autowired
    private RadiologyExamRepository examRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Transactional
    public void importCsv(MultipartFile file, String hospitalName) throws Exception {
        Hospital hospital = hospitalRepository.findByName(hospitalName).orElseGet(() -> {
            Hospital newHospital = new Hospital(hospitalName);
            return hospitalRepository.save(newHospital);
        });

        List<RadiologyExam> batch = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)  // Skip metadata row (row 0)
                    .withCSVParser(parser)
                    .build();

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                // Only process rows with a valid CPT code
                if (row.length < 19 || row[5].isBlank() || !row[6].equals("CPT")) {
                    continue;
                }

                RadiologyExam exam = new RadiologyExam();
                exam.setHospital(hospital);
                exam.setDescription(row[0].trim());
                exam.setCptCode(row[5].trim());
                exam.setInternalCode(row[1].trim());
                exam.setSetting(row[9].trim());
                exam.setPatientClass(row[10].trim());
                exam.setGrossCharge(parseBigDecimal(row[13]));
                exam.setPayerName(row[15].trim());
                exam.setPlanName(row[16].trim());
                exam.setNegotiatedRate(parseBigDecimal(row[18]));
                exam.setMethodology(row[24].trim());

                if (row.length > 14) {
                    exam.setDiscountCashPrice(parseBigDecimal(row[14]));
                } else {
                    // Handle missing data case
                    exam.setDiscountCashPrice(null);
                }

                batch.add(exam);

                // Save in chunks of 500 to avoid memory overload
                if (batch.size() >= 500) {
                    examRepository.saveAll(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                examRepository.saveAll(batch);
            }
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return value == null || value.isBlank() ? null : new BigDecimal(value.trim().replace("$", "").replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}