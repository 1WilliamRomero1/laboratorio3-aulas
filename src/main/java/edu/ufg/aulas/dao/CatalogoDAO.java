package edu.ufg.aulas.dao;

import edu.ufg.aulas.entidades.EstadoAula;
import edu.ufg.aulas.entidades.Planta;
import edu.ufg.aulas.entidades.TipoAula;
import edu.ufg.aulas.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;

public class CatalogoDAO {

    public List<Planta> listarPlantas() {
        return listar("from Planta p order by p.numero", Planta.class);
    }

    public List<TipoAula> listarTiposAula() {
        return listar("from TipoAula t order by t.id", TipoAula.class);
    }

    public List<EstadoAula> listarEstados() {
        return listar("from EstadoAula e order by e.id", EstadoAula.class);
    }

    private <T> List<T> listar(String hql, Class<T> clase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(hql, clase).getResultList();
        } finally {
            session.close();
        }
    }
}
