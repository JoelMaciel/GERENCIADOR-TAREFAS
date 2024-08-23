package com.joelmaciel.api_gerenciador.domain.services.impl;

import com.joelmaciel.api_gerenciador.api.converter.ListaConverter;
import com.joelmaciel.api_gerenciador.api.dtos.request.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaResumoDTO;
import com.joelmaciel.api_gerenciador.domain.exceptions.ListaNaoEncontradaException;
import com.joelmaciel.api_gerenciador.domain.models.Lista;
import com.joelmaciel.api_gerenciador.domain.repositories.ListaRepository;
import com.joelmaciel.api_gerenciador.domain.services.ListaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ListaServiceImpl implements ListaService {

    private final ListaRepository listaRepository;

    @Transactional
    @Override
    public ListaDTO adicionarLista(ListaRequestDTO listaRequestDTO) {
        Lista lista = ListaConverter.toModel(listaRequestDTO);
        return ListaConverter.toDTO(listaRepository.save(lista));
    }

    @Override
    public Page<ListaResumoDTO> buscarTodas(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (dataInicio != null && dataFim != null) {
            return listaRepository.findByDataCriacaoBetween(dataInicio, dataFim, pageable)
                    .map(ListaConverter::toSummaryDTO);
        } else {
            return listaRepository.findAll(pageable)
                    .map(ListaConverter::toSummaryDTO);
        }
    }

    @Override
    public ListaDTO buscarListaPorId(Long listaId) {
        Lista lista = buscarOptionalLista(listaId);

        lista.getItens().sort(
                (item1, item2) -> Boolean.compare(item2.isPrioritaria(), item1.isPrioritaria()));

        return ListaConverter.toDTO(lista);
    }

    @Transactional
    @Override
    public void deletarLista(Long listaId) {
        Lista lista = buscarOptionalLista(listaId);
        listaRepository.delete(lista);
    }

    @Override
    public Lista buscarOptionalLista(Long listaId) {
        return listaRepository.findById(listaId)
                .orElseThrow(() -> new ListaNaoEncontradaException(listaId));
    }

}
