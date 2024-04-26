package com.macrace.pickleball.service;

import com.macrace.pickleball.dto.request.FacilityRequest;
import com.macrace.pickleball.dto.response.AddFacilityResponse;
import com.macrace.pickleball.dto.response.FacilityResponse;
import com.macrace.pickleball.dto.response.MessageResponseTemplate;

import java.util.List;

public interface FacilityService {
    MessageResponseTemplate addFacility(FacilityRequest request);

    List<FacilityResponse> getAllFacility();

    MessageResponseTemplate updateFacility(Integer id, FacilityRequest request);
}
