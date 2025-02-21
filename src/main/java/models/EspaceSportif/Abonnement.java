package models.EspaceSportif;

import java.util.Date;

public class Abonnement {
    private int idAbonnement;
    private int idSport;
    private String typeAbonnement; // Mensuel, Trimestriel, Annuel
    private Date dateDebut;
    private Date dateFin;
    private double tarif;
    private String etat; // Actif, Expiré, Suspendu

    public Abonnement() {}

    public Abonnement(int idSport, String typeAbonnement, Date dateDebut, Date dateFin, double tarif, String etat) {
        this.idSport = idSport;
        this.typeAbonnement = typeAbonnement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tarif = tarif;
        this.etat = etat;
    }

    // Getters & Setters
    public int getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(int idAbonnement) { this.idAbonnement = idAbonnement; }

    public int getIdSport() { return idSport; }
    public void setIdSport(int idSport) { this.idSport = idSport; }

    public String getTypeAbonnement() { return typeAbonnement; }
    public void setTypeAbonnement(String typeAbonnement) { this.typeAbonnement = typeAbonnement; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}
