package com.projeto.rotas.controller;

import com.projeto.rotas.dto.TrechoRequest;
import com.projeto.rotas.dto.TrechoResponse;
import com.projeto.rotas.model.NoGrafo;
import com.projeto.rotas.model.Trecho;
import com.projeto.rotas.repository.NoGrafoRepository;
import com.projeto.rotas.repository.TrechoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trechos")
public class TrechoController {

    private final TrechoRepository trechoRepository;
    private final NoGrafoRepository noGrafoRepository;

    public TrechoController(TrechoRepository trechoRepository, NoGrafoRepository noGrafoRepository) {
        this.trechoRepository = trechoRepository;
        this.noGrafoRepository = noGrafoRepository;
    }

    @PostMapping
    public ResponseEntity<TrechoResponse> createTrecho(@Valid @RequestBody TrechoRequest request) {
        NoGrafo noOrigem = noGrafoRepository.findById(request.getNoOrigemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nó de origem não encontrado com ID: " + request.getNoOrigemId()));
        NoGrafo noDestino = noGrafoRepository.findById(request.getNoDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nó de destino não encontrado com ID: " + request.getNoDestinoId()));

        // Verifica se já existe um trecho com a mesma origem e destino
        if (trechoRepository.findByNoOrigemAndNoDestino(noOrigem, noDestino).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um trecho de " + noOrigem.getId() + " para " + noDestino.getId());
        }

        Trecho trecho = new Trecho(
                noOrigem,
                noDestino,
                request.getDistanciaMetros(),
                request.getTempoEstimadoSegundos(),
                request.getSentidoUnico()
        );
        trecho.setVelocidadeMediaPermitida(request.getVelocidadeMediaPermitida());
        trecho = trechoRepository.save(trecho);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TrechoResponse(trecho));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrechoResponse> getTrechoById(@PathVariable Long id) {
        Trecho trecho = trechoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trecho não encontrado com ID: " + id));
        return ResponseEntity.ok(new TrechoResponse(trecho));
    }

    @GetMapping
    public ResponseEntity<List<TrechoResponse>> getAllTrechos() {
        List<TrechoResponse> responseList = trechoRepository.findAll().stream()
                .map(TrechoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrechoResponse> updateTrecho(@PathVariable Long id, @Valid @RequestBody TrechoRequest request) {
        Trecho trecho = trechoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trecho não encontrado com ID: " + id));

        NoGrafo noOrigem = noGrafoRepository.findById(request.getNoOrigemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nó de origem não encontrado com ID: " + request.getNoOrigemId()));
        NoGrafo noDestino = noGrafoRepository.findById(request.getNoDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nó de destino não encontrado com ID: " + request.getNoDestinoId()));

        // Verifica se a atualização criaria um trecho duplicado
        if (!trecho.getNoOrigem().equals(noOrigem) || !trecho.getNoDestino().equals(noDestino)) {
            if (trechoRepository.findByNoOrigemAndNoDestino(noOrigem, noDestino).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um trecho de " + noOrigem.getId() + " para " + noDestino.getId());
            }
        }

        trecho.setNoOrigem(noOrigem);
        trecho.setNoDestino(noDestino);
        trecho.setDistanciaMetros(request.getDistanciaMetros());
        trecho.setTempoEstimadoSegundos(request.getTempoEstimadoSegundos());
        trecho.setSentidoUnico(request.getSentidoUnico());
        trecho.setVelocidadeMediaPermitida(request.getVelocidadeMediaPermitida());
        trecho = trechoRepository.save(trecho);
        return ResponseEntity.ok(new TrechoResponse(trecho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrecho(@PathVariable Long id) {
        if (!trechoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trecho não encontrado com ID: " + id);
        }
        trechoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
