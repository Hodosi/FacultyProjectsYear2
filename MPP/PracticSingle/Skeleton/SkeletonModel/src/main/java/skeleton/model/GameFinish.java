package skeleton.model;

import java.io.Serializable;

public class GameFinish implements Serializable {
    int id;
    String idPlayer;
    int idGame;
    int punctajTotal;
    String info;

    public GameFinish() {
    }

    public GameFinish(String idPlayer, int idGame, int punctajTotal) {
        this.idPlayer = idPlayer;
        this.idGame = idGame;
        this.punctajTotal = punctajTotal;
    }

    public int getId() {
        return id;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public int getIdGame() {
        return idGame;
    }

    public int getPunctajTotal() {
        return punctajTotal;
    }

    public String getInfo() {
        return info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public void setPunctajTotal(int punctajTotal) {
        this.punctajTotal = punctajTotal;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
