package skeleton.persistence.repository.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import skeleton.model.Clasament;
import skeleton.model.GameFinish;
import skeleton.persistence.IGameFinishRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class GameFinishDbRepository implements IGameFinishRepository {
    private SessionFactory sessionFactory;
    private  JdbcUtils dbUtils;

    public GameFinishDbRepository(Properties properties, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        dbUtils = new JdbcUtils(properties);
    }

    public GameFinishDbRepository(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(GameFinishDbRepository.class.getResourceAsStream("/skeletonserver.properties"));
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
    public void save(GameFinish entity) {
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
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, GameFinish entity) {

    }

    @Override
    public GameFinish findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<GameFinish> findAll() {
        return null;
    }

    @Override
    public Iterable<GameFinish> findAllPlayer(String idPlayer) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

//                Iterable<History> histories = session.createQuery("from History", History.class).list();
                Iterable<GameFinish> gameFinishList = session.createQuery("from GameFinish where id_player =:idPlayer", GameFinish.class)
                        .setParameter("idPlayer", idPlayer)
                        .list();

                tx.commit();

                return gameFinishList;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find game finish for player: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

    @Override
    public Iterable<Clasament> findClasament() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

//                Iterable<History> histories = session.createQuery("from History", History.class).list();
                Iterable<GameFinish> gameFinishList = session.createQuery("from GameFinish order by(punctajTotal) desc", GameFinish.class)
                        .list();

                tx.commit();

                List<Clasament> clasaments = new ArrayList<>();

                for(GameFinish gameFinish : gameFinishList){
                    Clasament clasament = new Clasament(gameFinish.getIdPlayer(), gameFinish.getPunctajTotal());
                    clasament.setId(gameFinish.getId());
                    clasament.setIdGame(gameFinish.getIdGame());

                    clasaments.add(clasament);
                }

                return clasaments;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find game finish for player: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

}
