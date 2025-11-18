package com.exercicio.extra.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "apostas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultadoEnum escolha;

    @Column(nullable = false)
    private Double valor;

    @Column
    private Double retorno;

    @Column
    private Boolean acertou;
}
