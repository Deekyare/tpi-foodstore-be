package com.tp.jpa.repository;
import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PedidoRepository extends BaseRepository<Pedido> {
    public PedidoRepository() {
        super(Pedido.class);
    }

    public List<Pedido> buscarPorEstado(Estado estado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            /*Consulta JPQL: retorna todos los pedidos activos con un estado específico para filtrar PENDIENTE, CONFIRMADO, TERMINADO o CANCELADO*/
            String jpql = "SELECT p FROM Pedido p WHERE p.estado = :estado AND p.eliminado = false";
            List<Pedido> consulta = em.createQuery(jpql, Pedido.class)
                    .setParameter("estado", estado)
                    .getResultList();
            return consulta;
        } finally {
            em.close();
        }
    }
}