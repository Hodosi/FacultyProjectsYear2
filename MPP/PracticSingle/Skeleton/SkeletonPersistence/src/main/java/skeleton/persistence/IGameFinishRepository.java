package skeleton.persistence;

import org.hibernate.SessionFactory;
import skeleton.model.Clasament;
import skeleton.model.GameFinish;

public interface IGameFinishRepository extends IRepository<Integer, GameFinish>{
    void setSessionFactory(SessionFactory sessionFactory);
    Iterable<GameFinish> findAllPlayer(String idPlayer);
    Iterable<Clasament> findClasament();
}
