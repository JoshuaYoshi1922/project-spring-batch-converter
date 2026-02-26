package com.example.BatchApp.Controller;


import com.example.BatchApp.Service.CsvImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    @Autowired
    private CsvImportService csvimportService;

    @PostMapping("/radiology")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file,
                                            @RequestParam("hospitalName") String hospitalName){
        try {
            csvimportService.importCsv(file, hospitalName);
            return ResponseEntity.ok("File imported successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error importing file: " + e.getMessage());
        }
    }
}
