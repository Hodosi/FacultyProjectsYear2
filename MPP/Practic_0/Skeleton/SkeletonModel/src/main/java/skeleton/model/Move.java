package skeleton.model;

import java.io.Serializable;

public class Move implements Serializable {
    private int id;
    private String username;
    private String currentState;
    private int punctaj;

    public Move(){

    }

    public Move(String username, String currentState, int punctaj) {
        this.username = username;
        this.currentState = currentState;
        this.punctaj = punctaj;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCurrentState() {
        return currentState;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }
}
