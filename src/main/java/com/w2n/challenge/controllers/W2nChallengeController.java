package com.w2n.challenge.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class W2nChallengeController {

    private final String applicationName;

    public W2nChallengeController(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    @GetMapping
    public ResponseEntity<String> getApplicationName() {
        return ResponseEntity.ok(this.applicationName);
    }

}
