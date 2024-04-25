package com.macrace.pickleball.controller;

import com.macrace.pickleball.dto.request.YardRequest;
import com.macrace.pickleball.service.YardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
@Validated
@Slf4j
public class YardController {
    private final YardService yardService;

    @PostMapping(path = "/yard")
    public ResponseEntity<Object> addNewYard(@Valid @RequestBody YardRequest request) {
        log.info("Add new yard request");
        return new ResponseEntity<>(yardService.addNewYard(request), HttpStatus.CREATED);
    }

    @GetMapping(path = "/yards")
    public ResponseEntity<Object> getAllYard() {
        log.info("Get all yards request");
        return new ResponseEntity<>(yardService.getAllYard(), HttpStatus.OK);
    }
}
