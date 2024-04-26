package com.macrace.pickleball.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FacilityResponse(
        Integer id,
        String name,
        String address,
        @JsonProperty("phone_number")
        String phoneNumber
) {
}
