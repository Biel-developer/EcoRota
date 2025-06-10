package com.projeto.rotas.repository;

import com.projeto.rotas.model.PontoDeColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoDeColetaRepository extends JpaRepository<PontoDeColeta, Long> {
    List<PontoDeColeta> findByStatus(String status);
    List<PontoDeColeta> findByTipoLixoAndStatus(String tipoLixo, String status);
    List<PontoDeColeta> findByPrioridadeGreaterThanEqualAndStatus(Integer prioridade, String status);
}
