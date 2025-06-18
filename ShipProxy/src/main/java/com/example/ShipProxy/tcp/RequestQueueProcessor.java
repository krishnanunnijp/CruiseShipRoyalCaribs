package com.example.ShipProxy.tcp;

import com.example.ShipProxy.wrapper.HttpRequestTask;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class RequestQueueProcessor {

    private final BlockingQueue<HttpRequestTask> requestQueue = new LinkedBlockingQueue<>();

    @PostConstruct
    public void startProcessor() {
        Thread processorThread = new Thread(() -> {
            while (true) {
                try {
                    HttpRequestTask req = requestQueue.take(); 
                    req.process(); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        processorThread.setDaemon(true);
        processorThread.start();
    }

    public void enqueue(HttpRequestTask request) {
        requestQueue.add(request);
    }
}
