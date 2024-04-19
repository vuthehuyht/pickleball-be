package com.macrace.pickleball.exceptions;

import com.macrace.pickleball.constant.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PhoneNumberNotFoundException extends RuntimeException {
    private Integer errorCode = ErrorCode.PHONE_NUMBER_NOT_FOUND;
    private String message = "Phone number not found";
}
