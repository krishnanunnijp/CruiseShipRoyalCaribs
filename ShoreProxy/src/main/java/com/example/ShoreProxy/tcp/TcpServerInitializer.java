package com.example.ShoreProxy.tcp;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class TcpServerInitializer {

    @PostConstruct
    public void startTcpServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(9001)) {
                System.out.println("TCP Server started on port 9001");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new TcpRequestHandler(clientSocket)).start(); 
                }
            } catch (IOException e) {
                System.err.println("Failed to start TCP server: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}

