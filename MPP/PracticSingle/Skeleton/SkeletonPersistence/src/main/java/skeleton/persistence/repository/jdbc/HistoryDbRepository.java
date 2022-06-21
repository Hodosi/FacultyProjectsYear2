package skeleton.persistence.repository.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import skeleton.model.Clasament;
import skeleton.model.History;
import skeleton.model.Move;
import skeleton.persistence.IHistoryRepository;

import java.io.IOException;
import java.util.*;

@Component
public class HistoryDbRepository implements IHistoryRepository {
    private SessionFactory sessionFactory;
    private JdbcUtils dbUtils;

    public HistoryDbRepository(Properties properties, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        dbUtils = new JdbcUtils(properties);
    }

    public HistoryDbRepository(){

        Properties serverProps=new Properties();
        try {
            serverProps.load(HistoryDbRepository.class.getResourceAsStream("/skeletonserver.properties"));
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
    public void save(History entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la save Task: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, History entity) {

    }

    @Override
    public History findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<History> findAll() {
        return null;
    }

    @Override
    public Iterable<History> findAllJocJucator(int idJoc, String idJucator) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

//                Iterable<History> histories = session.createQuery("from History", History.class).list();
                Iterable<History> histories = session.createQuery("from History where id_game =:idJoc and id_player = :idJucator", History.class)
                        .setParameter("idJoc", idJoc)
                        .setParameter("idJucator", idJucator)
                        .list();

                tx.commit();

                return histories;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Histories: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

    @Override
    public Iterable<History> findAllJocRunda(int idJoc, int idRunda) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

//                Iterable<History> histories = session.createQuery("from History", History.class).list();
                Iterable<History> histories = session.createQuery("from History where id_game =:idJoc and id_runda = :idRunda", History.class)
                        .setParameter("idJoc", idJoc)
                        .setParameter("idRunda", idRunda)
                        .list();

                tx.commit();

                return histories;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Histories: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

//    @Override
//    public Iterable<History> findClasament(int idJoc) {
//        try(Session session = sessionFactory.openSession()) {
//            Transaction tx = null;
//            try {
//                tx = session.beginTransaction();
//
//                Iterable<History> histories = session.createQuery("from History where id_game =:idJoc group by id_player order by sum(punctaj) desc", History.class)
//                        .setParameter("idJoc", idJoc)
//                        .list();
//
//                tx.commit();
//
//                return histories;
//
//            } catch (RuntimeException ex) {
//                System.err.println("Eroare la find All Histories: " + ex);
//                if (tx != null)
//                    tx.rollback();
//            }
//        }
//
//        return null;
//    }

    @Override
    public Iterable<Clasament> findClasament() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Iterable<History> histories = session.createQuery("from History", History.class)
                        .list();

                tx.commit();

                Set<String> idPlayers = new HashSet<>();

                for(History history : histories){
                    if(!idPlayers.contains(history.getIdPlayer())){
                        idPlayers.add(history.getIdPlayer());
                    }
                }

                List<Clasament> clasaments = new ArrayList<>();

                for(String username : idPlayers){
                    Clasament clasament = new Clasament(username, 0);
                    for (History history : histories){
                        if(history.getIdPlayer().equals(username)){
                            int pnct = clasament.getPuncte();
                            pnct += history.getPunctaj();
                            clasament.setPuncte(pnct);
                        }
                    }
                    clasaments.add(clasament);
                }

                Collections.sort(clasaments);

                int pozitie = 1;
                for(Clasament clasament : clasaments){
                    clasament.setId(pozitie);
                    pozitie++;
                }

                return clasaments;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Histories: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

    @Override
    public Iterable<Clasament> findClasament(int idJoc) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Iterable<History> histories = session.createQuery("from History where id_game =:idJoc", History.class)
                        .setParameter("idJoc", idJoc)
                        .list();

                tx.commit();

                Set<String> idPlayers = new HashSet<>();

                for(History history : histories){
                    if(!idPlayers.contains(history.getIdPlayer())){
                        idPlayers.add(history.getIdPlayer());
                    }
                }

                List<Clasament> clasaments = new ArrayList<>();

                for(String username : idPlayers){
                    Clasament clasament = new Clasament(username, 0);
                    for (History history : histories){
                        if(history.getIdPlayer().equals(username)){
                            int pnct = clasament.getPuncte();
                            pnct += history.getPunctaj();
                            clasament.setPuncte(pnct);
                        }
                    }
                    clasaments.add(clasament);
                }

                Collections.sort(clasaments);

                int pozitie = 1;
                for(Clasament clasament : clasaments){
                    clasament.setId(pozitie);
                    pozitie++;
                }

                return clasaments;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la find All Histories: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return null;
    }

    @Override
    public int findMaxGameId() {
        int maxGameId = -100;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                maxGameId = session.createQuery("select max(idGame) from History", Integer.class)
                        .setMaxResults(1)
                        .uniqueResult();

                tx.commit();

                return maxGameId;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la max game id: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return maxGameId;
    }

    @Override
    public int findMaxGameIdPlayer(String idJucator) {
        int maxGameId = -100;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                maxGameId = session.createQuery("select max(idGame) from History where idPlayer = :idJucator", Integer.class)
                        .setParameter("idJucator", idJucator)
                        .setMaxResults(1)
                        .uniqueResult();

                tx.commit();

                return maxGameId;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la max game id: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return maxGameId;
    }

    @Override
    public int findIdRundaGamePlayer(int idJoc, String idJucator) {
        int maxRundaId = -100;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                maxRundaId = session.createQuery("select max(idRunda) from History where idPlayer = :idJucator and idGame = :idJoc", Integer.class)
                        .setParameter("idJucator", idJucator)
                        .setParameter("idJoc", idJoc)
                        .setMaxResults(1)
                        .uniqueResult();

                tx.commit();

                return maxRundaId;

            } catch (RuntimeException ex) {
                System.err.println("Eroare la max game id: " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }

        return maxRundaId;
    }
}
