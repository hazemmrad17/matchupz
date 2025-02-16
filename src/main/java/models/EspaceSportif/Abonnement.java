package models.EspaceSportif;

import java.util.Date;

public class Abonnement {
    private int idAbonnement;
    private int id;  // Clé étrangère vers la table personne
    private String typeAbonnement; // Mensuel, Trimestriel, Annuel
    private Date dateDebut;
    private Date dateFin;
    private double tarif;
    private String status; // Actif, Expiré, Suspendu

    public Abonnement() {}

    public Abonnement(int id, String typeAbonnement, Date dateDebut, Date dateFin, double tarif, String status) {
        this.id = id;
        this.typeAbonnement = typeAbonnement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tarif = tarif;
        this.status = status;
    }

    // Getters & Setters
    public int getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(int idAbonnement) { this.idAbonnement = idAbonnement; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTypeAbonnement() { return typeAbonnement; }
    public void setTypeAbonnement(String typeAbonnement) { this.typeAbonnement = typeAbonnement; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
