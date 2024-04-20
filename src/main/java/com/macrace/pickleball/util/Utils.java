package com.macrace.pickleball.util;

import com.macrace.pickleball.constant.ErrorCode;
import com.macrace.pickleball.exceptions.GeneralException;
import org.springframework.http.HttpStatus;

public class Utils {
    public static GeneralException handleUnauthorized() {
        GeneralException customMessageException = new GeneralException();
        customMessageException.setMessage("");
        customMessageException.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        return customMessageException;
    }

    public static GeneralException handleTokenUnavailable() {
        GeneralException customMessageException = new GeneralException();
        customMessageException.setMessage("Token is unavailable");
        customMessageException.setCode(String.valueOf(ErrorCode.TOKEN_UNAVAILABLE));
        return customMessageException;
    }
}
