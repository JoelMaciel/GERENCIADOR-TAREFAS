package com.joelmaciel.api_gerenciador.domain.models;

import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
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

    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id")
    private Lista lista;

}

