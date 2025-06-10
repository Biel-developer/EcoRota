package com.projeto.rotas.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "ponto_de_coleta")
public class PontoDeColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Carregamento preguiçoso para evitar loops infinitos em JSON
    @JoinColumn(name = "no_grafo_id", nullable = false)
    private NoGrafo noGrafo; // O nó geográfico associado a este ponto de coleta

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(name = "tipo_lixo", nullable = false, length = 50)
    private String tipoLixo; // Ex: 'ORGANICO', 'RECICLAVEL', 'ESPECIAL'

    @Column(nullable = false)
    private Integer prioridade; // 1 (mais alta) a N (mais baixa)

    @Column(nullable = false, length = 50)
    private String status; // Ex: 'PENDENTE', 'COLETADO', 'IGNORADO'

    @Column(name = "data_ultima_coleta")
    private OffsetDateTime dataUltimaColeta;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public PontoDeColeta() {
    }

    public PontoDeColeta(NoGrafo noGrafo, String nome, String tipoLixo, Integer prioridade, String status) {
        this.noGrafo = noGrafo;
        this.nome = nome;
        this.tipoLixo = tipoLixo;
        this.prioridade = prioridade;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoGrafo getNoGrafo() {
        return noGrafo;
    }

    public void setNoGrafo(NoGrafo noGrafo) {
        this.noGrafo = noGrafo;
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

    public OffsetDateTime getDataUltimaColeta() {
        return dataUltimaColeta;
    }

    public void setDataUltimaColeta(OffsetDateTime dataUltimaColeta) {
        this.dataUltimaColeta = dataUltimaColeta;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
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
        PontoDeColeta that = (PontoDeColeta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PontoDeColeta{" +
                "id=" + id +
                ", noGrafoId=" + (noGrafo != null ? noGrafo.getId() : "null") +
                ", nome='" + nome + '\'' +
                ", tipoLixo='" + tipoLixo + '\'' +
                ", prioridade=" + prioridade +
                ", status='" + status + '\'' +
                '}';
    }
}