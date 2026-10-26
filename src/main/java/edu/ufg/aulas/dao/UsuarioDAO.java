package edu.ufg.aulas.dao;

import edu.ufg.aulas.entidades.Usuario;
import edu.ufg.aulas.util.HibernateUtil;
import edu.ufg.aulas.util.Seguridad;
import org.hibernate.Session;

public class UsuarioDAO {

    public Usuario autenticar(String correo, String clavePlana) {
        String hash = Seguridad.hash(clavePlana);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "from Usuario u where lower(u.correo) = :correo and u.clave = :clave", Usuario.class)
                    .setParameter("correo", correo.trim().toLowerCase())
                    .setParameter("clave", hash)
                    .uniqueResultOptional()
                    .orElse(null);
        } finally {
            session.close();
        }
    }
}
