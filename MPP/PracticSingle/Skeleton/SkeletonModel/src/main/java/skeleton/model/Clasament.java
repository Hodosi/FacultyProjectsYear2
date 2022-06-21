package skeleton.model;

import java.io.Serializable;

public class Clasament implements Serializable, Comparable {
    private int id;
    private String username;
    private int puncte;
    private int idGame;

    public Clasament(String username, int puncte) {
        this.username = username;
        this.puncte = puncte;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getPuncte() {
        return puncte;
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

    public void setPuncte(int puncte) {
        this.puncte = puncte;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    @Override
    public int compareTo(Object o) {
        return -1 * (this.getPuncte() - ((Clasament) o).getPuncte());
    }
}
