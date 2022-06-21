package skeleton.model;

import java.io.Serializable;

public class History implements Serializable {
    private int id;
    private int idGame;
    private int idRunda;
    private String idPlayer;
    private String propunere;
    private int punctaj;

    public History(){

    }

    public History(int idGame, int idRunda, String idPlayer, String propunere, int punctaj) {
        this.idGame = idGame;
        this.idRunda = idRunda;
        this.idPlayer = idPlayer;
        this.propunere = propunere;
        this.punctaj = punctaj;
    }

    public int getId() {
        return id;
    }

    public int getIdGame() {
        return idGame;
    }

    public int getIdRunda() {
        return idRunda;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public String getPropunere() {
        return propunere;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public void setIdRunda(int idRunda) {
        this.idRunda = idRunda;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public void setPropunere(String propunere) {
        this.propunere = propunere;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }
}
