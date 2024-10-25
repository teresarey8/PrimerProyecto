package com.iesjuanbosco.ejemploweb.controller;

import com.iesjuanbosco.ejemploweb.entity.Producto;
import com.iesjuanbosco.ejemploweb.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;

import javax.naming.Binding;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Para que Spring sepa que esta clase es un controlador tenemos que añadir la anotación @Controller antes de la clase
@Controller
public class ProductoController {

    //GET: Se utiliza para obtener datos y mostrar la vista x del producto.
    //POST: Se utiliza para enviar los datos del formulario y actualizar el producto en la base de datos.

    //Para acceder al repositorio creamos una propiedad y la asignamos en el constructor, el repositorio
    //es la base de datos
    private ProductoRepository productoRepository;

    public ProductoController(ProductoRepository repository){
        this.productoRepository = repository;
    }
    //MAPEANDO RUTAS, QUIERO QUE SI EL USUARIO ACCEDE A /PRODUCTOS2, QUIERO QUE RESPONDA ESTE CONTROLADOR
    @GetMapping("/productos2")    //Anotación que indica la URL localhost:8080/productos2 mediante GET
    @ResponseBody       //Anotación que indica que no pase por el motor de plantillas thymeleaf sino que voy a devolver yo el HTML diréctamente
    public String index(){
        List<Producto> productos = this.productoRepository.findAll();
        StringBuilder HTML = new StringBuilder("<html><body>");
        productos.forEach(producto -> {
            HTML.append("<p>" + producto.getTitulo() + "</p>");
        });
        HTML.append("</body></html>");

        return HTML.toString();
    }

    /* Con la anotación GetMapping le indicamos a Spring que el siguiente método
       se va a ejecutar cuando el usuario acceda a la URL http://localhost/productos */
    @GetMapping("/productos")
    public String findAll(Model model){
        //LE DECIMOS QUEENCUENTRE TODOS LOS PRODUCTOS Y LO METEMOS EN ARRAY
        List<Producto> productos = this.productoRepository.findAll();

        //y pasamos los datos a la vista
        model.addAttribute("productos",productos);
        //nombre de lista creada en el template, por defecto será aqui, pero si queremos redirigir ps lo podemos hacer, mas abajo, redirect
        return "producto-list";
    }
    //para añadir productos a mano
    @GetMapping("/productos/add")
    public String add(){
        List<Producto> productos = new ArrayList<Producto>();
        Producto p1 = new Producto(null, "Producto 1", 20, 45.5);
        Producto p2 = new Producto(null, "Producto 2", 50, 5.0);
        Producto p3 = new Producto(null, "Producto 3", 30, 50.5);
        Producto p4 = new Producto(null, "Producto 4", 10, 30.0);
        productos.add(p1);
        productos.add(p2);
        productos.add(p3);
        productos.add(p4);

        //Guardamos todos los productos en la base  datos utilizando el objeto productoRepository
        this.productoRepository.saveAll(productos);

        //Redirige al controlador /productos
        return "redirect:/productos";

    }
    @GetMapping("/productos/del/{id}")
    //cuando haya una url que sea /productos/del/algo que entre aqui y ya utilizarlo para borrar
    public String delete(@PathVariable Long id){
        //borrar el producto usando el repositorio
        //el id se pasa de una pagina a otra a traves de la barra de direcciones, arriba
        productoRepository.deleteById(id);
        //redirigir al listado de productos
        return "redirect:/productos";
    }
    @GetMapping("/productos/view/{id}")
    public String view(@PathVariable Long id, Model model){
        //obtenemos el productos de bd a partir del id de la barra de direcciones
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()){
            //mandamos el porducto a la vista
            model.addAttribute("producto",producto.get());
            return "producto-view";
        }else{
            return "redirect:/productos";
        }
    }

    @GetMapping("/productos/new")
    public String newProducto(Model model){
        model.addAttribute("producto", new Producto());
        return "producto-new";
    }

    //este lo inserta en la base de datos
    @PostMapping("/productos/new")
    public String newProductoInsert(@Valid Producto producto, BindingResult bindingResult){
        //Si ha habido errores de validación volvemos a mostrar el formulario
        if(bindingResult.hasErrors()){
            return "producto-new";
        }
        //Si no ha habido errores de validación insertamos los datos en la BD
        productoRepository.save(producto);
        //Redirigimos a /productos
        return "redirect:/productos";
    }
    @GetMapping("/productos/edit/{id}")
                            //dice que tiene que coger el id de la barra de direcciones
    public String newProducto(@PathVariable Long id, Model model){
        //para ver ese producto lo tendré que coger de nuestra base de datos, que es repositorio
        //se conecta a la bd, busca el producto con ese id y me lo devuelve
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            //pasamos el objeto a la vista
            model.addAttribute("producto", producto.get());
            return "producto-edit";
        }
            return "redirect:/productos";
    }
    @PostMapping("/productos/edit/{id}")
    public String newProductoUpdate(@PathVariable Long id,
                                    Producto producto){
        producto.setId(id);
        productoRepository.save(producto);
        return "redirect:/productos";
    }

}
