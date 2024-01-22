package org.mobios.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.mobios.service.NicFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class NicFileServiceImpl implements NicFileService{
    @Override
    public void addNICFile(MultipartFile files) {

    }

    @Override
    public List<String[]> getAllNICFiles() {
        return null;
    }

    @Override
    public List<String[]> parseCSV(MultipartFile file) throws IOException, CsvValidationException {
        if (file == null || (!file.getOriginalFilename().toLowerCase().endsWith(".csv") && (!file.getOriginalFilename().toLowerCase().endsWith(".txt")))){
            throw new IllegalArgumentException("Invalid file type. Please provide a CSV or text file.");
        }

        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> records = new ArrayList<>();
        String[] record;
        while ((record = csvReader.readNext()) != null){
            records.add(record);
        }
        return records;
    }

    @Override
    public void processNICFiles(List<String[]> files) {
        for (String [] file : files){
            if (file[0].length() != 12){
                throw new IllegalArgumentException("Please check the NIC ="+file[0]);
            }
            validateNIC(file[0]);
        }

    }

    @Override
    public void validateNIC(String nic) {
        LocalDate birthdate = null;
        Period age;
        String gender;

        int temp = Integer.parseInt(nic.substring(4,7));
        if (temp > 500){
            gender = "Female";
            temp-=500;
        }else{
            gender="Male";
        }


        LocalDate currentDate = LocalDate.now();
        age = Period.between(birthdate,currentDate);


    }
}
