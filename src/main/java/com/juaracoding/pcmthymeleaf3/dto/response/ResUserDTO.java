package com.juaracoding.pcmthymeleaf3.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.juaracoding.pcmthymeleaf3.dto.rel.RelAksesDTO;

import java.time.LocalDate;

public class ResUserDTO {

    private Long id;

    private String username;

    private String email;

    @JsonProperty("no-hp")
    private String noHp;

    private String password;

    @JsonProperty("nama-lengkap")
    private String namaLengkap;

    private String alamat;

    private LocalDate tanggalLahir;

    private RelAksesDTO akses;

    public RelAksesDTO getAkses() {
        return akses;
    }

    public void setAkses(RelAksesDTO akses) {
        this.akses = akses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}