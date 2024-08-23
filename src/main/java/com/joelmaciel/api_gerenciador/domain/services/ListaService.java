package com.joelmaciel.api_gerenciador.domain.services;

import com.joelmaciel.api_gerenciador.api.dtos.response.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.request.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaResumoDTO;
import com.joelmaciel.api_gerenciador.domain.models.Lista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ListaService {

    ListaDTO adicionarLista(ListaRequestDTO listaRequestDTO);

    Page<ListaResumoDTO> buscarTodas(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    ListaDTO buscarListaPorId(Long listaId);

    Lista buscarOptionalLista(Long listaId);

    void deletarLista(Long listaId);
}
