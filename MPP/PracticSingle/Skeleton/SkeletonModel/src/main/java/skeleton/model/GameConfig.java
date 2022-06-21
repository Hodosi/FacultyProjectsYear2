package skeleton.model;

import java.io.Serializable;

public class GameConfig implements Serializable {
    private int id;
    private String pozitie;
    private String valoare;

    public GameConfig(){

    }

    public GameConfig(String pozitie, String valoare) {
        this.pozitie = pozitie;
        this.valoare = valoare;
    }

    public int getId() {
        return id;
    }

    public String getPozitie() {
        return pozitie;
    }

    public String getValoare() {
        return valoare;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPozitie(String pozitie) {
        this.pozitie = pozitie;
    }

    public void setValoare(String valoare) {
        this.valoare = valoare;
    }
}
