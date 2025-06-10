package com.projeto.rotas.dto;

import com.projeto.rotas.model.NoGrafo;

import java.time.OffsetDateTime;

public class NoGrafoResponse {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String tipo;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public NoGrafoResponse(NoGrafo noGrafo) {
        this.id = noGrafo.getId();
        this.latitude = noGrafo.getLatitude();
        this.longitude = noGrafo.getLongitude();
        this.tipo = noGrafo.getTipo();
        this.createdAt = noGrafo.getCreatedAt();
        this.updatedAt = noGrafo.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
