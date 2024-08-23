package com.joelmaciel.api_gerenciador.domain.services.impl;

import com.joelmaciel.api_gerenciador.api.converter.ItemConverter;
import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ItemDTO;
import com.joelmaciel.api_gerenciador.domain.exceptions.BusinessException;
import com.joelmaciel.api_gerenciador.domain.exceptions.ItemNaoEncontradoException;
import com.joelmaciel.api_gerenciador.domain.models.Item;
import com.joelmaciel.api_gerenciador.domain.models.Lista;
import com.joelmaciel.api_gerenciador.domain.repositories.ItemRepository;
import com.joelmaciel.api_gerenciador.domain.services.ItemService;
import com.joelmaciel.api_gerenciador.domain.services.ListaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    public static final String ITEM_NAO_PERTENCE_A_ESTA_LISTA = "O item não pertence a lista informada.";
    private final ItemRepository itemRepository;
    private final ListaService listaService;

    @Transactional
    @Override
    public ItemDTO adicionarItem(Long listaId, ItemRequestDTO itemRequestDTO) {
        Lista lista = listaService.buscarOptionalLista(listaId);
        Item item = ItemConverter.toModel(itemRequestDTO);
        item.setLista(lista);
        return ItemConverter.toDTO(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDTO atualizarItem(Long listaId, Long itemId, ItemRequestDTO itemRequestDTO) {
        Item item = buscarItemRelacionado(listaId, itemId);

        item.setTitulo(itemRequestDTO.getTitulo());
        item.setDescricao(itemRequestDTO.getDescricao());

        return ItemConverter.toDTO(item);
    }

    @Transactional
    @Override
    public void deleteItem(Long listaId, Long itemId) {
        Item item = buscarItemRelacionado(listaId, itemId);
        itemRepository.delete(item);
    }

    @Override
    public Item buscarOptionalItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNaoEncontradoException(itemId));
    }

    private Item buscarItemRelacionado(Long listaId, Long itemId) {
        Lista lista = listaService.buscarOptionalLista(listaId);
        Item item = buscarOptionalItem(itemId);
        validarRelacaoItemLista(item, lista);
        return item;
    }

    private void validarRelacaoItemLista(Item item, Lista lista) {
        if (!item.getLista().getId().equals(lista.getId())) {
            throw new BusinessException(ITEM_NAO_PERTENCE_A_ESTA_LISTA);
        }
    }

}
