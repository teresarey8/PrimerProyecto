package com.iesjuanbosco.ejemploweb.controller;

import com.iesjuanbosco.ejemploweb.entity.Categoria;
import com.iesjuanbosco.ejemploweb.entity.Comentario;
import com.iesjuanbosco.ejemploweb.entity.Producto;
import com.iesjuanbosco.ejemploweb.repository.CategoriaRepository;
import com.iesjuanbosco.ejemploweb.repository.ComentarioRepository;
import com.iesjuanbosco.ejemploweb.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

//Para que Spring sepa que esta clase es un controlador tenemos que añadir la anotación @Controller antes de la clase
@Controller
public class ProductoController {

    private final ComentarioRepository comentarioRepository;
    //Para acceder al repositorio creamos una propiedad y la asignamos en el constructor
    private ProductoRepository productoRepository;
    private CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, ComentarioRepository comentarioRepository){

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.comentarioRepository = comentarioRepository;
    }

    /* Con la anotación GetMapping le indicamos a Spring que el siguiente método
       se va a ejecutar cuando el usuario acceda a la URL http://localhost/productos */
    @GetMapping("/productos")
    public String findAll(Model model){
        List<Producto> productos = this.productoRepository.findAll();
        //le pasamos todas las categorias a la vista, para el filtro
        List<Categoria> categorias = this.categoriaRepository.findAll();
        //Pasamos los datos a la vista
        model.addAttribute("productos",productos);
        model.addAttribute("titulo","Titulo de página");
        //ya tengo las categorias ahora se las añado al modelo y salen
        model.addAttribute("categorias",categorias);

        return "producto-list";
    }

    //Borra un producto a partir del id de la ruta
    @GetMapping("/productos/del/{id}")
    public String delete(@PathVariable Long id){
        //Borrar el producto usando el repositorio
        productoRepository.deleteById(id);
        //Redirigir al listado de getProductos: /getProductos
        return "redirect:/productos";
    }

    //Muestra un producto a partir del id de la ruta
    @GetMapping("/productos/view/{id}")
    public String view(@PathVariable Long id, Model model){
        //Obtenemos el producto de la BD a partir del id de la barra de direcciones
        Optional<Producto> producto = productoRepository.findById(id);
        if(producto.isPresent()){
            //Mandamos el producto y el comentario a la vista
            List <Comentario> comentarios = comentarioRepository.findByProducto(producto.get());
            model.addAttribute("producto",producto.get());
            model.addAttribute("comentarios", comentarios);
            model.addAttribute("comentario", new Comentario());
            return "producto-view";
        }
        else{
            return "redirect:/productos";
        }
    }
    /*
    @PostMapping("/productos/view/{id}/comentarios")
    public String addComentario(@PathVariable Long id, @ModelAttribute("nuevoComentario") Comentario comentario, Model model) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        comentario.setProducto(producto);
        comentarioRepository.save(comentario);  // Guardar el comentario
        //recargo los comentarios en el modelo
        List<Comentario> comentarios = comentarioRepository.findByProducto(producto);
        model.addAttribute("comentarios",comentarios);
        return "redirect:/productos/view/{id}/comentarios";
    }
    */
    @GetMapping("/productos/new")
    public String newProducto(Model model){
    //le paso un objeto vacio
        model.addAttribute("producto", new Producto());
        //le añado el objeto categorias, para que salgan todas las categorias que hay y eliga
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "producto-new";
    }

    @PostMapping("/productos/new")
    public String newProductoInsert(Model model ,@Valid Producto producto, BindingResult bindingResult){
        //Si ha habido errores de validación volvemos a mostrar el formulario
        if(bindingResult.hasErrors()){
            Sort sort = Sort.by("nombre").ascending();
            model.addAttribute("categorias", categoriaRepository.findAll(sort));
            return "producto-new";
        }

        //Si no ha habido errores de validación insertamos los datos en la BD
        productoRepository.save(producto);
        //Redirigimos a /getProductos

        return "redirect:/productos";
    }

    @GetMapping("/productos/edit/{id}")
    public String editProducto(@PathVariable Long id, Model model){
        Optional<Producto> producto = productoRepository.findById(id);
        if(producto.isPresent()){
            //Pasamos el objeto a la vista
            model.addAttribute("producto",producto.get());
            return "producto-edit";
        }

        return "redirect:/productos";
    }

    @PostMapping("/productos/edit/{id}")
    public String editProductoUpdate(@PathVariable Long id,
                                    Producto producto){
        producto.setId(id);
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/productos/categoria/{id}")
    public String findAll(Model model, @PathVariable Long id) {

        if(id.equals(-1L)){
            return "redirect:/productos";
        }
        Optional<Categoria> categoriaSeleccionada = categoriaRepository.findById(id);
        if (categoriaSeleccionada.isPresent()) {
            List<Producto> productos = this.productoRepository.findByCategoria(categoriaSeleccionada.get());
            List<Categoria> categorias = this.categoriaRepository.findAll();
            //Pasamos todos los datos necesarios a la vista
            //Pasamos el id de la categoría seleccionada, lo paso para que thymeleaf me ponga el selected="selected"
            //en la categoría que estamos y aparezca seleccionada en el desplegable
            model.addAttribute("selectedCategoriaId", id);
            model.addAttribute("productos", productos);
            model.addAttribute("categorias", categorias);

            return "producto-list";
        } else {
            //Categoría seleccionada no existe, redirijo a listado de productos
            return "redirect:/productos";
        }
    }
}
