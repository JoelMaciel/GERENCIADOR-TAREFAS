package com.joelmaciel.api_gerenciador.api.converter;

import com.joelmaciel.api_gerenciador.api.dtos.ItemDTO;
import com.joelmaciel.api_gerenciador.api.dtos.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.domain.models.Item;
import com.joelmaciel.api_gerenciador.domain.models.Lista;

import java.time.LocalDateTime;
import java.util.List;

public class ListaConverter {

    private ListaConverter() {
    }

    public static ListaDTO toDTO(Lista lista) {
        List<ItemDTO> itemDTOS = lista.getItens().stream()
                .map(ItemConverter::toDTO)
                .toList();

        return ListaDTO.builder()
                .id(lista.getId())
                .nome(lista.getNome())
                .dataCriacao(LocalDateTime.now())
                .itens(itemDTOS)
                .build();
    }

    public static Lista toModel(ListaRequestDTO listaRequestDTO) {
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
