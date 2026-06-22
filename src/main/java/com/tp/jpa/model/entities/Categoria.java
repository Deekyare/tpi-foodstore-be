package com.tp.jpa.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true) // Incluye los datos de "Base" en el texto
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

// Las categorías para agrupar productos
@Entity
public class Categoria extends Base {
    private String nombre;
    private String descripcion;
    @Builder.Default
    @OneToMany(mappedBy = "categoria", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    // Acá guardo los productos de la categoría en un Set para no repetir
    private Set<Producto> productos = new HashSet<>();

    public void addProducto(Producto producto) {
        if (this.productos == null) {
            this.productos = new HashSet<>();
        }
        this.productos.add(producto);
    }
}
