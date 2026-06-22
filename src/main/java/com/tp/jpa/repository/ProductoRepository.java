package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Producto;

public class ProductoRepository extends BaseRepository<Producto> {
    public ProductoRepository() {
        super(Producto.class);
    }
}
