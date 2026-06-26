package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Base;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

// Al poner <T extends Base>, nos aseguramos de poder usar los métodos de Base
public abstract class BaseRepository<T extends Base> {

    // La clase que vamos a manejar (Usuario, Producto, etc.)
    private final Class<T> entityClass;

    // Constructor que recibe la Class<T>
    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // guardar(T entity)
    public T guardar(T entity) {
        // Abre su propio EntityManager
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            // Si el id es null, es un alta, se usa persist
            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                // Si ya tiene id, es una actulización. Se usa merge
                entity = em.merge(entity);
            }

            tx.commit();
            return entity; // Retorna la entidad gestionada (con su ID asignado)
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Hace rollback ante cualquier excepción
                System.out.println("Transacción revertida por error en guardar.");
            }
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // buscarPorId(Long id)
    public Optional<T> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            // Si existe retorna Optional.of, si no retorna Optional.empty()
            if (entity != null) {
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } finally {
            em.close(); //
        }
    }

    // listarActivos()
    public List<T> listarActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Usa JPQL. entityClass.getSimpleName() reemplaza dinámicamente "Usuario" o "Producto"
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.eliminado = false";
            return em.createQuery(jpql, entityClass).getResultList();
        } finally {
            em.close();
        }
    }

    // eliminado lógico(Long id)
    public boolean eliminarLogico(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            // Busca el registro por ID
            T entity = em.find(entityClass, id);

            if (entity != null) {
                // Establece eliminado = true gracias a que heredamos de Base
                entity.setEliminado(true);
                // Actualiza el registro en la base de datos
                em.merge(entity);
                tx.commit();
                return true; // Encontró el registro y lo eliminó lógicamente
            }
            tx.commit();
            return false; // No lo encontró

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}