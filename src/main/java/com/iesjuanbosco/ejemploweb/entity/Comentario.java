package com.iesjuanbosco.ejemploweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
//crea objetos
@Builder
@Entity
@Table(name = "comentario")
//incluye los getter y setter, constuctor, toString, el data  nos pone toodo  , beneficios de lombok en pom.xl

public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String texto;
    private LocalDate fecha;

    @ManyToOne(targetEntity = Producto.class)
    private Producto producto;


}
