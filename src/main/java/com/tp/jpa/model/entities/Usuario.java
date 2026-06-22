package com.tp.jpa.model.entities;

import com.tp.jpa.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true) // Incluye los datos de Base en el texto
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

// Creo la clase Usuario, que hereda los atributos comunes de Base
@Entity
public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    // Hay una asociación. El Usuario conoce a sus pedidos, de 1:m Por eso es una lista. Se usa Set
    @Builder.Default
    @OneToMany (cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="id_usuario")
    private Set<Pedido> pedidos = new HashSet<>();

    public void addPedido(Pedido pedido) {
        // Solo por si la lista arranca en null
        if (this.pedidos == null) {
            this.pedidos = new HashSet<>();
        }
        this.pedidos.add(pedido);
    }
}
