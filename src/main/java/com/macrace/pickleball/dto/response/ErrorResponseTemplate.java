package com.macrace.pickleball.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponseTemplate(
        @JsonProperty("error_code")
        Integer errorCode,
        String message
) {
}
