package com.joelmaciel.api_gerenciador.api.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListaRequestDTO {

    @NotBlank
    @Size(min = 8, max = 50)
    private String nome;

    @Valid
    @Size(min = 1)
    @NotNull
    private List<ItemRequestDTO> itens;
}
