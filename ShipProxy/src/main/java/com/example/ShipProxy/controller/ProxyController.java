package com.example.ShipProxy.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.example.ShipProxy.tcp.TcpClientConnectionManager;
import com.example.sharedlib.proxy.model.ProxyRequest;
import com.example.sharedlib.proxy.model.ProxyResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ProxyController {
    @Autowired
    TcpClientConnectionManager tcpClient;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> proxy(HttpServletRequest request) throws Exception {
        ProxyRequest proxyRequest = buildFromHttpServletRequest(request);
        ProxyResponse response = tcpClient.sendRequest(proxyRequest);
        return ResponseEntity
                .status(response.getStatus())
                .headers(response.getHeaders())
                .body(response.getBody());
    }
    
    private ProxyRequest buildFromHttpServletRequest(HttpServletRequest request) throws IOException {
        ProxyRequest proxyRequest = new ProxyRequest();

        proxyRequest.setMethod(request.getMethod());
        proxyRequest.setUrl(request.getRequestURL().toString());

        // Extract headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        proxyRequest.setHeaders(headers);

        // Extract body
        String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        proxyRequest.setBody(body);

        return proxyRequest;
    }

}

