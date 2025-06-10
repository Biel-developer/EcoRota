package com.projeto.rotas.service;

import com.projeto.rotas.service.graph.Aresta;
import com.projeto.rotas.service.graph.EstadoNo;
import com.projeto.rotas.service.graph.Grafo;
import com.projeto.rotas.service.graph.NoGrafoRepresentacao;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DijkstraService {

    //aqui o chat fez tudo n faço ideia de como funciona tmj
    /**
     * Calcula o caminho mais curto de um nó de origem para todos os outros nós no grafo
     * usando o algoritmo de Dijkstra.
     *
     * @param grafo O grafo a ser percorrido.
     * @param origem O nó de origem.
     * @return Um mapa de distâncias (nó -> distância) e um mapa de predecessores (nó -> nó anterior no caminho mais curto).
     *         Retorna um Map.Entry onde a chave é o mapa de distâncias e o valor é o mapa de predecessores.
     */
    public Map.Entry<Map<NoGrafoRepresentacao, Double>, Map<NoGrafoRepresentacao, NoGrafoRepresentacao>> calcularCaminhosMaisCurtos(Grafo grafo, NoGrafoRepresentacao origem) {
        Map<NoGrafoRepresentacao, Double> distancias = new HashMap<>();
        Map<NoGrafoRepresentacao, NoGrafoRepresentacao> predecessores = new HashMap<>();
        PriorityQueue<EstadoNo> filaPrioridade = new PriorityQueue<>();

        // Inicializa todas as distâncias como infinito, exceto a origem
        for (NoGrafoRepresentacao no : grafo.getNos()) {
            distancias.put(no, Double.POSITIVE_INFINITY);
        }
        distancias.put(origem, 0.0);

        filaPrioridade.add(new EstadoNo(origem, 0.0));

        while (!filaPrioridade.isEmpty()) {
            EstadoNo estadoAtual = filaPrioridade.poll();
            NoGrafoRepresentacao noAtual = estadoAtual.no;
            Double distanciaAtual = estadoAtual.distancia;

            // Se já encontramos um caminho mais curto para este nó, ignoramos
            if (distanciaAtual > distancias.get(noAtual)) {
                continue;
            }

            for (Aresta aresta : grafo.getAdjacencias(noAtual)) {
                NoGrafoRepresentacao vizinho = aresta.getDestino();
                Double pesoAresta = aresta.getPeso();
                Double novaDistancia = distanciaAtual + pesoAresta;

                if (novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, noAtual);
                    filaPrioridade.add(new EstadoNo(vizinho, novaDistancia));
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(distancias, predecessores);
    }

    /**
     * Reconstrói o caminho mais curto de um nó de origem para um nó de destino.
     *
     * @param predecessores O mapa de predecessores retornado por calcularCaminhosMaisCurtos.
     * @param destino O nó de destino.
     * @return Uma lista de nós representando o caminho, ou uma lista vazia se não houver caminho.
     */
    public List<NoGrafoRepresentacao> reconstruirCaminho(Map<NoGrafoRepresentacao, NoGrafoRepresentacao> predecessores, NoGrafoRepresentacao origem, NoGrafoRepresentacao destino) {
        List<NoGrafoRepresentacao> caminho = new LinkedList<>();
        NoGrafoRepresentacao passo = destino;

        // Verifica se o destino é alcançável a partir da origem
        if (!predecessores.containsKey(destino) && !origem.equals(destino)) {
            return Collections.emptyList(); // Destino não alcançável
        }

        while (passo != null && !passo.equals(origem)) {
            caminho.add(0, passo); // Adiciona no início para manter a ordem correta
            passo = predecessores.get(passo);
        }
        if (passo != null && passo.equals(origem)) { // Adiciona o nó de origem
            caminho.add(0, passo);
        }
        return caminho;
    }
}