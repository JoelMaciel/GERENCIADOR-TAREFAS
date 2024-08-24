package com.joelmaciel.api_gerenciador.api.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
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
