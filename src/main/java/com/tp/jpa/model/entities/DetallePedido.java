package com.tp.jpa.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@ToString(callSuper = true) // Incluye los datos de Base en el texto
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal; // Precio * cantidad
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    public DetallePedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        if (producto != null) {
            this.subtotal = cantidad * producto.getPrecio();
        }
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        // Si cambio la cantidad tengo que volver a calcular el subtotal
        if (this.producto != null) {
            this.subtotal = cantidad * this.producto.getPrecio();
        }
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        //  Por si el producto viene null
        if (producto != null) {
            this.subtotal = this.cantidad * producto.getPrecio();
        } else {
            this.subtotal = 0.0;
        }
    }

    @Override
    public boolean equals(Object o) {
        // Si ambos objetos apuntan a la misma dirección de memoria, son el mismo.
        if (this == o) return true;

        // Si el objeto recibido es nulo o no pertenece a la misma clase
        // entonces es imposible que sean iguales.
        if (o == null || getClass() != o.getClass()) return false;

        // Se lo tratamos como DetallePedido para poder analizarlo.
        DetallePedido este = (DetallePedido) o;
        // Decimos que dos detalles son iguales si contienen el mismo Producto y datos.
        // Esto evita que en el pedido aparezcan dos líneas con la misma hamburguesa.
        return Objects.equals(producto, este.producto);
    }

    @Override
    public int hashCode() {
        // Si dos detalles tienen el mismo producto, irán al mismo "lugar" del Set.
        return Objects.hash(producto);
    }
}