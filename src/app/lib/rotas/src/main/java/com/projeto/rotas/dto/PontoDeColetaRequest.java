package com.projeto.rotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class PontoDeColetaRequest {
    @NotNull
    private Long noGrafoId; // ID do NoGrafo associado
    @NotBlank
    private String nome;
    @NotBlank
    private String tipoLixo;
    @NotNull @Min(1)
    private Integer prioridade;
    @NotBlank
    private String status;
    private String observacoes;

    public PontoDeColetaRequest() {
    }

    public PontoDeColetaRequest(Long noGrafoId, String nome, String tipoLixo, Integer prioridade, String status, String observacoes) {
        this.noGrafoId = noGrafoId;
        this.nome = nome;
        this.tipoLixo = tipoLixo;
        this.prioridade = prioridade;
        this.status = status;
        this.observacoes = observacoes;
    }

    public Long getNoGrafoId() {
        return noGrafoId;
    }

    public void setNoGrafoId(Long noGrafoId) {
        this.noGrafoId = noGrafoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoLixo() {
        return tipoLixo;
    }

    public void setTipoLixo(String tipoLixo) {
        this.tipoLixo = tipoLixo;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
