package skeleton.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import skeleton.client.gui.SkeletonController;
import skeleton.network.rpcprotocol.SkeletonServicesRpcProxy;
import skeleton.services.ISkeletonServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties clientProperties = new Properties();
        try{
            System.out.println("Try set Client properties. ");
            clientProperties.load(StartRpcClientFX.class.getResourceAsStream("/skeletonclient.properties"));
            System.out.println("Client properties set. ");
            clientProperties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find skeletonclient.properties " + e);
            return;
        }

        String serverIP = clientProperties.getProperty("skeleton.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProperties.getProperty("skeleton.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ISkeletonServices server = new SkeletonServicesRpcProxy(serverIP, serverPort);


        primaryStage.setTitle("Skeleton");
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("views/skeletonPage.fxml"));
        Parent pane=loader.load();

        SkeletonController competitionController = loader.getController();
        competitionController.setServer(server);

        loader.setController(competitionController);

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
