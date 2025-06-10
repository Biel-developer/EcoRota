package com.projeto.rotas.service.graph;

/**
 * Representa uma aresta (conexão) entre dois nós no grafo em memória.
 */
public class Aresta {
    private NoGrafoRepresentacao destino;
    private Double peso; // pode ser distância

    public Aresta(NoGrafoRepresentacao destino, Double peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public NoGrafoRepresentacao getDestino() {
        return destino;
    }

    public Double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "Aresta{" +
                "destino=" + destino.getId() +
                ", peso=" + peso +
                '}';
    }
}
