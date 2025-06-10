package com.projeto.rotas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TrechoRequest {
    @NotNull
    private Long noOrigemId;
    @NotNull
    private Long noDestinoId;
    @NotNull @Positive
    private Double distanciaMetros;
    @NotNull @Positive
    private Double tempoEstimadoSegundos;
    @NotNull
    private Boolean sentidoUnico;
    private Double velocidadeMediaPermitida;

    public TrechoRequest() {
    }

    public TrechoRequest(Long noOrigemId, Long noDestinoId, Double distanciaMetros, Double tempoEstimadoSegundos, Boolean sentidoUnico, Double velocidadeMediaPermitida) {
        this.noOrigemId = noOrigemId;
        this.noDestinoId = noDestinoId;
        this.distanciaMetros = distanciaMetros;
        this.tempoEstimadoSegundos = tempoEstimadoSegundos;
        this.sentidoUnico = sentidoUnico;
        this.velocidadeMediaPermitida = velocidadeMediaPermitida;
    }

    public Long getNoOrigemId() {
        return noOrigemId;
    }

    public void setNoOrigemId(Long noOrigemId) {
        this.noOrigemId = noOrigemId;
    }

    public Long getNoDestinoId() {
        return noDestinoId;
    }

    public void setNoDestinoId(Long noDestinoId) {
        this.noDestinoId = noDestinoId;
    }

    public Double getDistanciaMetros() {
        return distanciaMetros;
    }

    public void setDistanciaMetros(Double distanciaMetros) {
        this.distanciaMetros = distanciaMetros;
    }

    public Double getTempoEstimadoSegundos() {
        return tempoEstimadoSegundos;
    }

    public void setTempoEstimadoSegundos(Double tempoEstimadoSegundos) {
        this.tempoEstimadoSegundos = tempoEstimadoSegundos;
    }

    public Boolean getSentidoUnico() {
        return sentidoUnico;
    }

    public void setSentidoUnico(Boolean sentidoUnico) {
        this.sentidoUnico = sentidoUnico;
    }

    public Double getVelocidadeMediaPermitida() {
        return velocidadeMediaPermitida;
    }

    public void setVelocidadeMediaPermitida(Double velocidadeMediaPermitida) {
        this.velocidadeMediaPermitida = velocidadeMediaPermitida;
    }
}