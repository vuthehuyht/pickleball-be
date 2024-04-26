package com.macrace.pickleball.controller;

import com.macrace.pickleball.dto.request.FacilityRequest;
import com.macrace.pickleball.service.FacilityService;
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
public class FacilityController {
    private final FacilityService facilityService;

    @PostMapping(path = "/facility")
    public ResponseEntity<Object> addFacility(@Valid @RequestBody FacilityRequest request) {
        log.info("Add new facility request");
        return new ResponseEntity<>(facilityService.addFacility(request), HttpStatus.CREATED);
    }

    @GetMapping(path = "/facilities")
    public ResponseEntity<Object> getAllFacility() {
        log.info("Get all facility request");
        return new ResponseEntity<>(facilityService.getAllFacility(), HttpStatus.OK);
    }

    @PutMapping(path = "/facility/{facilityId}")
    public ResponseEntity<Object> updateFacility(@PathVariable("facilityId") Integer facilityId, FacilityRequest request) {
        log.info("Updating facility request");
        return new ResponseEntity<>(facilityService.updateFacility(facilityId, request), HttpStatus.OK);
    }
}
