package com.juaracoding.pcmthymeleaf3.dto.rel;

import jakarta.validation.constraints.NotNull;

public class RelGroupMenuDTO {

    @NotNull
    private Long id;

    private String nama;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
