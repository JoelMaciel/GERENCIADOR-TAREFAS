package com.joelmaciel.api_gerenciador.domain.services.impl;

import com.joelmaciel.api_gerenciador.api.converter.ListaConverter;
import com.joelmaciel.api_gerenciador.api.dtos.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.domain.models.Lista;
import com.joelmaciel.api_gerenciador.domain.repositories.ListaRepository;
import com.joelmaciel.api_gerenciador.domain.services.ListaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
