package com.joelmaciel.api_gerenciador.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import lombok.*;

import java.time.LocalDateTime;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataCriacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataConclusao;
}
