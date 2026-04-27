package com.example.library.controller;

import com.example.library.service.ScopeDemo.ApplicationScopedService;
import com.example.library.service.ScopeDemo.RequestScopedService;
import com.example.library.service.ScopeDemo.SessionScopedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scope-demo")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class ScopeDemoController {

    private final RequestScopedService requestScopedService;
    private final SessionScopedService sessionScopedService;
    private final ApplicationScopedService applicationScopedService;

    @PostMapping("/request/increment")
    public Map<String, Object> incrementRequest() {
        int newValue = requestScopedService.increment();
        Map<String, Object> response = new HashMap<>();
        response.put("counter", newValue);
        return response;
    }

    @GetMapping("/request/value")
    public Map<String, Object> getRequestValue() {
        Map<String, Object> response = new HashMap<>();
        response.put("counter", requestScopedService.getCounter());
        return response;
    }

    @PostMapping("/session/increment")
    public Map<String, Object> incrementSession() {
        int newValue = sessionScopedService.increment();
        Map<String, Object> response = new HashMap<>();
        response.put("counter", newValue);
        return response;
    }

    @GetMapping("/session/value")
    public Map<String, Object> getSessionValue() {
        Map<String, Object> response = new HashMap<>();
        response.put("counter", sessionScopedService.getCounter());
        return response;
    }

    @PostMapping("/application/increment")
    public Map<String, Object> incrementApplication() {
        int newValue = applicationScopedService.increment();
        Map<String, Object> response = new HashMap<>();
        response.put("counter", newValue);
        return response;
    }

    @GetMapping("/application/value")
    public Map<String, Object> getApplicationValue() {
        Map<String, Object> response = new HashMap<>();
        response.put("counter", applicationScopedService.getCounter());
        return response;
    }
}