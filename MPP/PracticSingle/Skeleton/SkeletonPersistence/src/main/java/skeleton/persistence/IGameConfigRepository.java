package skeleton.persistence;

import org.hibernate.SessionFactory;
import skeleton.model.GameConfig;

public interface IGameConfigRepository extends IRepository<Integer, GameConfig> {
    void setSessionFactory(SessionFactory sessionFactory);
    GameConfig add(GameConfig entity);
}
