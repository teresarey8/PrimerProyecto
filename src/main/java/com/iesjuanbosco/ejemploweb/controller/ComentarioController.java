package com.iesjuanbosco.ejemploweb.controller;

import com.iesjuanbosco.ejemploweb.entity.Comentario;
import com.iesjuanbosco.ejemploweb.entity.Producto;
import com.iesjuanbosco.ejemploweb.repository.ComentarioRepository;
import com.iesjuanbosco.ejemploweb.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class ComentarioController {

    @Autowired
    ComentarioRepository comentarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping("/productos/view/{idProducto}/comentarios/add")
    //El objeto Comentario será rellenado automáticamente con los datos enviados en la solicitud POST
    public String AddComentario(@ModelAttribute Comentario comentario,
                                //Esta anotación indica que el parámetro idProducto se tomará directamente de la URL, vinculando el valor del ID del producto a este argumento.
                                @PathVariable Long idProducto
    ) {
        comentario.setFecha(LocalDate.now());
        //buscamos en el repositorio por el id que esto devuelve un opcional, osea que puede existir o no
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        //Propósito: Enlazar el comentario con el producto para establecer una relación entre ellos en la base de datos.
        comentario.setProducto(producto);
        comentarioRepository.save(comentario);
        return "redirect:/productos/view/"+idProducto;
    }
}
