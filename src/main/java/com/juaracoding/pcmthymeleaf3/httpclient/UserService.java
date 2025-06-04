package com.juaracoding.pcmthymeleaf3.httpclient;

import com.juaracoding.pcmthymeleaf3.dto.validation.ValUserDTO;
import feign.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "user-services",url = "http://localhost:8085/user")
public interface UserService {

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

    @GetMapping("/download-excel")
    public Response downloadExcel(@RequestHeader("Authorization") String token,
                                  @RequestParam String column,
                                  @RequestParam String value);

    @GetMapping("/download-pdf")
    public Response downloadPdf(@RequestHeader("Authorization") String token,
                            @RequestParam String column,
                            @RequestParam String value);

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,@RequestBody ValUserDTO valUserDTO);

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id);

    @PutMapping("/{id}")
    public ResponseEntity<Object> update( @RequestHeader("Authorization") String token,
                                          @RequestBody ValUserDTO valUserDTO,
                                          @PathVariable Long id);

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
                                        @RequestHeader("Authorization") String token,
                                        @PathVariable Long id);

    @PostMapping(value="/files/upload/{username}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PostMapping("/files/upload/{username}")
            public ResponseEntity<Object> uploadImage(
            @RequestHeader("Authorization") String token,
            @PathVariable String username,
            @RequestPart("file") MultipartFile file);


}
