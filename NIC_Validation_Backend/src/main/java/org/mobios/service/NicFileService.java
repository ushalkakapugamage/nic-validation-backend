package org.mobios.service;


import com.opencsv.exceptions.CsvException;
import net.sf.jasperreports.engine.JRException;
import org.mobios.dao.NicEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NicFileService {
    void addNICFile(MultipartFile file) throws CsvException, IOException;
    List<NicEntity> getAllNICFiles();

    List<String[]> parseCSV(MultipartFile file) throws IOException, CsvException;

    List<NicEntity> processNICFiles(List<String[]> files,String fileName);

    NicEntity validateNIC(String nic,String fileName);


    List<NicEntity> getNicFileById(int id);

    List<NicEntity> getNicFileByFileName(String FileName);

    List<String> getFileNames();

    byte[] generateReports(String title) throws JRException, FileNotFoundException;
}
