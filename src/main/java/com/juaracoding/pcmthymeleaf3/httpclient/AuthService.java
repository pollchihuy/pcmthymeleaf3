package com.juaracoding.pcmthymeleaf3.httpclient;


import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.RegisDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.VerifyRegisDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-services",url = "http://localhost:8085/auth")
public interface AuthService {

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO);

    @PostMapping("/regis")
    public ResponseEntity<Object> registration(@RequestBody RegisDTO regisDTO);

    @PostMapping("/verify-regis")
    public ResponseEntity<Object> verifyRegis(@RequestBody VerifyRegisDTO verifyRegisDTO);
}