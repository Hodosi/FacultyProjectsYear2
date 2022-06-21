package skeleton.persistence.repository.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import skeleton.model.GameConfig;
import skeleton.model.GameData;
import skeleton.persistence.IGameConfigRepository;

import java.io.IOException;
import java.util.Properties;

@Component
public class GameConfigDbRepository implements IGameConfigRepository {
    private SessionFactory sessionFactory;
    private  JdbcUtils dbUtils;

    public GameConfigDbRepository(Properties properties, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        dbUtils = new JdbcUtils(properties);
    }

    public GameConfigDbRepository(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(GameConfigDbRepository.class.getResourceAsStream("/skeletonserver.properties"));
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
    public void save(GameConfig entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la save GameData: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public GameConfig add(GameConfig entity) {
        int id = -1;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                id = (int) session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare ORM "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        GameConfig gameConfig = new GameConfig(entity.getPozitie(), entity.getValoare());
        gameConfig.setId(id);
        return gameConfig;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, GameConfig entity) {

    }

    @Override
    public GameConfig findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<GameConfig> findAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Iterable<GameConfig> gameConfigList = session.createQuery("from GameConfig", GameConfig.class).list();

                tx.commit();

                return gameConfigList;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Moves: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }
}
