package com.macrace.pickleball.service;

import com.macrace.pickleball.dto.request.YardRequest;
import com.macrace.pickleball.dto.response.MessageResponseTemplate;
import com.macrace.pickleball.dto.response.YardResponse;

import java.util.List;

public interface YardService {
    MessageResponseTemplate addNewYard(YardRequest request);

    MessageResponseTemplate updateYard(YardRequest request);

    List<YardResponse> getAllYard();
}
