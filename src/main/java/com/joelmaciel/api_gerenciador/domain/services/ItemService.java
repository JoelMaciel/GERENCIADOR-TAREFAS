package com.joelmaciel.api_gerenciador.domain.services;

import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ItemDTO;
import com.joelmaciel.api_gerenciador.domain.models.Item;

public interface ItemService {

    ItemDTO adicionarItem(Long listaId, ItemRequestDTO itemRequestDTO);

    ItemDTO atualizarItem(Long listaId, Long itemId, ItemRequestDTO itemRequestDTO);

    Item buscarOptionalItem(Long itemId);

    void deleteItem(Long listaId, Long itemId);

    void iniciarItem(Long listaId, Long itemId);

    void concluirItem(Long listaId, Long itemId);

    void alterarPrioridadeItem(Long listaId, Long itemId);
}
