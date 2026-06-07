package com.example.library;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "name", "Library Management API",
                "status", "running",
                "endpoints", List.of(
                        "GET /api/books",
                        "POST /api/books",
                        "GET /api/members",
                        "POST /api/members",
                        "GET /api/loans",
                        "POST /api/loans/borrow",
                        "POST /api/loans/{id}/return"
                )
        );
    }
}
