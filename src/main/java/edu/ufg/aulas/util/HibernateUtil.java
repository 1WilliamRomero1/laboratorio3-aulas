package edu.ufg.aulas.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration cfg = new Configuration().configure();
            sobrescribir(cfg, "hibernate.connection.url", System.getenv("AULAS_DB_URL"));
            sobrescribir(cfg, "hibernate.connection.username", System.getenv("AULAS_DB_USER"));
            sobrescribir(cfg, "hibernate.connection.password", System.getenv("AULAS_DB_PASS"));
            sessionFactory = cfg.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    private static void sobrescribir(Configuration cfg, String propiedad, String valor) {
        if (valor != null && !valor.isBlank()) {
            cfg.setProperty(propiedad, valor);
        }
    }
}
