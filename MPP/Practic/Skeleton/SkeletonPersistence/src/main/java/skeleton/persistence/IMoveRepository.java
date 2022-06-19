package skeleton.persistence;

import org.hibernate.SessionFactory;
import skeleton.model.Move;

public interface IMoveRepository extends IRepository<Integer, Move>{
    void setSessionFactory(SessionFactory sessionFactory);
}
