package com.macrace.pickleball.service.impl;

import com.macrace.pickleball.constant.YardState;
import com.macrace.pickleball.dto.request.YardRequest;
import com.macrace.pickleball.dto.response.MessageResponseTemplate;
import com.macrace.pickleball.dto.response.YardResponse;
import com.macrace.pickleball.exceptions.FacilityNotFoundException;
import com.macrace.pickleball.exceptions.YardNameExistException;
import com.macrace.pickleball.exceptions.YardNotFoundException;
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

        Optional<Yard> yardOptional = yardRepository.findByName(request.getName());
        if (yardOptional.isPresent()) {
            throw new YardNameExistException();
        }

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
    public MessageResponseTemplate updateYard(Integer id, YardRequest request) {
        Yard yard = Optional.ofNullable(yardRepository.findById(id).orElseThrow(YardNotFoundException::new)).get();

        yard.setName(request.getName());
        yardRepository.save(yard);
        log.info("Update yard with id = {}", id);
        return new MessageResponseTemplate("Updating yard done");
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
