package com.macrace.pickleball.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FacilityRequest {
    @NotBlank(message = "Facility name is required")
    private String name;

    @NotBlank(message = "Facility address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @JsonProperty("phone_number")
    private String phoneNumber;
}
