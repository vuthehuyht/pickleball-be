package com.macrace.pickleball.service.impl;

import com.macrace.pickleball.dto.request.FacilityRequest;
import com.macrace.pickleball.dto.response.AddFacilityResponse;
import com.macrace.pickleball.dto.response.FacilityResponse;
import com.macrace.pickleball.dto.response.MessageResponseTemplate;
import com.macrace.pickleball.exceptions.FacilityNotFoundException;
import com.macrace.pickleball.exceptions.PhoneNumberNotFoundException;
import com.macrace.pickleball.model.Facility;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.FacilityRepository;
import com.macrace.pickleball.repository.UserRepository;
import com.macrace.pickleball.service.FacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponseTemplate addFacility(FacilityRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByPhoneNumber(authentication.getName());
        if (userOptional.isEmpty()) {
            throw new PhoneNumberNotFoundException();
        }

        Facility newFacility = Facility.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .user(userOptional.get())
                .build();
        facilityRepository.save(newFacility);
        log.info("Creating new facility done");
        return new MessageResponseTemplate("New facility added successfully");
    }

    @Override
    public List<FacilityResponse> getAllFacility() {
        List<FacilityResponse> response = new ArrayList<>();
        List<Facility> facilities = facilityRepository.findAll();
        if (facilities.isEmpty()) {
            return response;
        }

        facilities.forEach(facility -> response.add(
                new FacilityResponse(
                        facility.getId(),
                        facility.getName(),
                        facility.getAddress(),
                        facility.getPhoneNumber()
                )));
        log.info("Getting all facilities done");
        return response;
    }

    @Override
    public MessageResponseTemplate updateFacility(Integer id, FacilityRequest request) {
        Facility facility = Optional.ofNullable(facilityRepository.findById(id)
                .orElseThrow(FacilityNotFoundException::new)).get();

        facility.setName(request.getName());
        facilityRepository.save(facility);
        log.info("Updating facility with id = {}", id);
        return new MessageResponseTemplate("Update facility done");
    }
}
