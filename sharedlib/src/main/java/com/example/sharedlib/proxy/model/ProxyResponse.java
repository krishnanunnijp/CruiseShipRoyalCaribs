package com.example.sharedlib.proxy.model;

import java.io.Serializable;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyResponse implements Serializable {
	
	private static final long serialVersionUID = 2048906288081011822L;
    private int status;
    private HttpHeaders headers = new HttpHeaders(); 
    private String body;
    
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers != null ? headers : new HttpHeaders();
    }
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
    
    
}

