package com.macrace.pickleball.exceptions;

import com.macrace.pickleball.constant.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class YardNameExistException extends RuntimeException {
    private Integer errorCode = ErrorCode.YARD_NAME_EXIST;
    private String message = "Yard name exist";
}
