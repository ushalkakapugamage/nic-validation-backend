package org.mobios.controller;


import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.mobios.dto.Response;
import org.mobios.service.NicFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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





}
