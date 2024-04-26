package com.macrace.pickleball.exceptions;

import com.macrace.pickleball.constant.ErrorCode;

public class YardNotFoundException extends RuntimeException {
    private Integer errorCode = ErrorCode.YARD_NOT_FOUND;
    private String message = "Yard not found";
}
