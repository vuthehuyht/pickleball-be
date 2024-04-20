package com.macrace.pickleball.service;

import com.macrace.pickleball.dto.request.FacilityRequest;
import com.macrace.pickleball.dto.response.AddFacilityResponse;
import com.macrace.pickleball.dto.response.FacilityResponse;

import java.util.List;

public interface FacilityService {
    AddFacilityResponse addFacility(FacilityRequest request);

    List<FacilityResponse> getAllFacility();

    FacilityResponse updateFacility(Integer id, FacilityRequest request);
}
