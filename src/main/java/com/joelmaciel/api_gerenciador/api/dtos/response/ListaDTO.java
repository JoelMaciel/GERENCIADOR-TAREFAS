package com.joelmaciel.api_gerenciador.api.dtos.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaDTO {

    private Long id;
    private String nome;
    private LocalDate dataCriacao;
    private List<ItemDTO> itens;
}
