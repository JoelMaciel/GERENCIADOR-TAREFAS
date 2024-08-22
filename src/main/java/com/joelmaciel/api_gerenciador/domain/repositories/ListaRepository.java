package com.joelmaciel.api_gerenciador.domain.repositories;

import com.joelmaciel.api_gerenciador.domain.models.Lista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Long> {

    Page<Lista> findByDataCriacaoBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);
}
