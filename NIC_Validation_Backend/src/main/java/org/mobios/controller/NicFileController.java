package org.mobios.controller;


import com.opencsv.exceptions.CsvException;
import net.sf.jasperreports.engine.JRException;
import org.mobios.dto.Response;
import org.mobios.service.NicFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.io.FileNotFoundException;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/NIC")
public class NicFileController {

    @Autowired
    NicFileService nicFileService;

    @PostMapping("/addNicFile")
    public Response addNicFile(@RequestParam("file")MultipartFile[] files) throws IOException, CsvException {
        Response response = new Response();
        nicFileService.addNICFile(files);
        response.setResponse("status","File Saved Successfully");
        return  response;
    }

    @GetMapping("/getAllNicFiles")
    public ResponseEntity<Response> getAllFiles(){
        Response response = new Response();
        response.setResponse("data",nicFileService.getAllNICFiles());
        return  new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    @GetMapping("/getNicFileById")
    public ResponseEntity<Response> getNicFileById(@RequestParam("id")int id){
        Response response = new Response();
        response.setResponse("data",nicFileService.getNicFileById(id));
        return  new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    @GetMapping("/getNicFileByFileName")
    public ResponseEntity<Response> getNicFileByFileName(@RequestParam("name")String fileName){
        Response response = new Response();
        response.setResponse("data",nicFileService.getNicFileByFileName(fileName));
        return  new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    @GetMapping("/getFileNames")
    public ResponseEntity<Response> getFileNames(){
        Response response = new Response();
        response.setResponse("data",nicFileService.getFileNames());
        return  new ResponseEntity<Response>(response, HttpStatus.OK);

    }

    @GetMapping("/generateReports")
    public ResponseEntity<byte[]> generateReports(@RequestParam("title") String title) throws JRException, FileNotFoundException {
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION,"inline;filename = NIC-File.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(nicFileService.generateReports(title));

    }





}
