package com.joelmaciel.api_gerenciador.domain.services;

import com.joelmaciel.api_gerenciador.api.dtos.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.ListaRequestDTO;

public interface ListaService {

    ListaDTO adicionarLista(ListaRequestDTO listaRequestDTO);
}
