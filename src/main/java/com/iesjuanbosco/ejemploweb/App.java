package com.iesjuanbosco.ejemploweb;

import com.iesjuanbosco.ejemploweb.entity.Categoria;
import com.iesjuanbosco.ejemploweb.entity.Producto;
import com.iesjuanbosco.ejemploweb.repository.CategoriaRepository;
import com.iesjuanbosco.ejemploweb.repository.ProductoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class App {

	//Comentario de ejemplo
	public static void main(String[] args) {

		ApplicationContext contex = SpringApplication.run(App.class, args);

		/*Así podemos acceder a un Bean (un respositorio, un controlador, una entidad, etc.) de Spring
		para, por ejemplo, insertar datos de ejemplo nada más ejecutar la app
		*/
		/*
		ProductoRepository productoRepository =  contex.getBean(ProductoRepository.class);
		CategoriaRepository categoriaRepository = contex.getBean(CategoriaRepository.class);

		Producto p1 =new Producto(null, "titulo",10,50.5);
		Producto p2 =new Producto(null, "titulo2",15,70.5);
		Categoria c = new Categoria(null, "Moviles", "Los mejores móviles");

		//Añado el producto 1 a la categoría Móviles
		p1.setCategoria(c);
		c.getProductos().add(p1);

		//Añado el producto 2 a la categoría Móviles
		p2.setCategoria(c);
		c.getProductos().add(p2);

		//Como la relación tiene CASCADE.ALL se guardan en cascada y guarda los productos de la categoría
		categoriaRepository.save(c);

		 */
		ProductoRepository productoRepository =  contex.getBean(ProductoRepository.class);
		CategoriaRepository categoriaRepository = contex.getBean(CategoriaRepository.class);

		List<Producto> productos = new ArrayList<Producto>();
		Categoria categoria = Categoria.builder()
				.nombre("Moviles")
				.descripcion("Los mejores moviles")
				.build();
		Producto p1 = Producto.builder()
				.cantidad(50)
				.precio(45.0)
				.titulo("Producto1")
				//que no se olvide asignar una categoria a cada producto
				.categoria(categoria)
				.build();
		Producto p2 = Producto.builder()
				.cantidad(60)
				.precio(115.0)
				.titulo("Producto2")
				.categoria(categoria)
				.build();
		Producto p3 = Producto.builder()
				.cantidad(90)
				.precio(48.0)
				.titulo("Producto3")
				.categoria(categoria)
				.build();
		Producto p4 = Producto.builder()
				.cantidad(900)
				.precio(200.0)
				.titulo("Producto4")
				.categoria(categoria)
				.build();
		//hay que añadirle la categoria al productos y el productos a la catgeoria
		categoria.setProductos(new ArrayList<Producto>());
		categoria.getProductos().add(p1);
		categoria.getProductos().add(p2);
		categoria.getProductos().add(p3);
		categoria.getProductos().add(p4);
		//Guardamos todos los getProductos en la base de datos utilizando el objeto productoRepository
		categoriaRepository.save(categoria);
	}

}
