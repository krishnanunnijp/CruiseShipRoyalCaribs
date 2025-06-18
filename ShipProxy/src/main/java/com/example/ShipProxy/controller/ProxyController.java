package com.example.ShipProxy.controller;

import com.example.ShipProxy.tcp.RequestQueueProcessor;
import com.example.ShipProxy.tcp.TcpClientConnectionManager;
import com.example.ShipProxy.wrapper.HttpRequestTask; // renamed to avoid confusion
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ProxyController {

    @Autowired
    private RequestQueueProcessor queueProcessor;

    @Autowired
    private TcpClientConnectionManager tcpClient;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public void proxy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var asyncContext = request.startAsync(); 
        HttpRequestTask task = new HttpRequestTask(asyncContext, tcpClient);
        queueProcessor.enqueue(task);
    }

}
