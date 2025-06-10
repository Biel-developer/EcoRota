package com.projeto.rotas.service.graph;

import java.util.*;

/**
 * Representação do grafo em memória usando lista de adjacências.
 */
public class Grafo {
    private Map<NoGrafoRepresentacao, List<Aresta>> adjacencias;

    public Grafo() {
        this.adjacencias = new HashMap<>();
    }

    public void adicionarNo(NoGrafoRepresentacao no) {
        adjacencias.putIfAbsent(no, new ArrayList<>());
    }

    /**
     * Adiciona uma aresta direcionada ao grafo.
     * @param origem O nó de origem.
     * @param destino O nó de destino.
     * @param peso O peso da aresta (ex: distância, tempo).
     */
    public void adicionarAresta(NoGrafoRepresentacao origem, NoGrafoRepresentacao destino, Double peso) {
        // Garante que os nós existam no grafo
        adicionarNo(origem);
        adicionarNo(destino);
        adjacencias.get(origem).add(new Aresta(destino, peso));
    }

    /**
     * Adiciona uma aresta bidirecional (não-direcionada) ao grafo.
     * @param no1 Um dos nós.
     * @param no2 O outro nó.
     * @param peso O peso da aresta.
     */
    public void adicionarArestaBidirecional(NoGrafoRepresentacao no1, NoGrafoRepresentacao no2, Double peso) {
        adicionarAresta(no1, no2, peso);
        adicionarAresta(no2, no1, peso);
    }

    public List<Aresta> getAdjacencias(NoGrafoRepresentacao no) {
        return adjacencias.getOrDefault(no, Collections.emptyList());
    }

    public Set<NoGrafoRepresentacao> getNos() {
        return adjacencias.keySet();
    }

    public NoGrafoRepresentacao getNoById(Long id) {
        return adjacencias.keySet().stream()
                .filter(no -> no.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean contemNo(NoGrafoRepresentacao no) {
        return adjacencias.containsKey(no);
    }

    public int getNumeroDeNos() {
        return adjacencias.size();
    }

    public int getNumeroDeArestas() {
        return adjacencias.values().stream().mapToInt(List::size).sum();
    }
}