package com.joelmaciel.api_gerenciador.api.dtos.response;

import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private StatusItem status;
    private boolean isPrioritaria;
    private LocalDate dataCriacao;
    private LocalDate dataConclusao;
}
