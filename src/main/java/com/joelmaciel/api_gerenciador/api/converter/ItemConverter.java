package com.joelmaciel.api_gerenciador.api.converter;

import com.joelmaciel.api_gerenciador.api.dtos.ItemDTO;
import com.joelmaciel.api_gerenciador.api.dtos.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import com.joelmaciel.api_gerenciador.domain.models.Item;

import java.time.LocalDateTime;

public class ItemConverter {

    private ItemConverter() {
    }

    public static Item toModel(ItemRequestDTO itemRequestDTO) {
        return Item.builder()
                .titulo(itemRequestDTO.getTitulo())
                .descricao(itemRequestDTO.getDescricao())
                .status(StatusItem.PENDENTE)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    public static ItemDTO toDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .titulo(item.getTitulo())
                .descricao(item.getDescricao())
                .status(item.getStatus())
                .isPrioritaria(item.isPrioritaria())
                .dataCriacao(item.getDataCriacao())
                .dataConclusao(item.getDataConclusao())
                .build();
    }
}
