package skeleton.persistence.repository.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import skeleton.model.History;
import skeleton.persistence.IHistoryRepository;

import java.io.IOException;
import java.util.Properties;

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
}
