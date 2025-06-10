package com.projeto.rotas.controller;

import com.projeto.rotas.dto.PontoDeColetaRequest;
import com.projeto.rotas.dto.PontoDeColetaResponse;
import com.projeto.rotas.model.NoGrafo;
import com.projeto.rotas.model.PontoDeColeta;
import com.projeto.rotas.repository.NoGrafoRepository;
import com.projeto.rotas.repository.PontoDeColetaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pontos-coleta")
public class PontoDeColetaController {

    private final PontoDeColetaRepository pontoDeColetaRepository;
    private final NoGrafoRepository noGrafoRepository;

    public PontoDeColetaController(PontoDeColetaRepository pontoDeColetaRepository, NoGrafoRepository noGrafoRepository) {
        this.pontoDeColetaRepository = pontoDeColetaRepository;
        this.noGrafoRepository = noGrafoRepository;
    }

    @PostMapping
    public ResponseEntity<PontoDeColetaResponse> createPontoDeColeta(@Valid @RequestBody PontoDeColetaRequest request) {
        NoGrafo noGrafo = noGrafoRepository.findById(request.getNoGrafoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "NoGrafo não encontrado com ID: " + request.getNoGrafoId()));

        PontoDeColeta pontoDeColeta = new PontoDeColeta(
                noGrafo,
                request.getNome(),
                request.getTipoLixo(),
                request.getPrioridade(),
                request.getStatus()
        );
        pontoDeColeta.setObservacoes(request.getObservacoes());
        pontoDeColeta = pontoDeColetaRepository.save(pontoDeColeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PontoDeColetaResponse(pontoDeColeta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoDeColetaResponse> getPontoDeColetaById(@PathVariable Long id) {
        PontoDeColeta pontoDeColeta = pontoDeColetaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto de coleta não encontrado com ID: " + id));
        return ResponseEntity.ok(new PontoDeColetaResponse(pontoDeColeta));
    }

    @GetMapping
    public ResponseEntity<List<PontoDeColetaResponse>> getAllPontosDeColeta() {
        List<PontoDeColetaResponse> responseList = pontoDeColetaRepository.findAll().stream()
                .map(PontoDeColetaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoDeColetaResponse> updatePontoDeColeta(@PathVariable Long id, @Valid @RequestBody PontoDeColetaRequest request) {
        PontoDeColeta pontoDeColeta = pontoDeColetaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto de coleta não encontrado com ID: " + id));

        NoGrafo noGrafo = noGrafoRepository.findById(request.getNoGrafoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "NoGrafo não encontrado com ID: " + request.getNoGrafoId()));

        pontoDeColeta.setNoGrafo(noGrafo);
        pontoDeColeta.setNome(request.getNome());
        pontoDeColeta.setTipoLixo(request.getTipoLixo());
        pontoDeColeta.setPrioridade(request.getPrioridade());
        pontoDeColeta.setStatus(request.getStatus());
        pontoDeColeta.setObservacoes(request.getObservacoes());
        pontoDeColeta = pontoDeColetaRepository.save(pontoDeColeta);
        return ResponseEntity.ok(new PontoDeColetaResponse(pontoDeColeta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePontoDeColeta(@PathVariable Long id) {
        if (!pontoDeColetaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto de coleta não encontrado com ID: " + id);
        }
        pontoDeColetaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
