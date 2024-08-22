package com.joelmaciel.api_gerenciador.api.controllers;

import com.joelmaciel.api_gerenciador.api.dtos.response.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.request.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaResumoDTO;
import com.joelmaciel.api_gerenciador.domain.services.ListaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping
    public Page<ListaResumoDTO> buscarTodasListas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return listaService.buscarTodas(dataInicio, dataFim, pageable);
    }

    @GetMapping("/{listaId}")
    public ListaDTO buscarListaPorId(@PathVariable Long listaId) {
        return listaService.buscarListaPorId(listaId);
    }

    @DeleteMapping("/{listaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarLista(@PathVariable Long listaId) {
        listaService.deletarLista(listaId);
    }


}
