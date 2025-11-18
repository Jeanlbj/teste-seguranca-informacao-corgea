package com.exercicio.extra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "esporte_id", nullable = false)
    private Esporte esporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_a_id", nullable = false)
    private Time timeA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_b_id", nullable = false)
    private Time timeB;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private Double oddTimeA;

    @Column(nullable = false)
    private Double oddEmpate;

    @Column(nullable = false)
    private Double oddTimeB;

    @Enumerated(EnumType.STRING)
    private ResultadoEnum resultado;
}
