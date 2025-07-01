package com.juaracoding.pcmthymeleaf3.config;

import com.juaracoding.pcmthymeleaf3.handler.CustomErrorDecoder;
import feign.Feign;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .errorDecoder(new CustomErrorDecoder());
    }
}
