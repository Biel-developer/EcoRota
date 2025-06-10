package com.projeto.rotas.dto;

import com.projeto.rotas.model.Caminhao;

import java.time.OffsetDateTime;

public class CaminhaoResponse {
    private Long id;
    private String placa;
    private Double capacidadeKg;
    private Double capacidadeVolumeM3;
    private Double localizacaoAtualLatitude;
    private Double localizacaoAtualLongitude;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CaminhaoResponse(Caminhao caminhao) {
        this.id = caminhao.getId();
        this.placa = caminhao.getPlaca();
        this.capacidadeKg = caminhao.getCapacidadeKg();
        this.capacidadeVolumeM3 = caminhao.getCapacidadeVolumeM3();
        this.localizacaoAtualLatitude = caminhao.getLocalizacaoAtualLatitude();
        this.localizacaoAtualLongitude = caminhao.getLocalizacaoAtualLongitude();
        this.status = caminhao.getStatus();
        this.createdAt = caminhao.getCreatedAt();
        this.updatedAt = caminhao.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public Double getCapacidadeKg() {
        return capacidadeKg;
    }

    public Double getCapacidadeVolumeM3() {
        return capacidadeVolumeM3;
    }

    public Double getLocalizacaoAtualLatitude() {
        return localizacaoAtualLatitude;
    }

    public Double getLocalizacaoAtualLongitude() {
        return localizacaoAtualLongitude;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
