package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CategoriaRepository extends BaseRepository<Categoria> {

    // Este es el constructor que necesita el padre
    public CategoriaRepository() {
        super(Categoria.class);
    }

    /* Consulta JPQL: retorna los productos activos de una categoría. Como la relación es unidireccional y Categoria es la dueña,
     se navega desde Categoria hacia su colección c.productos mediante JOIN. Se filtra por el id de la categoría (parámetro nombrado :catId)
     y por p.eliminado = false para excluir las bajas lógicas.*/

    public List<Producto> buscarProductosPorCategoria(Long categoriaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p FROM Categoria c JOIN c.productos p " + "WHERE c.id = :catId AND p.eliminado = false";
            List<Producto> consulta = em.createQuery(jpql, Producto.class)
                    .setParameter("catId", categoriaId)
                    .getResultList();
            return consulta;
        } finally {
            em.close();
        }
    }
}