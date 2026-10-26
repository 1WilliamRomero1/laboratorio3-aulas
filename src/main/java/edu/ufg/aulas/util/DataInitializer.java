package edu.ufg.aulas.util;

import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.Planta;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.entidades.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;

public final class DataInitializer {

    public static final String ADMIN_CORREO = "admin@ufg.edu.sv";
    public static final String ADMIN_CLAVE = "admin123";

    private DataInitializer() {
    }

    public static void inicializar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            if (contar(session, "Planta") == 0) {
                for (int numero = 1; numero <= 5; numero++) {
                    session.persist(new Planta(numero));
                }
            }
            if (contar(session, "TipoAula") == 0) {
                String[] tipos = {"Normal", "Centro de cómputo", "Laboratorio", "Especial"};
                for (String nombre : tipos) {
                    session.persist(new TipoAula(nombre));
                }
            }
            if (contar(session, "EstadoAula") == 0) {
                String[] estados = {"Asignada", "Asignada temporalmente", "En mantenimiento",
                    "Disponible", "Fuera de servicio"};
                for (String nombre : estados) {
                    session.persist(new EstadoAula(nombre));
                }
            }
            if (contar(session, "Usuario") == 0) {
                session.persist(new Usuario("Administrador", ADMIN_CORREO, Seguridad.hash(ADMIN_CLAVE)));
            }

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

    private static long contar(Session session, String entidad) {
        return session.createQuery("select count(e) from " + entidad + " e", Long.class)
                .getSingleResult();
    }
}
