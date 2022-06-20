package skeleton.persistence;

import org.hibernate.SessionFactory;
import skeleton.model.GameData;

public interface IGameDataRepository extends IRepository<Integer, GameData> {
    void setSessionFactory(SessionFactory sessionFactory);
    Iterable<GameData> findAllJoc(int idJoc);
    GameData add(GameData entity);
}
