package com.projeto.rotas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OtimizarRotaRequest {

   @NotNull(message = "O ID do caminhão é obrigatório.")
    private Long caminhaoId;

    @NotEmpty(message = "A lista de tipos de lixo não pode estar vazia.")
    private List<String> tiposLixo;

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
