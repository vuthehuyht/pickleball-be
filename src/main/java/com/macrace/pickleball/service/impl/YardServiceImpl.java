package com.macrace.pickleball.service.impl;

import com.macrace.pickleball.constant.YardState;
import com.macrace.pickleball.dto.request.YardRequest;
import com.macrace.pickleball.dto.response.MessageResponseTemplate;
import com.macrace.pickleball.dto.response.YardResponse;
import com.macrace.pickleball.exceptions.FacilityNotFoundException;
import com.macrace.pickleball.exceptions.YardNameExistException;
import com.macrace.pickleball.model.Facility;
import com.macrace.pickleball.model.Yard;
import com.macrace.pickleball.repository.FacilityRepository;
import com.macrace.pickleball.repository.YardRepository;
import com.macrace.pickleball.service.YardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class YardServiceImpl implements YardService {
    private final FacilityRepository facilityRepository;
    private final YardRepository yardRepository;

    @Override
    public MessageResponseTemplate addNewYard(YardRequest request) {
        Facility facility = Optional.ofNullable(facilityRepository.findById(request.getFacilityId())
                .orElseThrow(FacilityNotFoundException::new)).get();

        Optional.ofNullable(yardRepository.findByName(request.getName())
                .orElseThrow(YardNameExistException::new)).get();

        Yard newYard = Yard.builder()
                .name(request.getName())
                .state(YardState.READY.toString())
                .facility(facility)
                .build();
        yardRepository.save(newYard);
        log.info("New yard was added in facility id = {}", facility.getId());
        return new MessageResponseTemplate("New yard was added successfully");
    }

    @Override
    public MessageResponseTemplate updateYard(YardRequest request) {
        return null;
    }

    @Override
    public List<YardResponse> getAllYard() {
        List<Yard> yards = yardRepository.findAll();
        List<YardResponse> response = new ArrayList<>();

        if (yards.isEmpty()) {
            return Collections.emptyList();
        }

        yards.forEach(yard -> {
            response.add(new YardResponse(yard.getId(), yard.getName(), yard.getState()));
        });
        return response;
    }
}
