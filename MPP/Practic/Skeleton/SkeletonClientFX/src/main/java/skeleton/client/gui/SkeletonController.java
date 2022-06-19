package skeleton.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import skeleton.model.Move;
import skeleton.model.User;
import skeleton.services.ISkeletonObserver;
import skeleton.services.ISkeletonServices;
import skeleton.services.SkeletonException;

import java.net.URL;
import java.util.ResourceBundle;

public class SkeletonController implements Initializable, ISkeletonObserver {
    private ISkeletonServices server;
    private User crtUser;

    public void setServer(ISkeletonServices s){
        this.server = s;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    ObservableList<Move> moveObservableList = FXCollections.observableArrayList();

    // anchor panes for pages
    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private AnchorPane mainAnchorPane;


    // login page
    @FXML
    private TextField textFieldUsernameLogin;

    @FXML
    private PasswordField passwordFieldLogin;

    // main page
    @FXML
    private TextField textFieldStartGameData;

    @FXML
    private Button buttonStartGame;

    @FXML
    private TableView<Move> tableViewMove;

    @FXML
    private TableColumn<Move, String> tableColumnUsername;

    @FXML
    private TableColumn<Move, String> tableColumnCurrentState;

    @FXML
    private TableColumn<Move, Integer> tableColumnPunctaj;

    private void initViewMove(){
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnCurrentState.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        tableColumnPunctaj.setCellValueFactory(new PropertyValueFactory<>("punctaj"));
        tableViewMove.setItems(moveObservableList);

    }

    @FXML
    void loginAction(){
        String username = textFieldUsernameLogin.getText().trim();
        String password = passwordFieldLogin.getText().trim();
        crtUser = new User(username, password);

        try{
            server.login(crtUser, this);
            System.out.println("Start init main page");
            initMainPage();
            this.crtUser = server.findUserByUsername(username);

        } catch (SkeletonException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Skeleton");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }

    @FXML
    void logoutAction()  {
        try{
            server.logout(crtUser, this);
        }
        catch (SkeletonException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Skeleton");
            alert.setHeaderText("Logout failure");
            alert.setContentText("Logout exception");
            alert.showAndWait();
        }
        loginAnchorPane.setVisible(true);
        mainAnchorPane.setVisible(false);
    }

    private void initMainPage(){
        loginAnchorPane.setVisible(false);
        mainAnchorPane.setVisible(true);
        initViewMove();
    }

    @FXML
    void startGame() {
        buttonStartGame.setDisable(true);
        //tring startGameData = textFieldStartGameData.getText().trim();
        String startGameData = null;
        try{
            server.startGame(crtUser, startGameData);
        } catch (SkeletonException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Skeleton");
            alert.setHeaderText("Start Game failure");
            alert.setContentText("ERROR");
            alert.showAndWait();
        }
    }

    @Override
    public void newMove() throws SkeletonException {
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game started");
            alert.setHeaderText("start");
            alert.setContentText("go go go");
            alert.showAndWait();

            try {
                moveObservableList.clear();
                moveObservableList.setAll(server.findCurrentMoves());
            }catch (SkeletonException exception){
                Alert alertException = new Alert(Alert.AlertType.INFORMATION);
                alertException.setTitle("Skeleton");
                alertException.setHeaderText("Move exception");
                alertException.setContentText(exception.getMessage());
                alertException.showAndWait();
            }
        });
    }
}
