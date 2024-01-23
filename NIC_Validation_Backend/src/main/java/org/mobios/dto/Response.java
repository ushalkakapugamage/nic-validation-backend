package org.mobios.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class Response {
    HashMap <String,Object> response = new HashMap<>();

    public void setResponse(String message,Object body) {
        this.response.put(message, body);
    }
}
