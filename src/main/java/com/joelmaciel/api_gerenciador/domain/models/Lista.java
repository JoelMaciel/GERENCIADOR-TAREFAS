package com.joelmaciel.api_gerenciador.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "lista")
public class Lista {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();
}
