package com.juaracoding.pcmthymeleaf3.httpclient;

import com.juaracoding.pcmthymeleaf3.config.FeignClientConfig;
import com.juaracoding.pcmthymeleaf3.dto.validation.ValGroupMenuDTO;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name = "group-menu-services",url = "http://localhost:8080/group-menu")
@FeignClient(name = "group-menu-services",url = "${host.rest.api}"+"group-menu",configuration = FeignClientConfig.class)
public interface GroupMenuService {

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("Authorization") String token);

    @GetMapping("/{sort}/{sort-by}/{page}")
    public ResponseEntity<Object> findByParam(
            @RequestHeader("Authorization") String token,
            @PathVariable String sort,
            @PathVariable(value = "sort-by") String sortBy,
            @PathVariable Integer page,
            @RequestParam Integer size,
            @RequestParam String column,
            @RequestParam String value);

    @GetMapping("/{sort}/{sort-by}/{page}/{q}")
    public ResponseEntity<Object> findByParam(
            @RequestHeader("Authorization") String token,
            @PathVariable String sort,
            @PathVariable(value = "sort-by") String sortBy,
            @PathVariable Integer page,
            @PathVariable Integer q,
            @RequestParam Integer size,
            @RequestParam String column,
            @RequestParam String value);

    @GetMapping("/download-excel")
    public Response downloadExcel(@RequestHeader("Authorization") String token,
                                  @RequestParam String column,
                                  @RequestParam String value);

    @GetMapping("/download-pdf")
    public Response downloadPdf(@RequestHeader("Authorization") String token,
                            @RequestParam String column,
                            @RequestParam String value);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,@RequestBody ValGroupMenuDTO valGroupMenuDTO);

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id);

    @PutMapping("/{id}")
    public ResponseEntity<Object> update( @RequestHeader("Authorization") String token,
                                          @RequestBody ValGroupMenuDTO valGroupMenuDTO,
                                          @PathVariable Long id);

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
                                        @RequestHeader("Authorization") String token,
                                        @PathVariable Long id);

}
