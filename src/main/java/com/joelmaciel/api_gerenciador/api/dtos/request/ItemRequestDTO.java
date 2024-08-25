package com.joelmaciel.api_gerenciador.api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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
