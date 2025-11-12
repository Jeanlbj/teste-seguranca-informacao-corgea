package com.exercicio.extra.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carteiras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double saldo;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
