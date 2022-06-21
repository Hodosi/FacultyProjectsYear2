package skeleton.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import skeleton.model.Clasament;
import skeleton.model.GameData;
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
    ObservableList<GameData> gameDataObservableList = FXCollections.observableArrayList();
    ObservableList<Clasament> clasamentsObservableList = FXCollections.observableArrayList();

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
    private TextField textFieldGameData;

    @FXML
    private Button buttonStartGame;

    @FXML
    private Button buttonMove;

    @FXML
    private TableView<Move> tableViewMove;

    @FXML
    private TableColumn<Move, String> tableColumnUsername;

    @FXML
    private TableColumn<Move, String> tableColumnCurrentState;

    @FXML
    private TableColumn<Move, Integer> tableColumnPunctaj;

    @FXML
    private TableView<GameData> tableViewGameData;

    @FXML
    private TableColumn<GameData, String> tableColumnUsernameGameData;

    @FXML
    private TableColumn<GameData, String> tableColumnData;

    @FXML
    private TableColumn<GameData, Integer> tableColumnIdGame;

    @FXML
    private TableView<Clasament> tableViewClasament;

    @FXML
    private TableColumn<Clasament, String> tableColumnUsernameClasament;

    @FXML
    private TableColumn<Clasament, Integer> tableColumnPuncteClasament;

    @FXML
    private TableColumn<Clasament, Integer> tableColumnIdGameClasament;



    private void initViewMove(){
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnCurrentState.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        tableColumnPunctaj.setCellValueFactory(new PropertyValueFactory<>("punctaj"));
        tableViewMove.setItems(moveObservableList);
    }

    private void initViewGameData(){
        tableColumnUsernameGameData.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnIdGame.setCellValueFactory(new PropertyValueFactory<>("idGame"));
        tableViewGameData.setItems(gameDataObservableList);
    }

    private void initViewClasament(){
        tableColumnUsernameClasament.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnPuncteClasament.setCellValueFactory(new PropertyValueFactory<>("puncte"));
        tableColumnIdGameClasament.setCellValueFactory(new PropertyValueFactory<>("idGame"));
        tableViewClasament.setItems(clasamentsObservableList);
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
        buttonMove.setDisable(true);
        initViewMove();
        initViewGameData();
        initViewClasament();
    }

    int nrMoves = 0;
    @FXML
    void startGameAction() {
        String startGameData = textFieldGameData.getText().trim();
        //String startGameData = null;
        try{
            if(startGameData.equals(crtUser.getUsername())){
                buttonStartGame.setDisable(true);
                buttonMove.setDisable(false);
                nrMoves = 0;
                server.start(crtUser.getUsername(), startGameData);
            }
        } catch (SkeletonException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Skeleton");
            alert.setHeaderText("Start Game failure");
            alert.setContentText("ERROR");
            alert.showAndWait();
        }
    }

    @FXML
    void move(){
        String gameData = textFieldGameData.getText().trim();
        try{
            server.move(crtUser.getUsername(), gameData);
            nrMoves++;
            if(nrMoves == 3){
                buttonStartGame.setDisable(false);
                buttonMove.setDisable(true);
            }
        } catch (SkeletonException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Skeleton");
            alert.setHeaderText("Game move failure");
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void startGame() throws SkeletonException {
        Platform.runLater(()->{
            try {
                gameDataObservableList.clear();
                gameDataObservableList.setAll(server.findAllGameData());
            }catch (SkeletonException exception){
                Alert alertException = new Alert(Alert.AlertType.INFORMATION);
                alertException.setTitle("Skeleton");
                alertException.setHeaderText("Move exception");
                alertException.setContentText(exception.getMessage());
                alertException.showAndWait();
            }
        });
    }

    @Override
    public void newMove() throws SkeletonException {
        Platform.runLater(()->{
            try {
                moveObservableList.clear();
//                moveObservableList.setAll(server.findCurrentMoves());
                moveObservableList.setAll(server.findAllMovesPlayer(crtUser.getUsername()));
            }catch (SkeletonException exception){
                Alert alertException = new Alert(Alert.AlertType.INFORMATION);
                alertException.setTitle("Skeleton");
                alertException.setHeaderText("Move exception");
                alertException.setContentText(exception.getMessage());
                alertException.showAndWait();
            }
        });
    }

    @Override
    public void finishGame() throws SkeletonException {
        Platform.runLater(()->{
            try {
                clasamentsObservableList.clear();
                clasamentsObservableList.setAll(server.findClasament());
            }catch (SkeletonException exception){
                Alert alertException = new Alert(Alert.AlertType.INFORMATION);
                alertException.setTitle("Skeleton");
                alertException.setHeaderText("Finish exception");
                alertException.setContentText(exception.getMessage());
                alertException.showAndWait();
            }
        });
    }
}
