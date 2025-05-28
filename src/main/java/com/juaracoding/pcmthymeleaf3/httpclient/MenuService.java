package com.juaracoding.pcmthymeleaf3.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "menu-services",url = "http://localhost:8085/menu")
public interface MenuService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);
}
