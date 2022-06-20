package skeleton.model;

import java.io.Serializable;

public class GameData implements Serializable {
    private int id;
    private String username;
    private String data;
    private int idGame;


    public GameData() {

    }

    public GameData(String username, String data) {
        this.username = username;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getData() {
        return data;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }
}
