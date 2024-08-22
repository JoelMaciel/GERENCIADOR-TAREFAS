package com.joelmaciel.api_gerenciador.api.controllers;

import com.joelmaciel.api_gerenciador.api.dtos.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.domain.services.ListaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/listas")
public class ListaController {

    private final ListaService listaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListaDTO adicionarLista(@RequestBody @Valid ListaRequestDTO listaRequestDTO) {
        return listaService.adicionarLista(listaRequestDTO);
    }

}
