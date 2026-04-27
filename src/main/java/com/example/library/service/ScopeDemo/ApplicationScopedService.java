package com.example.library.service.ScopeDemo;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

// APPLICATION SCOPE - Single instance for entire application
@Service
@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApplicationScopedService implements Serializable {
    private static final long serialVersionUID = 1L;
    private int counter = 0;

    public int increment() {
        counter++;
        System.out.println("Application Scope - Counter: " + counter);
        return counter;
    }

    public int getCounter() {
        return counter;
    }
}