package com.projeto.rotas.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "trecho", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"no_origem_id", "no_destino_id"})
})
public class Trecho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no_origem_id", nullable = false)
    private NoGrafo noOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no_destino_id", nullable = false)
    private NoGrafo noDestino;

    @Column(name = "distancia_metros", nullable = false, precision = 10)
    private Double distanciaMetros;

    @Column(name = "tempo_estimado_segundos", nullable = false, precision = 10)
    private Double tempoEstimadoSegundos;

    @Column(name = "sentido_unico", nullable = false)
    private Boolean sentidoUnico;

    @Column(name = "velocidade_media_permitida", precision = 5)
    private Double velocidadeMediaPermitida;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Trecho() {

    }

    public Trecho(NoGrafo noOrigem, NoGrafo noDestino, Double distanciaMetros, Double tempoEstimadoSegundos, Boolean sentidoUnico) {
        this.noOrigem = noOrigem;
        this.noDestino = noDestino;
        this.distanciaMetros = distanciaMetros;
        this.tempoEstimadoSegundos = tempoEstimadoSegundos;
        this.sentidoUnico = sentidoUnico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoGrafo getNoOrigem() {
        return noOrigem;
    }

    public void setNoOrigem(NoGrafo noOrigem) {
        this.noOrigem = noOrigem;
    }

    public NoGrafo getNoDestino() {
        return noDestino;
    }

    public void setNoDestino(NoGrafo noDestino) {
        this.noDestino = noDestino;
    }

    public Double getDistanciaMetros() {
        return distanciaMetros;
    }

    public void setDistanciaMetros(Double distanciaMetros) {
        this.distanciaMetros = distanciaMetros;
    }

    public Double getTempoEstimadoSegundos() {
        return tempoEstimadoSegundos;
    }

    public void setTempoEstimadoSegundos(Double tempoEstimadoSegundos) {
        this.tempoEstimadoSegundos = tempoEstimadoSegundos;
    }

    public Boolean getSentidoUnico() {
        return sentidoUnico;
    }

    public void setSentidoUnico(Boolean sentidoUnico) {
        this.sentidoUnico = sentidoUnico;
    }

    public Double getVelocidadeMediaPermitida() {
        return velocidadeMediaPermitida;
    }

    public void setVelocidadeMediaPermitida(Double velocidadeMediaPermitida) {
        this.velocidadeMediaPermitida = velocidadeMediaPermitida;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trecho trecho = (Trecho) o;
        return Objects.equals(id, trecho.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trecho{" +
                "id=" + id +
                ", noOrigemId=" + (noOrigem != null ? noOrigem.getId() : "null") +
                ", noDestinoId=" + (noDestino != null ? noDestino.getId() : "null") +
                ", distanciaMetros=" + distanciaMetros +
                ", tempoEstimadoSegundos=" + tempoEstimadoSegundos +
                ", sentidoUnico=" + sentidoUnico +
                '}';
    }
}