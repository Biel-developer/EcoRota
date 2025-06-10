package com.projeto.rotas.dto;

import com.projeto.rotas.model.PontoDeColeta;

import java.time.OffsetDateTime;

public class PontoDeColetaResponse {
    private Long id;
    private Long noGrafoId;
    private Double latitude; // Adicionado para conveniência
    private Double longitude; // Adicionado para conveniência
    private String nome;
    private String tipoLixo;
    private Integer prioridade;
    private String status;
    private OffsetDateTime dataUltimaColeta;
    private String observacoes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public PontoDeColetaResponse(PontoDeColeta pontoDeColeta) {
        this.id = pontoDeColeta.getId();
        this.noGrafoId = pontoDeColeta.getNoGrafo().getId();
        this.latitude = pontoDeColeta.getNoGrafo().getLatitude();
        this.longitude = pontoDeColeta.getNoGrafo().getLongitude();
        this.nome = pontoDeColeta.getNome();
        this.tipoLixo = pontoDeColeta.getTipoLixo();
        this.prioridade = pontoDeColeta.getPrioridade();
        this.status = pontoDeColeta.getStatus();
        this.dataUltimaColeta = pontoDeColeta.getDataUltimaColeta();
        this.observacoes = pontoDeColeta.getObservacoes();
        this.createdAt = pontoDeColeta.getCreatedAt();
        this.updatedAt = pontoDeColeta.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getNoGrafoId() {
        return noGrafoId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getNome() {
        return nome;
    }

    public String getTipoLixo() {
        return tipoLixo;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getDataUltimaColeta() {
        return dataUltimaColeta;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}