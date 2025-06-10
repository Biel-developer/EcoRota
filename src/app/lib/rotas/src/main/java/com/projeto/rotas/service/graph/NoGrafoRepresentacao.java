package com.projeto.rotas.service.graph;

import java.util.Objects;

/**
 * Representa um nó (vértice) no grafo em memória para o algoritmo de Dijkstra.
 * Contém apenas as informações essenciais para o cálculo da rota.
 */
public class NoGrafoRepresentacao {
    private Long id; // Corresponde ao ID do NoGrafo do banco de dados
    private Double latitude;
    private Double longitude;

    public NoGrafoRepresentacao(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
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

    // equals e hashCode tem q ter para que o HashMap e HashSet funcionem corretamente saporra
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoGrafoRepresentacao that = (NoGrafoRepresentacao) o;
        return Objects.equals(id, that.id); // Comparação baseada no ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash baseado no ID
    }

    @Override
    public String toString() {
        return "NoGrafoRepresentacao{" +
                "id=" + id +
                ", lat=" + latitude +
                ", lon=" + longitude +
                '}';
    }
}