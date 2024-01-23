package org.mobios.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.mobios.dao.NicEntity;
import org.mobios.repository.NicFileRepository;
import org.mobios.service.NicFileService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    NicFileRepository nicFileRepository;

    @Override
    public void addNICFile(MultipartFile[] files) throws IOException, CsvException {
        for (MultipartFile file : files){
            nicFileRepository.saveAll( processNICFiles(parseCSV(file)));
        }


    }

    @Override
    public List<NicEntity> getAllNICFiles() {
        return nicFileRepository.findAll();
    }


    @Override
    public List<NicEntity> getNicFileById(int id) {
        return nicFileRepository.findById(id);
    }

    @Override
    public List<String[]> parseCSV(MultipartFile file) throws IOException, CsvException {
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
    public List<NicEntity> processNICFiles(List<String[]> files) {
        List <NicEntity> data = new ArrayList<>();
        for (String [] file : files){
            if (file[0].length() != 12){
                throw new IllegalArgumentException("Please check the NIC ="+file[0]);
            }
            data.add(validateNIC(file[0]));
        }
        return data;
    }

    @Override
    public NicEntity validateNIC(String nic) {
        LocalDate birthdate = null;
        int age;
        String gender;

        int temp = Integer.parseInt(nic.substring(4,7));
        if (temp > 500){
            gender = "Female";
            temp-=500;
        }else{
            gender="Male";
        }

        birthdate = calculateBirthday(Integer.parseInt(nic.substring(0,4)),temp);

        LocalDate currentDate = LocalDate.now();
        age = Period.between(birthdate,currentDate).getYears();

        NicEntity entity = new NicEntity();
        entity.setNIC(nic);
        entity.setAge(age);
        entity.setGender(gender);
        entity.setBirthdate(birthdate);

        return entity;


    }



    public LocalDate calculateBirthday(int year,int numberOfDays){
        numberOfDays-=1;
        int month = 0;
        int[] days = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
        for (int day : days){
            if (isLeapYear(year) && day == 28){
                day++;
            }
            month++;
            if (numberOfDays > day){
                numberOfDays -= day;
            }else{
                break;
            }

        }
        return LocalDate.of(year,month,numberOfDays);
    }

    private boolean isLeapYear(int year) {
        if (year % 4 == 0){
            if (year % 100 == 0){
                return (year % 400) == 0;
            }else {
                return true;
            }
        }else {
            return false;
        }

    }
}
