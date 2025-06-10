package com.projeto.rotas.repository;


import com.projeto.rotas.model.NoGrafo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoGrafoRepository extends JpaRepository<NoGrafo, Long> {
    // metodos de consulta personalizados podem ser adicionados aqui
    // ex: NoGrafo findByLatitudeAndLongitude(Double latitude, Double longitude);
    // da pra ver de por esses n sei se vai ser necessario
}
