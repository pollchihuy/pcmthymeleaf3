package com.juaracoding.pcmthymeleaf3.httpclient;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "group-menu-services",url = "http://localhost:8085/group-menu")
public interface GroupMenuService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);
}
