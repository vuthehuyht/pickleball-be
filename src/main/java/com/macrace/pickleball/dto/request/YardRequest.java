package com.macrace.pickleball.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class YardRequest {
    @NotBlank(message = "Yard name is required")
    private String name;

    @NotNull(message = "Facility id is required")
    @JsonProperty("facility_id")
    private Integer facilityId;
}
