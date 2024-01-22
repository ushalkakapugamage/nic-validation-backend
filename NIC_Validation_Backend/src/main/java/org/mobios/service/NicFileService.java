package org.mobios.service;


import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface NicFileService {
    void addNICFile(MultipartFile files);
    List<String[]> getAllNICFiles();

    List<String[]> parseCSV(MultipartFile file) throws IOException, CsvValidationException;

    void processNICFiles(List<String[]> files);

    void validateNIC(String nic);




}
