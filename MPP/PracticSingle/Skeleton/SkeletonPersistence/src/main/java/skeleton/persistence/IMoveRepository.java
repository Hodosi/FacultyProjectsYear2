package skeleton.persistence;

import org.hibernate.SessionFactory;
import skeleton.model.Move;
import skeleton.model.User;

public interface IMoveRepository extends IRepository<Integer, Move>{
    void setSessionFactory(SessionFactory sessionFactory);
    Move findMoveByUsername(String username);
    Iterable<Move >findAllPlayer(String username);
    void deleteForUser(String username);
}
