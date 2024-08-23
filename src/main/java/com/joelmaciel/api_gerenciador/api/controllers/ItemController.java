package com.joelmaciel.api_gerenciador.api.controllers;

import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ItemDTO;
import com.joelmaciel.api_gerenciador.domain.services.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/listas/{listaId}/itens")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO adicionarItem(@PathVariable Long listaId, @RequestBody @Valid ItemRequestDTO itemRequestDTO) {
        return itemService.adicionarItem(listaId, itemRequestDTO);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO atualizarItem(
            @PathVariable Long listaId,
            @PathVariable Long itemId,
            @RequestBody @Valid ItemRequestDTO itemRequestDTO
    ) {
        return itemService.atualizarItem(listaId, itemId, itemRequestDTO);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarItem(@PathVariable Long listaId, @PathVariable Long itemId) {
        itemService.deleteItem(listaId, itemId);
    }

    @PatchMapping("/{itemId}/status-progresso")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void iniciarItem(@PathVariable Long listaId, @PathVariable Long itemId) {
        itemService.iniciarItem(listaId, itemId);
    }

    @PatchMapping("/{itemId}/status-confirmado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void concluirItem(@PathVariable Long listaId, @PathVariable Long itemId) {
        itemService.concluirItem(listaId, itemId);
    }

}
