
package com.projeto.rotas.service;

import com.projeto.rotas.model.*;
import com.projeto.rotas.repository.*;
import com.projeto.rotas.service.graph.Grafo;
import com.projeto.rotas.service.graph.NoGrafoRepresentacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RotaOtimizacaoService {

    private final NoGrafoRepository noGrafoRepository;
    private final TrechoRepository trechoRepository;
    private final PontoDeColetaRepository pontoDeColetaRepository;
    private final CaminhaoRepository caminhaoRepository;
    private final RotaOtimizadaRepository rotaOtimizadaRepository;
    private final DijkstraService dijkstraService;

    public RotaOtimizacaoService(NoGrafoRepository noGrafoRepository,
                                 TrechoRepository trechoRepository,
                                 PontoDeColetaRepository pontoDeColetaRepository,
                                 CaminhaoRepository caminhaoRepository,
                                 RotaOtimizadaRepository rotaOtimizadaRepository,
                                 DijkstraService dijkstraService) {
        this.noGrafoRepository = noGrafoRepository;
        this.trechoRepository = trechoRepository;
        this.pontoDeColetaRepository = pontoDeColetaRepository;
        this.caminhaoRepository = caminhaoRepository;
        this.rotaOtimizadaRepository = rotaOtimizadaRepository;
        this.dijkstraService = dijkstraService;
    }

    /**
     * Constrói o grafo em memória a partir dos dados do banco de dados.
     */
    private Grafo construirGrafo(List<NoGrafo> nosGrafo, List<Trecho> trechos) {
        Grafo grafo = new Grafo();
        Map<Long, NoGrafoRepresentacao> noRepresentacaoMap = new HashMap<>();

        // Adiciona todos os nós ao grafo
        for (NoGrafo no : nosGrafo) {
            NoGrafoRepresentacao rep = new NoGrafoRepresentacao(no.getId(), no.getLatitude(), no.getLongitude());
            grafo.adicionarNo(rep);
            noRepresentacaoMap.put(no.getId(), rep);
        }

        // Adiciona todas as arestas (trechos) ao grafo
        for (Trecho trecho : trechos) {
            NoGrafoRepresentacao origemRep = noRepresentacaoMap.get(trecho.getNoOrigem().getId());
            NoGrafoRepresentacao destinoRep = noRepresentacaoMap.get(trecho.getNoDestino().getId());

            if (origemRep != null && destinoRep != null) {
                grafo.adicionarAresta(origemRep, destinoRep, trecho.getTempoEstimadoSegundos());
                if (!trecho.getSentidoUnico()) {
                    grafo.adicionarAresta(destinoRep, origemRep, trecho.getTempoEstimadoSegundos());
                }
            }
        }
        return grafo;
    }

    /**
     * Encontra o nó mais próximo no grafo para uma coordenada dada
     */
    private NoGrafoRepresentacao encontrarNoMaisProximo(List<NoGrafo> nos, double latitude, double longitude) {
        NoGrafo noMaisProximo = null;
        double menorDistancia = Double.MAX_VALUE;

        for (NoGrafo no : nos) {
            double distancia = calcularDistanciaHaversine(latitude, longitude, no.getLatitude(), no.getLongitude());
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                noMaisProximo = no;
            }
        }

        return noMaisProximo != null ? 
            new NoGrafoRepresentacao(noMaisProximo.getId(), noMaisProximo.getLatitude(), noMaisProximo.getLongitude()) : 
            null;
    }

    /**
     * Calcula a distância entre duas coordenadas usando a fórmula de Haversine
     */
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raio da Terra em km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // retorna em metros
    }

    /**
     * Otimiza a rota para um caminhão, visitando pontos de coleta pendentes.
     */
    @Transactional
    public RotaOtimizada otimizarRota(Long caminhaoId, List<String> tiposLixo) {
        Caminhao caminhao = caminhaoRepository.findById(caminhaoId)
                .orElseThrow(() -> new RuntimeException("Caminhão não encontrado com ID: " + caminhaoId));

        // 1. Carregar todos os nós e trechos para construir o grafo completo
        List<NoGrafo> todosNosGrafo = noGrafoRepository.findAll();
        List<Trecho> todosTrechos = trechoRepository.findAll();
        Grafo grafo = construirGrafo(todosNosGrafo, todosTrechos);

        // 2. Obter pontos de coleta pendentes, ordenados por prioridade
        List<PontoDeColeta> pontosPendentes;
        if (tiposLixo != null && !tiposLixo.isEmpty()) {
            pontosPendentes = pontoDeColetaRepository.findAll().stream()
                    .filter(p -> p.getStatus().equals("PENDENTE") && tiposLixo.contains(p.getTipoLixo()))
                    .sorted(Comparator.comparing(PontoDeColeta::getPrioridade))
                    .collect(Collectors.toList());
        } else {
            pontosPendentes = pontoDeColetaRepository.findByStatus("PENDENTE").stream()
                    .sorted(Comparator.comparing(PontoDeColeta::getPrioridade))
                    .collect(Collectors.toList());
        }

        if (pontosPendentes.isEmpty()) {
            throw new RuntimeException("Não há pontos de coleta pendentes para otimizar.");
        }

        // 3. Encontrar o nó de depósito (ou usar coordenadas do caminhão)
        NoGrafoRepresentacao noInicial = encontrarNoMaisProximo(todosNosGrafo, 
            caminhao.getLocalizacaoAtualLatitude(), 
            caminhao.getLocalizacaoAtualLongitude());

        if (noInicial == null) {
            throw new RuntimeException("Não foi possível encontrar um nó inicial válido para o caminhão.");
        }

        // 4. Construir a rota otimizada usando algoritmo guloso
        List<Long> sequenciaNosRota = new ArrayList<>();
        Double distanciaTotal = 0.0;
        Double tempoTotal = 0.0;

        NoGrafoRepresentacao localizacaoAtual = noInicial;
        sequenciaNosRota.add(localizacaoAtual.getId());

        Set<PontoDeColeta> pontosVisitados = new HashSet<>();
        List<PontoDeColeta> pontosParaVisitar = new ArrayList<>(pontosPendentes);

        // Algoritmo guloso: sempre ir para o ponto mais próximo
        while (!pontosParaVisitar.isEmpty()) {
            PontoDeColeta proximoPonto = null;
            Double menorTempo = Double.POSITIVE_INFINITY;
            List<NoGrafoRepresentacao> melhorCaminho = null;

            // Encontrar o ponto mais próximo
            for (PontoDeColeta ponto : pontosParaVisitar) {
                NoGrafoRepresentacao noPonto = grafo.getNoById(ponto.getNoGrafo().getId());
                if (noPonto == null) continue;

                // Calcular caminho mais curto usando Dijkstra
                Map.Entry<Map<NoGrafoRepresentacao, Double>, Map<NoGrafoRepresentacao, NoGrafoRepresentacao>> resultado =
                        dijkstraService.calcularCaminhosMaisCurtos(grafo, localizacaoAtual);

                Double tempoParaPonto = resultado.getKey().get(noPonto);
                if (tempoParaPonto != null && tempoParaPonto < menorTempo) {
                    menorTempo = tempoParaPonto;
                    proximoPonto = ponto;
                    melhorCaminho = dijkstraService.reconstruirCaminho(resultado.getValue(), localizacaoAtual, noPonto);
                }
            }

            if (proximoPonto != null && melhorCaminho != null && !melhorCaminho.isEmpty()) {
                // Adicionar o caminho à rota (excluindo o primeiro nó se já estiver na sequência)
                for (int i = 1; i < melhorCaminho.size(); i++) {
                    sequenciaNosRota.add(melhorCaminho.get(i).getId());
                }

                tempoTotal += menorTempo;
                distanciaTotal += menorTempo * 10; // Aproximação: assumindo velocidade média

                // Atualizar localização atual
                localizacaoAtual = grafo.getNoById(proximoPonto.getNoGrafo().getId());

                // Marcar ponto como visitado
                pontosVisitados.add(proximoPonto);
                pontosParaVisitar.remove(proximoPonto);

                // Marcar ponto como coletado no banco
                proximoPonto.setStatus("COLETADO");
                proximoPonto.setDataUltimaColeta(OffsetDateTime.now());
                pontoDeColetaRepository.save(proximoPonto);
            } else {
                // Não foi possível encontrar caminho para nenhum ponto
                break;
            }
        }

        // 5. Retornar ao depósito
        if (!localizacaoAtual.equals(noInicial)) {
            Map.Entry<Map<NoGrafoRepresentacao, Double>, Map<NoGrafoRepresentacao, NoGrafoRepresentacao>> resultado =
                    dijkstraService.calcularCaminhosMaisCurtos(grafo, localizacaoAtual);
            
            Double tempoRetorno = resultado.getKey().get(noInicial);
            if (tempoRetorno != null) {
                List<NoGrafoRepresentacao> caminhoRetorno = dijkstraService.reconstruirCaminho(
                    resultado.getValue(), localizacaoAtual, noInicial);
                
                // Adicionar caminho de retorno (excluindo o primeiro nó)
                for (int i = 1; i < caminhoRetorno.size(); i++) {
                    sequenciaNosRota.add(caminhoRetorno.get(i).getId());
                }
                
                tempoTotal += tempoRetorno;
                distanciaTotal += tempoRetorno * 10;
            }
        }

        // 6. Criar e salvar a rota otimizada
        RotaOtimizada rota = new RotaOtimizada();
        rota.setCaminhao(caminhao);
        rota.setDataGeracao(OffsetDateTime.now());
        rota.setDistanciaTotalMetros(distanciaTotal);
        rota.setTempoTotalEstimadoSegundos(tempoTotal);
        rota.setSequenciaNos(sequenciaNosRota);
        rota.setStatus("ATIVA");
        rota.setCreatedAt(OffsetDateTime.now());
        rota.setUpdatedAt(OffsetDateTime.now());

        return rotaOtimizadaRepository.save(rota);
    }
}

