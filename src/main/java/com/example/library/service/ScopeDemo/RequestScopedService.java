package com.example.library.service.ScopeDemo;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

// REQUEST SCOPE - New instance for each HTTP request
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedService implements Serializable {
    private static final long serialVersionUID = 1L;
    private int counter = 0;

    public int increment() {
        counter++;
        System.out.println("Request Scope - Counter: " + counter + " (Thread: " + Thread.currentThread().getId() + ")");
        return counter;
    }

    public int getCounter() {
        return counter;
    }
}