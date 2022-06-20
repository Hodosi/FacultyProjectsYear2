package skeleton.persistence.repository.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.stereotype.Component;
import skeleton.model.Move;
import skeleton.model.User;
import skeleton.persistence.IMoveRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
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
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();

                session.update(entity);

                tx.commit();

            } catch(RuntimeException ex){
                System.err.println("Eroare la update Move "+ex);
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public Move findOne(Integer integer) {
        return null;
    }

    @Override
    public Move findMoveByUsername(String username) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Move move = session.createQuery("from Move where username = :username", Move.class)
                        .setParameter("username", username)
                        .setMaxResults(1)
                        .uniqueResult();

                tx.commit();

                return move;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find move by username: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

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
