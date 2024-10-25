package com.iesjuanbosco.ejemploweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaCosteMedioDTO {
    //esto existe por que una consulta sql me va a devolver tres campos que no estan en mi entidad, asi que me creo esto
    private Long id;
    private String nombreCategoria;
    private Double costeMedio;
    private Long numeroProductos;
    private String foto;
}
