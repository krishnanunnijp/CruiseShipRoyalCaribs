package com.example.ShipProxy.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.springframework.stereotype.Component;

import com.example.sharedlib.proxy.model.ProxyRequest;
import com.example.sharedlib.proxy.model.ProxyResponse;



@Component
public class TcpClientConnectionManager {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final Object lock = new Object();

    private void connectIfNecessary() throws IOException {
        if (socket == null || socket.isClosed()) {
            synchronized (lock) {
                if (socket == null || socket.isClosed()) {
                    try {
                    	Socket socket = new Socket("shoreproxy", 9002);
                        out = new ObjectOutputStream(socket.getOutputStream());
                        in = new ObjectInputStream(socket.getInputStream());
                    } catch (IOException e) {
                    	throw new IOException("Unable to connect to shore proxy server at localhost:9002", e);
                    }
                }
            }
        }
    }


    
    public ProxyResponse sendRequest(ProxyRequest request) throws IOException, ClassNotFoundException {
        connectIfNecessary();
        synchronized (lock) {
            out.writeObject(request);
            out.flush();
            return (ProxyResponse) in.readObject();
        }
    }
}


