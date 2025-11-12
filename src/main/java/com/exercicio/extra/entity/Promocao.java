package com.exercicio.extra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "promocoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double valorBonus;

    @Column(nullable = false)
    private LocalDate dataValidade;
}