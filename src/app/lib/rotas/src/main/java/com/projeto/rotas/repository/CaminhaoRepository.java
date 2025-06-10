package com.projeto.rotas.repository;

import com.projeto.rotas.model.Caminhao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {
    Optional<Caminhao> findByPlaca(String placa);
    List<Caminhao> findByStatus(String status);
}