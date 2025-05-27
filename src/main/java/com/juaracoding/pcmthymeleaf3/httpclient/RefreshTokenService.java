package com.juaracoding.pcmthymeleaf3.httpclient;


import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "refresh-auth-services",url = "http://localhost:8085/auth")
public interface RefreshTokenService {

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> tokenExpired(@RequestBody LoginDTO loginDTO);
}
