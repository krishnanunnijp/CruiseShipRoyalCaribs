package com.example.ShoreProxy.tcp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.sharedlib.proxy.model.ProxyRequest;
import com.example.sharedlib.proxy.model.ProxyResponse;




public class TcpRequestHandler implements Runnable {
    private final Socket clientSocket;
    private final RestTemplate restTemplate = new RestTemplate();

    public TcpRequestHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                ProxyRequest request = (ProxyRequest) in.readObject();
                ProxyResponse response = handleRequest(request);
                out.writeObject(response);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private ProxyResponse handleRequest(ProxyRequest request) {
        try {

            
            HttpEntity<String> entity = new HttpEntity<>(request.getBody(), request.getHeaders());

            
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    request.getUrl(),                                 
                    HttpMethod.valueOf(request.getMethod()),          
                    entity,
                    String.class
            );

            
            ProxyResponse response = new ProxyResponse();
            response.setStatus(responseEntity.getStatusCodeValue());
            response.setBody(responseEntity.getBody());

            response.setHeaders(responseEntity.getHeaders());


            return response;
        } catch (Exception e) {
            // Handle HTTP errors or connection errors
            ProxyResponse errorResponse = new ProxyResponse();
            errorResponse.setStatus(500);
            errorResponse.setBody("Error while forwarding request: " + e.getMessage());
            return errorResponse;
        }
    }

}

