package com.macrace.pickleball.exceptions;

import com.macrace.pickleball.constant.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FacilityNotFoundException extends RuntimeException {
    private Integer errorCode = ErrorCode.FACILITY_NOT_FOUND;
    private String message = "Facility not found";
}
