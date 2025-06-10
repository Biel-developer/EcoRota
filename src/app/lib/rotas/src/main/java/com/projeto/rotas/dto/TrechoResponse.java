package com.projeto.rotas.dto;

import com.projeto.rotas.model.Trecho;

import java.time.OffsetDateTime;

public class TrechoResponse {
    private Long id;
    private Long noOrigemId;
    private Long noDestinoId;
    private Double distanciaMetros;
    private Double tempoEstimadoSegundos;
    private Boolean sentidoUnico;
    private Double velocidadeMediaPermitida;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public TrechoResponse(Trecho trecho) {
        this.id = trecho.getId();
        this.noOrigemId = trecho.getNoOrigem().getId();
        this.noDestinoId = trecho.getNoDestino().getId();
        this.distanciaMetros = trecho.getDistanciaMetros();
        this.tempoEstimadoSegundos = trecho.getTempoEstimadoSegundos();
        this.sentidoUnico = trecho.getSentidoUnico();
        this.velocidadeMediaPermitida = trecho.getVelocidadeMediaPermitida();
        this.createdAt = trecho.getCreatedAt();
        this.updatedAt = trecho.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getNoOrigemId() {
        return noOrigemId;
    }

    public Long getNoDestinoId() {
        return noDestinoId;
    }

    public Double getDistanciaMetros() {
        return distanciaMetros;
    }

    public Double getTempoEstimadoSegundos() {
        return tempoEstimadoSegundos;
    }

    public Boolean getSentidoUnico() {
        return sentidoUnico;
    }

    public Double getVelocidadeMediaPermitida() {
        return velocidadeMediaPermitida;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}