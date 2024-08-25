package com.joelmaciel.api_gerenciador.api.converter;

import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ItemDTO;
import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import com.joelmaciel.api_gerenciador.domain.models.Item;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ItemConverter {

    private ItemConverter() {
    }

    public Item toModel(ItemRequestDTO itemRequestDTO) {
        return Item.builder()
                .titulo(itemRequestDTO.getTitulo())
                .descricao(itemRequestDTO.getDescricao())
                .status(StatusItem.PENDENTE)
                .dataCriacao(LocalDate.now())
                .build();
    }

    public ItemDTO toDTO(Item item) {
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
