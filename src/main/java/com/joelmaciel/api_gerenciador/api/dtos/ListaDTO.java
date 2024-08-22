package com.joelmaciel.api_gerenciador.api.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaDTO {

    private Long id;
    private String nome;
    private LocalDateTime dataCriacao;
    private List<ItemDTO> itens;
}
