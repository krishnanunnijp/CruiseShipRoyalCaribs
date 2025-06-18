package com.example.sharedlib.proxy.model;

import java.io.Serializable;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyRequest implements Serializable {
	
	private static final long serialVersionUID = 2048906288081011821L;
    private String method;
    private String url;
    private HttpHeaders headers = new HttpHeaders(); 
    private String body;
    
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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

