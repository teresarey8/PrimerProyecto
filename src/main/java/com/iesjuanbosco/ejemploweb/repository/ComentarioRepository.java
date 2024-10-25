package com.iesjuanbosco.ejemploweb.repository;

import com.iesjuanbosco.ejemploweb.entity.Comentario;
import com.iesjuanbosco.ejemploweb.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    //obtenemos todoss los comentarios filtrados por cada producto
    List<Comentario> findByProducto(Producto producto);
}
