package com.projeto.rotas.repository;

import com.projeto.rotas.model.Caminhao;
import com.projeto.rotas.model.RotaOtimizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface RotaOtimizadaRepository extends JpaRepository<RotaOtimizada, Long> {
    List<RotaOtimizada> findByCaminhao(Caminhao caminhao);
    List<RotaOtimizada> findByStatus(String status);
    List<RotaOtimizada> findByDataGeracaoBetween(OffsetDateTime start, OffsetDateTime end);
}