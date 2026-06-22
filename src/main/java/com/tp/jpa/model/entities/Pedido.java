package com.tp.jpa.model.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.interfaces.Calculable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true) // Incluye los datos de Base en el texto
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

// Clase Pedido que hereda de Base y usa la interfaz para calcular el total
@Entity
public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Builder.Default
    private Double total = 0.0;
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    // Los detalles del pedido en un Set para que no se repitan
    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DetallePedido> detalles = new HashSet<>();

    // Método agregar productos al pedido
    public void addDetallePedido(int cantidad, Producto producto) {
        // Primero veo si ya pedimos este producto antes
        DetallePedido detalleExistente = findDetallePedidoByProducto(producto);

        if (detalleExistente != null) {
            // Si ya estaba, solo le sumo la cantidad nueva
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
        } else {
            // Si es nuevo, creo el detalle y lo meto al Set
            DetallePedido nuevoDetalle = new DetallePedido(cantidad, producto);

            // Le asignamos este pedido (this) al detalle para que la FK no se guarde en NULL.
            this.detalles.add(nuevoDetalle);
        }
        // Siempre recalculo el total
        calcularTotal();
    }

    // Función auxiliar para buscar un producto entre los detalles
    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().equals(producto)) {
                return detalle;
            }
        }
        return null;
    }
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalleEncontrado = findDetallePedidoByProducto(producto);

        if (detalleEncontrado != null) {
            this.detalles.remove(detalleEncontrado);
            calcularTotal();
            System.out.println("Se sacó el producto: " + producto.getNombre());
        } else {
            System.out.println("No encontré ese producto en el pedido.");
        }
    }
    // Sumo todos los subtotales para tener el total final
    @Override
    //FORMA FUNCIONAL
    public void calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(detalle -> detalle.getSubtotal())
                .sum();
    }
    //Mostrar por consola la cantidad de ítems que tiene un pedido (ejemplo 2 panchos, 2
    //bebidas, deberá decir que hay 4 items)
    public int obtenerCantidadItems() {
        return detalles.stream()
                .mapToInt(d -> d.getCantidad())
                .sum();
    }
}
