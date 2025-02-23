package models;

import javafx.beans.property.*;
import java.util.Date;

public class HistoriqueClub {
    private IntegerProperty idHistorique;
    private IntegerProperty idJoueur;
    private StringProperty nomClub;
    private ObjectProperty<Date> saisonDebut;
    private ObjectProperty<Date> saisonFin;

    // Constructor
    public HistoriqueClub(int idHistorique, int idJoueur, String nomClub, Date saisonDebut, Date saisonFin) {
        this.idHistorique = new SimpleIntegerProperty(idHistorique);
        this.idJoueur = new SimpleIntegerProperty(idJoueur);
        this.nomClub = new SimpleStringProperty(nomClub);
        this.saisonDebut = new SimpleObjectProperty<>(saisonDebut);
        this.saisonFin = new SimpleObjectProperty<>(saisonFin);
    }

    public HistoriqueClub(int idJoueur, String nomClub, Date saisonDebut, Date saisonFin) {
        this.idJoueur = new SimpleIntegerProperty(idJoueur);
        this.nomClub = new SimpleStringProperty(nomClub);
        this.saisonDebut = new SimpleObjectProperty<>(saisonDebut);
        this.saisonFin = new SimpleObjectProperty<>(saisonFin);
    }


    // Default constructor
    public HistoriqueClub() {
        this.idHistorique = new SimpleIntegerProperty();
        this.idJoueur = new SimpleIntegerProperty();
        this.nomClub = new SimpleStringProperty();
        this.saisonDebut = new SimpleObjectProperty<>();
        this.saisonFin = new SimpleObjectProperty<>();
    }

    // Constructor for easier instantiation
    public HistoriqueClub(String nomClub, Date saisonDebut, Date saisonFin) {
        this.nomClub = new SimpleStringProperty(nomClub);
        this.saisonDebut = new SimpleObjectProperty<>(saisonDebut);
        this.saisonFin = new SimpleObjectProperty<>(saisonFin);
    }

    // Getters and setters (using JavaFX properties)
    public IntegerProperty idHistoriqueProperty() {
        return idHistorique;
    }

    public int getIdHistorique() {
        return idHistorique.get();
    }

    public void setIdHistorique(int idHistorique) {
        this.idHistorique.set(idHistorique);
    }

    public IntegerProperty idJoueurProperty() {
        return idJoueur;
    }

    public int getIdJoueur() {
        return idJoueur.get();
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur.set(idJoueur);
    }

    public StringProperty nomClubProperty() {
        return nomClub;
    }

    public String getNomClub() {
        return nomClub.get();
    }

    public void setNomClub(String nomClub) {
        this.nomClub.set(nomClub);
    }

    public ObjectProperty<Date> saisonDebutProperty() {
        return saisonDebut;
    }

    public Date getSaisonDebut() {
        return saisonDebut.get();
    }

    public void setSaisonDebut(Date saisonDebut) {
        this.saisonDebut.set(saisonDebut);
    }

    public ObjectProperty<Date> saisonFinProperty() {
        return saisonFin;
    }

    public Date getSaisonFin() {
        return saisonFin.get();
    }

    public void setSaisonFin(Date saisonFin) {
        this.saisonFin.set(saisonFin);
    }
}
