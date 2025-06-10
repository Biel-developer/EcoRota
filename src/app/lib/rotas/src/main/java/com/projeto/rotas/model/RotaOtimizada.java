package com.projeto.rotas.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rota_otimizada")
public class RotaOtimizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminhao_id", nullable = false)
    private Caminhao caminhao;

    @CreationTimestamp
    @Column(name = "data_geracao", nullable = false, updatable = false)
    private OffsetDateTime dataGeracao;

    @Column(name = "distancia_total_metros", nullable = false, precision = 12)
    private Double distanciaTotalMetros;

    @Column(name = "tempo_total_estimado_segundos", nullable = false, precision = 12)
    private Double tempoTotalEstimadoSegundos;

    // Armazena a sequência de IDs dos nós (no_grafo_id) na rota como JSONB
    // @JdbcTypeCode(SqlTypes.JSON) é uma forma de dizer ao Hibernate para mapear para JSONB
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sequencia_nos", nullable = false)
    private List<Long> sequenciaNos; // Lista de IDs de NoGrafo

    @Column(nullable = false, length = 50)
    private String status; // Ex: 'ATIVA', 'CONCLUIDA', 'CANCELADA'

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public RotaOtimizada(RotaOtimizada rotaOtimizada) { // ESTE CONSTRUTOR É CRÍTICO
        this.id = rotaOtimizada.getId();
        this.dataGeracao = rotaOtimizada.getDataGeracao();
        this.distanciaTotalMetros = rotaOtimizada.getDistanciaTotalMetros();
        this.tempoTotalEstimadoSegundos = rotaOtimizada.getTempoTotalEstimadoSegundos();
        this.sequenciaNos = rotaOtimizada.getSequenciaNos();
        this.status = rotaOtimizada.getStatus();
    }

    public RotaOtimizada(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Caminhao getCaminhao() {
        return caminhao;
    }

    public void setCaminhao(Caminhao caminhao) {
        this.caminhao = caminhao;
    }

    public OffsetDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(OffsetDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public Double getDistanciaTotalMetros() {
        return distanciaTotalMetros;
    }

    public void setDistanciaTotalMetros(Double distanciaTotalMetros) {
        this.distanciaTotalMetros = distanciaTotalMetros;
    }

    public Double getTempoTotalEstimadoSegundos() {
        return tempoTotalEstimadoSegundos;
    }

    public void setTempoTotalEstimadoSegundos(Double tempoTotalEstimadoSegundos) {
        this.tempoTotalEstimadoSegundos = tempoTotalEstimadoSegundos;
    }

    public List<Long> getSequenciaNos() {
        return sequenciaNos;
    }

    public void setSequenciaNos(List<Long> sequenciaNos) {
        this.sequenciaNos = sequenciaNos;
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
        RotaOtimizada that = (RotaOtimizada) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RotaOtimizada{" +
                "id=" + id +
                ", caminhaoId=" + (caminhao != null ? caminhao.getId() : "null") +
                ", distanciaTotalMetros=" + distanciaTotalMetros +
                ", tempoTotalEstimadoSegundos=" + tempoTotalEstimadoSegundos +
                ", status='" + status + '\'' +
                '}';
    }
}