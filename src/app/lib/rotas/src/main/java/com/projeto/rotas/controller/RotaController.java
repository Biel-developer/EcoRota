package com.projeto.rotas.controller;

import com.projeto.rotas.dto.OtimizarRotaRequest;
import com.projeto.rotas.dto.RotaResponse;
import com.projeto.rotas.model.RotaOtimizada;
import com.projeto.rotas.repository.RotaOtimizadaRepository;
import com.projeto.rotas.service.RotaOtimizacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/rotas")
public class RotaController {

    private final RotaOtimizacaoService rotaOtimizacaoService;
    private final RotaOtimizadaRepository rotaOtimizadaRepository;

    public RotaController(RotaOtimizacaoService rotaOtimizacaoService, RotaOtimizadaRepository rotaOtimizadaRepository) {
        this.rotaOtimizacaoService = rotaOtimizacaoService;
        this.rotaOtimizadaRepository = rotaOtimizadaRepository;
    }

    @PostMapping("/otimizar")
    public ResponseEntity<RotaResponse> otimizarRota(@Valid @RequestBody OtimizarRotaRequest request) {
        try {
            RotaOtimizada rotaOtimizada = rotaOtimizacaoService.otimizarRota(request.getCaminhaoId(), request.getTiposLixo());
            return ResponseEntity.status(HttpStatus.CREATED).body(new RotaResponse(rotaOtimizada));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotaResponse> getRotaById(@PathVariable Long id) {
        RotaOtimizada rotaOtimizada = rotaOtimizadaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rota otimizada não encontrada com ID: " + id));
        return ResponseEntity.ok(new RotaResponse(rotaOtimizada));
    }

    @GetMapping
    public ResponseEntity<List<RotaResponse>> getAllRotas() {
        List<RotaResponse> responseList = rotaOtimizadaRepository.findAll().stream()
                .map(RotaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

}