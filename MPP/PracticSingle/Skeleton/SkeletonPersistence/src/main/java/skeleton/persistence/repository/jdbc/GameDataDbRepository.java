package skeleton.persistence.repository.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import skeleton.model.GameData;
import skeleton.model.History;
import skeleton.model.Move;
import skeleton.persistence.IGameDataRepository;

import java.io.IOException;
import java.util.Properties;

@Component
public class GameDataDbRepository implements IGameDataRepository {

    private SessionFactory sessionFactory;
    private  JdbcUtils dbUtils;

    public GameDataDbRepository(Properties properties, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        dbUtils = new JdbcUtils(properties);
    }

    public GameDataDbRepository(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(GameDataDbRepository.class.getResourceAsStream("/skeletonserver.properties"));
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
    public void save(GameData entity) {
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
    public GameData add(GameData entity) {
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
        GameData gameData = new GameData(entity.getUsername(), entity.getData());
        gameData.setIdGame(entity.getIdGame());
        gameData.setId(id);
        return gameData;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, GameData entity) {

    }

    @Override
    public GameData findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<GameData> findAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Iterable<GameData> gameDataList = session.createQuery("from GameData", GameData.class).list();

                tx.commit();

                return gameDataList;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Moves: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

    @Override
    public Iterable<GameData> findAllJoc(int idJoc) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

//                Iterable<History> histories = session.createQuery("from History", History.class).list();
                Iterable<GameData> gameDataList = session.createQuery("from GameData where id_game =:idJoc", GameData.class)
                        .setParameter("idJoc", idJoc)
                        .list();

                tx.commit();

                return gameDataList;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Histories: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

}
