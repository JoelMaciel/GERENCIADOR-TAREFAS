package com.joelmaciel.api_gerenciador.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "lista")
public class Lista {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL)
    private List<Item> itens = new ArrayList<>();
}
