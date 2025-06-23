package com.example.ShoreProxy;



import java.net.ServerSocket;
import java.net.Socket;
import com.example.ShoreProxy.tcp.TcpRequestHandler;

public class ShoreProxyServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9002);
        System.out.println("ShoreProxy listening on port 9002...");

        while (true) {
        	Socket clientSocket = serverSocket.accept();
        	System.out.println("🔌 New TCP connection accepted from: " + clientSocket.getRemoteSocketAddress());
            new Thread(new TcpRequestHandler(clientSocket)).start();
        }
    }
}

