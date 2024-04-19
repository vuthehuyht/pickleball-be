package com.macrace.pickleball.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.macrace.pickleball.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Phone number is required")
    @PhoneNumber
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
