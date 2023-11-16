package com.lokytech.authservice.exception;

import feign.FeignException;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String s, FeignException e){
        super();
    }

    public ExternalServiceException(String message){
        super(message);
    }
}
