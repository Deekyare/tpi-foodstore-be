package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository extends BaseRepository<Usuario> {
    public UsuarioRepository() {
        super(Usuario.class);
    }

    /* Consulta JPQL: busca un usuario activo por su dirección de correo electrónico
   Retorna Optional para manejar el caso en que el mail no esté registrado*/

    public Optional<Usuario> buscarPorMail(String mail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.mail = :mail AND u.eliminado = false";
            TypedQuery<Usuario> consulta = em.createQuery(jpql, Usuario.class)
                    .setParameter("mail", mail);
            List<Usuario> res = consulta.getResultList();
            return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
        } finally {
            em.close();
        }
    }

    /* Consulta JPQL: retorna los pedidos activos de un usuario. Como la relación es unidireccional y Usuario es el dueño, se navega
    desde Usuario hacia su colección u.pedidos mediante JOIN. Se filtra por el id del usuario (:uid) y por p.eliminado = false
    para excluir las bajas lógicas.*/
    public List<Pedido> buscarPedidosPorUsuario(Long idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p FROM Usuario u JOIN u.pedidos p WHERE u.id = :uid AND p.eliminado = false";
            TypedQuery<Pedido> consulta = em.createQuery(jpql, Pedido.class);
            consulta.setParameter("uid", idUsuario);
            List<Pedido> res = consulta.getResultList();
            return res;
        } finally {
            em.close();
        }
    }

    public Optional<Usuario> buscarPorPedidoId(Long pedidoId) {
        EntityManager em = com.tp.jpa.util.JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u JOIN u.pedidos p WHERE p.id = :id AND u.eliminado = false", Usuario.class)
                    .setParameter("id", pedidoId)
                    .getResultList()
                    .stream()
                    .findFirst(); // Devuelve un Optional<Usuario> de forma limpia
        } finally {
            em.close();
        }
    }
}