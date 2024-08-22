package com.joelmaciel.api_gerenciador.domain.models;

import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item")
public class Item {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusItem status;

    private boolean isPrioritaria = false;

    private LocalDate dataCriacao;
    private LocalDate dataConclusao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id")
    private Lista lista;

}

