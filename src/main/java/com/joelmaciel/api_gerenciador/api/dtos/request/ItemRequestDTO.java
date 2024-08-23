package com.joelmaciel.api_gerenciador.api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ItemRequestDTO {

    @NotBlank
    @Size(min = 8, max = 50)
    private String titulo;

    @NotBlank
    @Size(min = 10, max = 200)
    private String descricao;

}
