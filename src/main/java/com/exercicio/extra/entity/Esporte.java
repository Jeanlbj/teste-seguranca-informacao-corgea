package com.exercicio.extra.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "esportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Esporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;
}
