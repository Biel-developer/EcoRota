package com.projeto.rotas.repository;

import com.projeto.rotas.model.NoGrafo;
import com.projeto.rotas.model.Trecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrechoRepository extends JpaRepository<Trecho, Long> {
    List<Trecho> findByNoOrigem(NoGrafo noOrigem);
    List<Trecho> findByNoDestino(NoGrafo noDestino);
    Optional<Trecho> findByNoOrigemAndNoDestino(NoGrafo noOrigem, NoGrafo noDestino);
}