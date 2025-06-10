package com.projeto.rotas.controller;

import com.projeto.rotas.dto.NoGrafoRequest;
import com.projeto.rotas.dto.NoGrafoResponse;
import com.projeto.rotas.model.NoGrafo;
import com.projeto.rotas.repository.NoGrafoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/nos-grafo")
public class NoGrafoController {

    private final NoGrafoRepository noGrafoRepository;

    public NoGrafoController(NoGrafoRepository noGrafoRepository) {
        this.noGrafoRepository = noGrafoRepository;
    }

    @PostMapping
    public ResponseEntity<NoGrafoResponse> createNoGrafo(@Valid @RequestBody NoGrafoRequest request) {
        NoGrafo noGrafo = new NoGrafo(request.getLatitude(), request.getLongitude(), request.getTipo());
        noGrafo = noGrafoRepository.save(noGrafo);
        return ResponseEntity.status(HttpStatus.CREATED).body(new NoGrafoResponse(noGrafo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoGrafoResponse> getNoGrafoById(@PathVariable Long id) {
        NoGrafo noGrafo = noGrafoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nó de grafo não encontrado com ID: " + id));
        return ResponseEntity.ok(new NoGrafoResponse(noGrafo));
    }

    @GetMapping
    public ResponseEntity<List<NoGrafoResponse>> getAllNosGrafo() {
        List<NoGrafoResponse> responseList = noGrafoRepository.findAll().stream()
                .map(NoGrafoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoGrafoResponse> updateNoGrafo(@PathVariable Long id, @Valid @RequestBody NoGrafoRequest request) {
        NoGrafo noGrafo = noGrafoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nó de grafo não encontrado com ID: " + id));

        noGrafo.setLatitude(request.getLatitude());
        noGrafo.setLongitude(request.getLongitude());
        noGrafo.setTipo(request.getTipo());
        noGrafo = noGrafoRepository.save(noGrafo);
        return ResponseEntity.ok(new NoGrafoResponse(noGrafo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoGrafo(@PathVariable Long id) {
        if (!noGrafoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nó de grafo não encontrado com ID: " + id);
        }
        noGrafoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
