package com.example.ShipProxy.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.sharedlib.proxy.model.ProxyRequest;
import com.example.sharedlib.proxy.model.ProxyResponse;

import jakarta.annotation.PostConstruct;



@Component
public class TcpClientConnectionManager {

    @Value("${shore.proxy.host}")
    private String shoreHost;

    @Value("${shore.proxy.port}")
    private int shorePort;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final BlockingQueue<ProxyJob> queue = new LinkedBlockingQueue<>();

    @PostConstruct
    public void init() {
        try {
            this.socket = new Socket(this.shoreHost, this.shorePort);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to shore proxy server at " + shoreHost + ":" + shorePort, e);
        }

        Thread worker = new Thread(() -> {
            while (true) {
                ProxyJob job = null;
                try {
                    job = queue.take();

                    out.writeObject(job.request());
                    out.flush();

                    ProxyResponse response = (ProxyResponse) in.readObject();
                    job.future().complete(response);

                } catch (Exception e) {
                    if (job != null) {
                        job.future().completeExceptionally(e);
                    }
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }

    public CompletableFuture<ProxyResponse> sendRequest(ProxyRequest request) {
        ProxyJob job = new ProxyJob(request);
        queue.offer(job);
        return job.future();
    }

    private record ProxyJob(ProxyRequest request, CompletableFuture<ProxyResponse> future) {
        ProxyJob(ProxyRequest request) {
            this(request, new CompletableFuture<>());
        }
    }
}




