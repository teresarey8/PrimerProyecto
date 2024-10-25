package com.iesjuanbosco.ejemploweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
//crea objetos
@Builder
@Entity
@Table(name="categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(length = 1000)
    private String descripcion;
    private String foto;
    //aqui se relacionan producto y categoria, en forma de cascada
    @OneToMany(targetEntity = Producto.class, cascade = CascadeType.ALL,
            //aqui le digo como quiero que se llame esta en producto
            mappedBy = "categoria")

    private List<Producto> productos = new ArrayList<>();

}
