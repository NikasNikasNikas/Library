package com.example.library.service.ScopeDemo;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

// SESSION SCOPE - Same instance for entire user session
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedService implements Serializable {
    private static final long serialVersionUID = 1L;
    private int counter = 0;

    public int increment() {
        counter++;
        System.out.println("Session Scope - Counter: " + counter + " (Session: " + this.hashCode() + ")");
        return counter;
    }

    public int getCounter() {
        return counter;
    }
}
