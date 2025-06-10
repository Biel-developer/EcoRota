package com.projeto.rotas.service.graph;

/**
 * Classe auxiliar para a PriorityQueue no algoritmo de Dijkstra.
 * Representa um nó e a distância acumulada até ele a partir da origem.
 */
public class EstadoNo implements Comparable<EstadoNo> {
    public NoGrafoRepresentacao no;
    public Double distancia;

    public EstadoNo(NoGrafoRepresentacao no, Double distancia) {
        this.no = no;
        this.distancia = distancia;
    }

    @Override
    public int compareTo(EstadoNo outro) {
        return Double.compare(this.distancia, outro.distancia);
    }
}