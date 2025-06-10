package com.projeto.rotas.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "caminhao")
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String placa;

    @Column(name = "capacidade_kg", nullable = false, precision = 10)
    private Double capacidadeKg;

    @Column(name = "capacidade_volume_m3", nullable = false, precision = 10)
    private Double capacidadeVolumeM3;

    @Column(name = "localizacao_atual_latitude", precision = 10)
    private Double localizacaoAtualLatitude;

    @Column(name = "localizacao_atual_longitude", precision = 10)
    private Double localizacaoAtualLongitude;

    @Column(nullable = false, length = 50)
    private String status; // Ex: 'DISPONIVEL', 'EM_ROTA', 'MANUTENCAO'

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Caminhao() {
    }

    public Caminhao(String placa, Double capacidadeKg, Double capacidadeVolumeM3, String status) {
        this.placa = placa;
        this.capacidadeKg = capacidadeKg;
        this.capacidadeVolumeM3 = capacidadeVolumeM3;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Double getCapacidadeKg() {
        return capacidadeKg;
    }

    public void setCapacidadeKg(Double capacidadeKg) {
        this.capacidadeKg = capacidadeKg;
    }

    public Double getCapacidadeVolumeM3() {
        return capacidadeVolumeM3;
    }

    public void setCapacidadeVolumeM3(Double capacidadeVolumeM3) {
        this.capacidadeVolumeM3 = capacidadeVolumeM3;
    }

    public Double getLocalizacaoAtualLatitude() {
        return localizacaoAtualLatitude;
    }

    public void setLocalizacaoAtualLatitude(Double localizacaoAtualLatitude) {
        this.localizacaoAtualLatitude = localizacaoAtualLatitude;
    }

    public Double getLocalizacaoAtualLongitude() {
        return localizacaoAtualLongitude;
    }

    public void setLocalizacaoAtualLongitude(Double localizacaoAtualLongitude) {
        this.localizacaoAtualLongitude = localizacaoAtualLongitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caminhao caminhao = (Caminhao) o;
        return Objects.equals(id, caminhao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Caminhao{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", capacidadeKg=" + capacidadeKg +
                ", capacidadeVolumeM3=" + capacidadeVolumeM3 +
                ", status='" + status + '\'' +
                '}';
    }
}
