package com.joelmaciel.api_gerenciador.domain.repositories;

import com.joelmaciel.api_gerenciador.domain.models.Lista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Long> {
}
