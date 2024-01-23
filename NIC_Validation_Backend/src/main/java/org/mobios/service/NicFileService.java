package org.mobios.service;


import com.opencsv.exceptions.CsvException;
import org.mobios.dao.NicEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NicFileService {
    void addNICFile(MultipartFile[] files) throws CsvException, IOException;
    List<NicEntity> getAllNICFiles();

    List<String[]> parseCSV(MultipartFile file) throws IOException, CsvException;

    List<NicEntity> processNICFiles(List<String[]> files);

    NicEntity validateNIC(String nic);


    List<NicEntity> getNicFileById(int id);
}
