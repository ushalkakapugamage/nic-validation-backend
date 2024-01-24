package org.mobios.advice;

import org.mobios.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class HandleException {


    @ExceptionHandler
    public Response handleExceptions(IllegalArgumentException ex){
        Response response = new Response();
        response.setResponse("status",ex.getMessage());
        return response;
    }
}
