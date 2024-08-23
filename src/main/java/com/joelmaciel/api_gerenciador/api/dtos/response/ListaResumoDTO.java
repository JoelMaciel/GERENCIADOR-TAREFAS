package com.joelmaciel.api_gerenciador.api.dtos.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaResumoDTO {

    private Long id;
    private String nome;
    private LocalDate dataCriacao;
}
