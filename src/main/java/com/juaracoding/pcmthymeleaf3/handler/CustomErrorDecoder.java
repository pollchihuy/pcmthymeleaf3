package com.juaracoding.pcmthymeleaf3.handler;


import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int intResponseStatus = response.status();
        return new ErrorExceptionHandling(intResponseStatus,String.valueOf(intResponseStatus));
    }
}
