package edu.ufg.aulas.dao;

import edu.ufg.aulas.entidades.Aula;
import edu.ufg.aulas.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AulaDAO {

    public boolean existe(String nombre, String edificio) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Long total = session.createQuery(
                    "select count(a) from Aula a where lower(a.nombre) = :nombre and lower(a.edificio) = :edificio",
                    Long.class)
                    .setParameter("nombre", nombre.trim().toLowerCase())
                    .setParameter("edificio", edificio.trim().toLowerCase())
                    .getSingleResult();
            return total > 0;
        } finally {
            session.close();
        }
    }

    public void guardar(Aula aula) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(aula);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Aula> listar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "select a from Aula a join fetch a.planta join fetch a.tipoAula join fetch a.estado order by a.id desc",
                    Aula.class).getResultList();
        } finally {
            session.close();
        }
    }
}
