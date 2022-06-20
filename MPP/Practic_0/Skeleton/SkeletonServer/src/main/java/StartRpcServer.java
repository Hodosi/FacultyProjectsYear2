import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import skeleton.network.utils.AbstractServer;
import skeleton.network.utils.ServerException;
import skeleton.network.utils.SkeletonRpcConcurrentServer;
import skeleton.persistence.IHistoryRepository;
import skeleton.persistence.IMoveRepository;
import skeleton.persistence.IUserRepository;
import skeleton.persistence.repository.jdbc.HistoryDbRepository;
import skeleton.persistence.repository.jdbc.MoveDbRepository;
import skeleton.persistence.repository.jdbc.UserDbRepository;
import skeleton.server.SkeletonServicesImplementation;
import skeleton.services.ISkeletonServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    private static int defaultPort=55556;
    public static void main(String[] args) {
        initialize();

        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/skeletonserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find skeletonserver.properties "+e);
            return;
        }
        IUserRepository userRepository = new UserDbRepository(serverProps);
        IMoveRepository moveRepository = new MoveDbRepository(serverProps, sessionFactory);
        IHistoryRepository historyRepository = new HistoryDbRepository(serverProps, sessionFactory);

        ISkeletonServices competitionServerImplementation = new SkeletonServicesImplementation(userRepository, moveRepository, historyRepository);


        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new SkeletonRpcConcurrentServer(chatServerPort, competitionServerImplementation);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
