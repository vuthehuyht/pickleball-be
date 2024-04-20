package com.macrace.pickleball.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FacilityRequest {
    @NotBlank(message = "Facility name is required")
    private String name;
}
