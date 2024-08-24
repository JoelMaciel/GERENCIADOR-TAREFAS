package com.joelmaciel.api_gerenciador.api.converter;

import com.joelmaciel.api_gerenciador.api.dtos.request.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ItemDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaResumoDTO;
import com.joelmaciel.api_gerenciador.domain.models.Item;
import com.joelmaciel.api_gerenciador.domain.models.Lista;

import java.time.LocalDate;
import java.util.List;

public class ListaConverter {

    private ListaConverter() {
    }

    public ListaDTO toDTO(Lista lista) {
        List<ItemDTO> itemDTOS = lista.getItens().stream()
                .map(ItemConverter::toDTO)
                .toList();

        return ListaDTO.builder()
                .id(lista.getId())
                .nome(lista.getNome())
                .dataCriacao(LocalDate.now())
                .itens(itemDTOS)
                .build();
    }

    public  ListaResumoDTO toSummaryDTO(Lista lista) {
        return ListaResumoDTO.builder()
                .id(lista.getId())
                .nome(lista.getNome())
                .dataCriacao(lista.getDataCriacao())
                .build();
    }

    public  Lista toModel(ListaRequestDTO listaRequestDTO) {
        Lista lista = Lista.builder()
                .nome(listaRequestDTO.getNome())
                .build();

        List<Item> items = listaRequestDTO.getItens().stream()
                .map(ItemConverter::toModel)
                .toList();

        items.forEach(item -> item.setLista(lista));

        lista.setItens(items);
        return lista;
    }
}
