package skeleton.persistence.repository.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.stereotype.Component;
import skeleton.model.Move;
import skeleton.persistence.IMoveRepository;

import java.io.IOException;
import java.util.Properties;

@Component
public class MoveDbRepository implements IMoveRepository {
    private SessionFactory sessionFactory;
    private  JdbcUtils dbUtils;

    public MoveDbRepository(Properties properties, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        dbUtils = new JdbcUtils(properties);
    }

    public MoveDbRepository(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(MoveDbRepository.class.getResourceAsStream("/skeletonserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find skeletonserver.properties "+e);
            return;
        }

        dbUtils = new JdbcUtils(serverProps);
    }

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Move entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Move entity) {

    }

    @Override
    public Move findOne(Integer integer) {
        return null;
    }

//    @Override
//    public Iterable<Move> findAll() {
//        return null;
//    }

    @Override
    public Iterable<Move> findAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Iterable<Move> moves = session.createQuery("from Move", Move.class).list();

                tx.commit();

                return moves;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Moves: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }
}
