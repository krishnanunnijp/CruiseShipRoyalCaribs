package com.example.ShoreProxy.tcp;

import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TcpServer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("Shore proxy listening...");
        Socket shipSocket = serverSocket.accept(); // Single persistent connection
        new Thread(new TcpRequestHandler(shipSocket)).start();
    }
}

