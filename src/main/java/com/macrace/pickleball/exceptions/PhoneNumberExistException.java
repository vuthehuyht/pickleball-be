package com.macrace.pickleball.exceptions;

import com.macrace.pickleball.constant.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PhoneNumberExistException extends RuntimeException {
    private Integer errorCode = ErrorCode.PHONE_NUMBER_EXIST;
    private String message = "Phone number exists";
}
