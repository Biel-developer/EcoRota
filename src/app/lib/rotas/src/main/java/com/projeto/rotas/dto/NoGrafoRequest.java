package com.projeto.rotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NoGrafoRequest {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotBlank
    private String tipo; // Ex: 'INTERSECAO', 'PONTO_COLETA', 'DEPOSITO'

    public NoGrafoRequest() {
    }

    public NoGrafoRequest(Double latitude, Double longitude, String tipo) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipo = tipo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}