package com.example.BatchApp.Service;


import com.example.BatchApp.Enity.Hospital;
import com.example.BatchApp.Enity.RadiologyExam;
import com.example.BatchApp.Repository.HospitalRepository;
import com.example.BatchApp.Repository.RadiologyExamRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
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

    @PostConstruct
    public void runOnStartup() throws Exception {
        importCsvFromResources();
    }

    public void importCsvFromResources() throws Exception {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("nkch_radiology_cpt.csv");

        if (inputStream == null) {
            throw new RuntimeException("CSV file not found in resources folder");
        }

        importFile(inputStream, "NKCH");
    }

    public void importFile(InputStream inputStream, String hospitalName) throws Exception {
        Hospital hospital = hospitalRepository.findByName(hospitalName)
                .orElseGet(() -> hospitalRepository.save(new Hospital(hospitalName)));

        List<RadiologyExam> batch = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .build();

        String[] row;
        while ((row = csvReader.readNext()) != null) {
            if (row.length < 19 || row[5].isBlank() || !row[6].equals("CPT")) {
                continue;
            }

            batch.add(mapRowToExam(row, hospital));

            if (batch.size() >= 500) {
                examRepository.saveAll(batch);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            examRepository.saveAll(batch);
        }

        System.out.println("CSV Import complete!");
    }

    private RadiologyExam mapRowToExam(String[] row, Hospital hospital) {
        RadiologyExam exam = new RadiologyExam();
        exam.setHospital(hospital);
        exam.setDescription(row[0].trim());
        exam.setCptCode(row[5].trim());
        exam.setInternalCode(row[1].trim());
        exam.setSetting(row[9].trim());
        exam.setPatientClass(row[10].trim());
        exam.setGrossCharge(parseBigDecimal(row[13]));
        exam.setDiscountedCashPrice(parseBigDecimal(row[14]));
        exam.setPayerName(row[15].trim());
        exam.setPlanName(row[16].trim());
        exam.setNegotiatedRate(parseBigDecimal(row[18]));
        exam.setMethodology(row[24].trim());
        return exam;
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return value == null || value.isBlank() ? null
                    : new BigDecimal(value.trim().replace("$", "").replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}



