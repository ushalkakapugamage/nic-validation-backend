package org.mobios.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.mobios.dao.NicEntity;
import org.mobios.repository.NicFileRepository;
import org.mobios.service.NicFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NicFileServiceImpl implements NicFileService{
    private String fileName;

    @Autowired
    NicFileRepository nicFileRepository;

    @Override
    public void addNICFile(MultipartFile[] files) throws IOException, CsvException {
        for (MultipartFile file : files){
            fileName = file.getOriginalFilename();
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
    public List<NicEntity> getNicFileByFileName(String FileName) {
        return nicFileRepository.findByFileName(FileName);
    }

    @Override
    public List<String> getFileNames() {
        return nicFileRepository.findDistinctFileName();
    }

    @Override
    public byte[] generateReports(String title) throws JRException, FileNotFoundException {
        List<NicEntity> data = nicFileRepository.findByFileName(fileName);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(data);
        JasperReport compileReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/nicFile.jrxml"));
        HashMap<String,Object> map = new HashMap<>();
        map.put("title",title.toUpperCase());
        JasperPrint report = JasperFillManager.fillReport(compileReport, map, beanCollectionDataSource);
        byte[] pdf = JasperExportManager.exportReportToPdf(report);
        return pdf;

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
    public List<NicEntity> processNICFiles(List<String[]> records) {
        String regex = "^[0-9]{12}$";
        Pattern pattern = Pattern.compile(regex);


        List <NicEntity> data = new ArrayList<>();
        for (String [] record : records){
            Matcher matcher = pattern.matcher(record[0]);
            if (!matcher.matches()){
                throw new IllegalArgumentException("Invalid NIC Format"+record[0]);
            }
            data.add(validateNIC(record[0]));
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
        entity.setBirthdate(Date.valueOf(birthdate));
        entity.setFileName(fileName);

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
