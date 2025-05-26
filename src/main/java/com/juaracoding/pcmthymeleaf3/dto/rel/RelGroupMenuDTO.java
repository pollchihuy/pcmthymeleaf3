package com.juaracoding.pcmthymeleaf3.dto.rel;

import jakarta.validation.constraints.NotNull;

public class RelGroupMenuDTO {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
