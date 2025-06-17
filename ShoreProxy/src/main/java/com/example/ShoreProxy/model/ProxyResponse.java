package com.example.ShoreProxy.model;

import java.io.Serializable;


import java.util.Map;

public class ProxyResponse implements Serializable {
    private int statusCode;
    private Map<String, String> headers; // ✅ change to Map
    private String body;

    // Getters and setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
