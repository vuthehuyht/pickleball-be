package com.macrace.pickleball.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.macrace.pickleball.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Phone number is required")
    @PhoneNumber
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Full name is required")
    @JsonProperty("full_name")
    private String fullName;
}
