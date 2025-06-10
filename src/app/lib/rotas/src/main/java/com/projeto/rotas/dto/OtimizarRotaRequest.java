package com.projeto.rotas.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OtimizarRotaRequest {
    @NotNull
    private Long caminhaoId;
    private List<String> tiposLixo; // Opcional: filtrar por tipo de lixo

    public OtimizarRotaRequest() {
    }

    public OtimizarRotaRequest(Long caminhaoId, List<String> tiposLixo) {
        this.caminhaoId = caminhaoId;
        this.tiposLixo = tiposLixo;
    }

    public Long getCaminhaoId() {
        return caminhaoId;
    }

    public void setCaminhaoId(Long caminhaoId) {
        this.caminhaoId = caminhaoId;
    }

    public List<String> getTiposLixo() {
        return tiposLixo;
    }

    public void setTiposLixo(List<String> tiposLixo) {
        this.tiposLixo = tiposLixo;
    }
}
