package com.projeto.rotas.dto;

import com.projeto.rotas.model.RotaOtimizada;

import java.time.OffsetDateTime;
import java.util.List;

public class RotaResponse {
    private Long id;
    private Long caminhaoId;
    private String caminhaoPlaca;
    private OffsetDateTime dataGeracao;
    private Double distanciaTotalMetros;
    private Double tempoTotalEstimadoSegundos;
    private List<Long> sequenciaNos; // IDs dos nós na sequência da rota
    private String status;

    public RotaResponse(RotaOtimizada rotaOtimizada) {
        this.id = rotaOtimizada.getId();
        this.caminhaoId = rotaOtimizada.getCaminhao().getId();
        this.caminhaoPlaca = rotaOtimizada.getCaminhao().getPlaca();
        this.dataGeracao = rotaOtimizada.getDataGeracao();
        this.distanciaTotalMetros = rotaOtimizada.getDistanciaTotalMetros();
        this.tempoTotalEstimadoSegundos = rotaOtimizada.getTempoTotalEstimadoSegundos();
        this.sequenciaNos = rotaOtimizada.getSequenciaNos();
        this.status = rotaOtimizada.getStatus();
    }

    public Long getId() {
        return id;
    }

    public Long getCaminhaoId() {
        return caminhaoId;
    }

    public String getCaminhaoPlaca() {
        return caminhaoPlaca;
    }

    public OffsetDateTime getDataGeracao() {
        return dataGeracao;
    }

    public Double getDistanciaTotalMetros() {
        return distanciaTotalMetros;
    }

    public Double getTempoTotalEstimadoSegundos() {
        return tempoTotalEstimadoSegundos;
    }

    public List<Long> getSequenciaNos() {
        return sequenciaNos;
    }

    public String getStatus() {
        return status;
    }
}