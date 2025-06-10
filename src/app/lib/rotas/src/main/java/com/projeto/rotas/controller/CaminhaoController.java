package com.projeto.rotas.controller;

import com.projeto.rotas.dto.CaminhaoRequest;
import com.projeto.rotas.dto.CaminhaoResponse;
import com.projeto.rotas.model.Caminhao;
import com.projeto.rotas.repository.CaminhaoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/caminhoes")
public class CaminhaoController {

    private final CaminhaoRepository caminhaoRepository;

    public CaminhaoController(CaminhaoRepository caminhaoRepository) {
        this.caminhaoRepository = caminhaoRepository;
    }

    @PostMapping
    public ResponseEntity<CaminhaoResponse> createCaminhao(@Valid @RequestBody CaminhaoRequest request) {
        Caminhao caminhao = new Caminhao(
                request.getPlaca(),
                request.getCapacidadeKg(),
                request.getCapacidadeVolumeM3(),
                request.getStatus()
        );
        caminhao.setLocalizacaoAtualLatitude(request.getLocalizacaoAtualLatitude());
        caminhao.setLocalizacaoAtualLongitude(request.getLocalizacaoAtualLongitude());
        caminhao = caminhaoRepository.save(caminhao);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CaminhaoResponse(caminhao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaminhaoResponse> getCaminhaoById(@PathVariable Long id) {
        Caminhao caminhao = caminhaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caminhão não encontrado com ID: " + id));
        return ResponseEntity.ok(new CaminhaoResponse(caminhao));
    }

    @GetMapping
    public ResponseEntity<List<CaminhaoResponse>> getAllCaminhoes() {
        List<CaminhaoResponse> responseList = caminhaoRepository.findAll().stream()
                .map(CaminhaoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaminhaoResponse> updateCaminhao(@PathVariable Long id, @Valid @RequestBody CaminhaoRequest request) {
        Caminhao caminhao = caminhaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caminhão não encontrado com ID: " + id));

        caminhao.setPlaca(request.getPlaca());
        caminhao.setCapacidadeKg(request.getCapacidadeKg());
        caminhao.setCapacidadeVolumeM3(request.getCapacidadeVolumeM3());
        caminhao.setLocalizacaoAtualLatitude(request.getLocalizacaoAtualLatitude());
        caminhao.setLocalizacaoAtualLongitude(request.getLocalizacaoAtualLongitude());
        caminhao.setStatus(request.getStatus());
        caminhao = caminhaoRepository.save(caminhao);
        return ResponseEntity.ok(new CaminhaoResponse(caminhao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaminhao(@PathVariable Long id) {
        if (!caminhaoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Caminhão não encontrado com ID: " + id);
        }
        caminhaoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}