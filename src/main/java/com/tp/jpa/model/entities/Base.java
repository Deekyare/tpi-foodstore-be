package com.tp.jpa.model.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder //Necesario para que funcione la herencia con el Builder
@MappedSuperclass
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Para saber si lo borramos lógicamente
    @Builder.Default
    private boolean eliminado = false;
    // Para saber cuándo se creó el registro
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base base = (Base) o;
        // Si el ID es null, significa que el objeto es nuevo.
        // Solo son iguales si son la misma instancia en memoria.
        // Si tienen ID, comparamos los IDs.
        return id != null && id.equals(base.id);
    }

    @Override
    public int hashCode() {
        // Retornamos un hash constante para la clase.
        // Esto evita que el "cajón" del Set cambie cuando el objeto pase de null a tener ID.
        return getClass().hashCode();
    }
}


