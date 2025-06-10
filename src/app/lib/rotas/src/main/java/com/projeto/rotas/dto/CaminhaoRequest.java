package com.projeto.rotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CaminhaoRequest {
    @NotBlank
    private String placa;
    @NotNull @Positive
    private Double capacidadeKg;
    @NotNull @Positive
    private Double capacidadeVolumeM3;
    @NotNull
    private Double localizacaoAtualLatitude;
    @NotNull
    private Double localizacaoAtualLongitude;
    @NotBlank
    private String status;

    public CaminhaoRequest() {
    }

    public CaminhaoRequest(String placa, Double capacidadeKg, Double capacidadeVolumeM3, Double localizacaoAtualLatitude, Double localizacaoAtualLongitude, String status) {
        this.placa = placa;
        this.capacidadeKg = capacidadeKg;
        this.capacidadeVolumeM3 = capacidadeVolumeM3;
        this.localizacaoAtualLatitude = localizacaoAtualLatitude;
        this.localizacaoAtualLongitude = localizacaoAtualLongitude;
        this.status = status;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Double getCapacidadeKg() {
        return capacidadeKg;
    }

    public void setCapacidadeKg(Double capacidadeKg) {
        this.capacidadeKg = capacidadeKg;
    }

    public Double getCapacidadeVolumeM3() {
        return capacidadeVolumeM3;
    }

    public void setCapacidadeVolumeM3(Double capacidadeVolumeM3) {
        this.capacidadeVolumeM3 = capacidadeVolumeM3;
    }

    public Double getLocalizacaoAtualLatitude() {
        return localizacaoAtualLatitude;
    }

    public void setLocalizacaoAtualLatitude(Double localizacaoAtualLatitude) {
        this.localizacaoAtualLatitude = localizacaoAtualLatitude;
    }

    public Double getLocalizacaoAtualLongitude() {
        return localizacaoAtualLongitude;
    }

    public void setLocalizacaoAtualLongitude(Double localizacaoAtualLongitude) {
        this.localizacaoAtualLongitude = localizacaoAtualLongitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}