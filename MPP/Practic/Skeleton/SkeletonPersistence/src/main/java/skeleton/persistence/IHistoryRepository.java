package skeleton.persistence;

import org.hibernate.SessionFactory;
import skeleton.model.Clasament;
import skeleton.model.History;

public interface IHistoryRepository extends IRepository<Integer, History>{
    void setSessionFactory(SessionFactory sessionFactory);
    Iterable<History> findAllJocJucator(int idJoc, String idJucator);
    Iterable<History> findAllJocRunda(int idJoc, int idRunda);
//    Iterable<History> findClasament(int idJoc);
    Iterable<Clasament> findClasament(int idJoc);
    int findMaxGameId();
}
