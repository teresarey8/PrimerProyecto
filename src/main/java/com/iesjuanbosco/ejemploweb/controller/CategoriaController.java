package com.iesjuanbosco.ejemploweb.controller;

import com.iesjuanbosco.ejemploweb.DTO.CategoriaCosteMedioDTO;
import com.iesjuanbosco.ejemploweb.entity.Categoria;
import com.iesjuanbosco.ejemploweb.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Esta anotación define que este métodoo se va a ejecutar cuando el servidor reciba una solicitud HTTP GET para la URL /categorias/.
    @GetMapping ("/categorias")
    //model Es un objeto proporcionado por Spring que se utiliza para pasar datos del controlador a la vista (en este caso, a una plantilla).
    public String categoria(Model model) {
        //Con esto obtendríamos todas las categorías
        //List<Categoria> categorias  = categoriaRepository.findAll();
        //Con esto hacemos una consulta personalizada para obtener el coste medio y número de productos por categoria

        //Aquí se realiza una consulta personalizada al repositorio (categoriaRepository) para recuperar datos complejos sobre las categorías.
        //La consulta personalizada devuelve una lista de objetos CategoriaCosteMedioDTO, que es un objeto DTO (Data Transfer Object) que probablemente contiene campos como el nombre de la categoría,
        // el coste medio de los productos en esa categoría, y el número de productos asociados.
        List<CategoriaCosteMedioDTO> categoriasConStats = categoriaService.obtenerCategoriasConStats();
        //Aquí se añaden las estadísticas de las categorías al modelo, para que estos datos estén disponibles en la vista.
        model.addAttribute("categorias", categoriasConStats);
        return "categoria-list";
    }
    @GetMapping("/categoria/del/{id}")
    public String borrarCategoria(@PathVariable("id") Long id) {
        categoriaService.eliminarCategoria(id);
        return "redirect:/categorias";  // Redirige de nuevo a la lista de categorías
    }
//    @GetMapping("/categorias/view/{id}")
//    // La anotación @PathVariable vincula el valor del parámetro id en la URL con el argumento del métodoo.
//    // Esto significa que el valor de {id} en la URL será recibido en este parámetro como un Long.
//    public String view(Model model, @PathVariable Long id) {
//        // El métodoo findById(id) devuelve un objeto Optional,
//        // que es una clase envolvente en Java que puede o no contener un valor.
//        // Esto es útil para evitar NullPointerExceptions si no se encuentra una categoría con ese ID.
//        Optional<Categoria> categoria = categoriaService.findById(id);
//        //entonces como puede contener o no la categoria, lo comprueba
//        if(categoria.isPresent()) {
//            //si existe , usamos get para extraerla y agregarla al modelo con el nombre categoria
//            model.addAttribute("categoria", categoria.get());
//            return "categoria-view";
//        }else{
//            return "redirect:/categorias/";
//        }
//
//    }
    @GetMapping("/categorias/new")
    public String addCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria-new";
    }

    @PostMapping("/categorias/new")
    public String addCategoriaInsert(@ModelAttribute("categoria") Categoria categoria,
                                @RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
            try {
                categoriaService.guardarCategoria(categoria, file);
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
                return "redirect:/categorias/new";
            }
            return "redirect:/categorias";
    }

}
