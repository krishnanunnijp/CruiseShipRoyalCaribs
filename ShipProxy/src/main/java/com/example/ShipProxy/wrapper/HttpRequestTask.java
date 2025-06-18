package com.example.ShipProxy.wrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;

import com.example.ShipProxy.tcp.TcpClientConnectionManager;
import com.example.sharedlib.proxy.model.ProxyRequest;
import com.example.sharedlib.proxy.model.ProxyResponse;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpRequestTask {

    private final AsyncContext asyncContext;
    private final TcpClientConnectionManager tcpManager;

    public HttpRequestTask(AsyncContext asyncContext, TcpClientConnectionManager tcpManager) {
        this.asyncContext = asyncContext;
        this.tcpManager = tcpManager;
    }

    public void process() {
        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

        try {
            ProxyRequest proxyRequest = buildFromHttpServletRequest(request);
            ProxyResponse proxyResp = tcpManager.sendRequest(proxyRequest);

            // Set status and headers
            response.setStatus(proxyResp.getStatus());
            if (proxyResp.getHeaders() != null) {
                proxyResp.getHeaders().forEach((k, vList) -> vList.forEach(v -> response.addHeader(k, v)));
            }

            // Write body
            if (proxyResp.getBody() != null) {
                response.getWriter().write(proxyResp.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(500, "Proxy Internal Error");
            } catch (IOException ignored) {}
        } finally {
            asyncContext.complete(); // Very important
        }
    }

    private ProxyRequest buildFromHttpServletRequest(HttpServletRequest request) throws IOException {
        ProxyRequest proxyReq = new ProxyRequest();
        proxyReq.setMethod(request.getMethod());


        StringBuffer fullUrl = request.getRequestURL();
        String query = request.getQueryString();
        if (query != null && !query.isEmpty()) {
            fullUrl.append('?').append(query);
        }
        proxyReq.setUrl(fullUrl.toString());


        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                headers.add(name, values.nextElement());
            }
        }
        proxyReq.setHeaders(headers);

        // Body
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        proxyReq.setBody(body);

        return proxyReq;
    }
}
