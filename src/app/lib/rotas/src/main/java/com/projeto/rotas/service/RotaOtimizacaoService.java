package com.projeto.rotas.service;

import com.projeto.rotas.model.*;
import com.projeto.rotas.repository.*;
import com.projeto.rotas.service.graph.Grafo;
import com.projeto.rotas.service.graph.NoGrafoRepresentacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RotaOtimizacaoService {

    //aqui foi obra do chat tbm

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
     * @param nosGrafo Lista de NoGrafo do banco de dados.
     * @param trechos Lista de Trecho do banco de dados.
     * @return Um objeto Grafo pronto para ser usado pelos algoritmos.
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
                grafo.adicionarAresta(origemRep, destinoRep, trecho.getTempoEstimadoSegundos()); // Usando tempo como peso
                if (!trecho.getSentidoUnico()) {
                    grafo.adicionarAresta(destinoRep, origemRep, trecho.getTempoEstimadoSegundos());
                }
            }
        }
        return grafo;
    }

    /**
     * Otimiza a rota para um caminhão, visitando pontos de coleta pendentes.
     * Esta é uma heurística simples (greedy approach).
     *
     * @param caminhaoId ID do caminhão.
     * @param tiposLixo Opcional: tipos de lixo a serem coletados.
     * @return A rota otimizada.
     */
    @Transactional
    public RotaOtimizada otimizarRota(Long caminhaoId, List<String> tiposLixo) {
        Caminhao caminhao = caminhaoRepository.findById(caminhaoId)
                .orElseThrow(() -> new RuntimeException("Caminhão não encontrado com ID: " + caminhaoId));

        // 1. Carregar todos os nós e trechos para construir o grafo completo
        List<NoGrafo> todosNosGrafo = noGrafoRepository.findAll();
        List<Trecho> todosTrechos = trechoRepository.findAll();
        Grafo grafo = construirGrafo(todosNosGrafo, todosTrechos);

        // Mapeia IDs de NoGrafo para suas representações em memória
        Map<Long, NoGrafoRepresentacao> noRepresentacaoMap = todosNosGrafo.stream()
                .collect(Collectors.toMap(NoGrafo::getId, no -> new NoGrafoRepresentacao(no.getId(), no.getLatitude(), no.getLongitude())));

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

        // 3. Iniciar a otimização da rota
        List<Long> sequenciaNosRota = new ArrayList<>();
        Double distanciaTotal = 0.0;
        Double tempoTotal = 0.0;

        // Ponto de partida inicial do caminhão
        NoGrafoRepresentacao localizacaoAtualCaminhao = new NoGrafoRepresentacao(
                -1L, // ID temporário para a localização inicial do caminhão
                caminhao.getLocalizacaoAtualLatitude(),
                caminhao.getLocalizacaoAtualLongitude()
        );
        // Adiciona a localização inicial do caminhão ao grafo se ela não for um nó existente
        // (Isso é uma simplificação. Em um sistema real, você encontraria o nó mais próximo ou interpolaria)
        if (!grafo.contemNo(localizacaoAtualCaminhao)) {
            grafo.adicionarNo(localizacaoAtualCaminhao);
        }
        sequenciaNosRota.add(localizacaoAtualCaminhao.getId()); // Adiciona o ponto de partida

        Set<PontoDeColeta> pontosVisitados = new HashSet<>();
        Double capacidadeAtualKg = caminhao.getCapacidadeKg(); // Assumindo que o caminhão começa vazio
        Double capacidadeAtualM3 = caminhao.getCapacidadeVolumeM3();

        while (!pontosPendentes.isEmpty() && capacidadeAtualKg > 0 && capacidadeAtualM3 > 0) {
            PontoDeColeta proximoPonto = null;
            Double menorTempo = Double.POSITIVE_INFINITY;
            List<NoGrafoRepresentacao> melhorCaminho = null;

            // Para cada ponto pendente, calcule o caminho mais curto a partir da localização atual do caminhão
            for (PontoDeColeta ponto : pontosPendentes) {
                NoGrafoRepresentacao destinoNo = noRepresentacaoMap.get(ponto.getNoGrafo().getId());
                if (destinoNo == null) continue; // Garante que o nó existe no grafo

                Map.Entry<Map<NoGrafoRepresentacao, Double>, Map<NoGrafoRepresentacao, NoGrafoRepresentacao>> dijkstraResult =
                        dijkstraService.calcularCaminhosMaisCurtos(grafo, localizacaoAtualCaminhao);

                Double tempoParaPonto = dijkstraResult.getKey().get(destinoNo);
                if (tempoParaPonto != null && tempoParaPonto < menorTempo) {
                    // Simulação de capacidade: assuma que cada ponto de coleta adiciona um peso fixo
                    // Em um sistema real, você teria o peso/volume de lixo estimado para cada ponto
                    Double pesoEstimadoLixo = 100.0; // Exemplo: 100 kg por ponto
                    Double volumeEstimadoLixo = 0.5; // Exemplo: 0.5 m3 por ponto

                    if (capacidadeAtualKg >= pesoEstimadoLixo && capacidadeAtualM3 >= volumeEstimadoLixo) {
                        menorTempo = tempoParaPonto;
                        proximoPonto = ponto;
                        melhorCaminho = dijkstraService.reconstruirCaminho(dijkstraResult.getValue(), localizacaoAtualCaminhao, destinoNo);
                    }
                }
            }

            if (proximoPonto != null && melhorCaminho != null && !melhorCaminho.isEmpty()) {
                // Adiciona o caminho à rota
                // O primeiro nó do melhorCaminho é a localização atual, então pulamos ele se já estiver na sequência
                for (int i = 0; i < melhorCaminho.size(); i++) {
                    if (i == 0 && sequenciaNosRota.contains(melhorCaminho.get(i).getId())) {
                        continue; // Evita duplicar o nó de partida se já foi adicionado
                    }
                    sequenciaNosRota.add(melhorCaminho.get(i).getId());
                }

                distanciaTotal += menorTempo; // Usando tempo como "distância" para o peso do grafo
                tempoTotal += menorTempo;

                // Atualiza a localização atual do caminhão para o ponto recém-coletado
                localizacaoAtualCaminhao = noRepresentacaoMap.get(proximoPonto.getNoGrafo().getId());

                // Atualiza a capacidade do caminhão (simulação)
                capacidadeAtualKg -= 100.0;
                capacidadeAtualM3 -= 0.5;

                // Remove o ponto da lista de pendentes e marca como coletado
                pontosPendentes.remove(proximoPonto);
                proximoPonto.setStatus("COLETADO");
                pontoDeColetaRepository.save(proximoPonto);
                pontosVisitados.add(proximoPonto); // Adiciona aos pontos realmente visitados
            } else {
                // Não foi possível encontrar um próximo ponto ou o caminhão está cheio
                break;
            }
        }

        // 4. Salvar a rota otimizada
        RotaOtimizada rota = new RotaOtimizada(
                // Distância total (na verdade, tempo total acumulado)
        );
        return rotaOtimizadaRepository.save(rota);
    }
}