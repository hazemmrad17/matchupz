package models.sponsor;

public class Sponsor {
    private int id_sponsor;
    private String nom;
    private String Contact;
    private String Pack;
    private String sponsor_picture;

    public Sponsor() {}
    public Sponsor(int id_sponsor, String nom, String Contact, String Pack) {
        this.id_sponsor = id_sponsor;
        this.nom = nom;
        this.Contact = Contact;
        this.Pack = Pack;
    }

    public Sponsor(String nom, String Contact, String Pack, String sponsor_picture) {
        this.nom = nom;
        this.Contact = Contact;
        this.Pack = Pack;
        this.sponsor_picture = sponsor_picture;
    }

    public int getId_sponsor() {
        return id_sponsor;
    }

    public String getContact() {
        return Contact;
    }

    public String getNom() {
        return nom;
    }

    public String getPack() {
        return Pack;
    }

    public void setId_sponsor(int id_sponsor) {
        this.id_sponsor = id_sponsor;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setPack(String pack) {
        Pack = pack;
    }

    public String getSponsor_picture() {
        return sponsor_picture;
    }

    public void setSponsor_picture(String sponsor_picture) {
        this.sponsor_picture = sponsor_picture;
    }

    @Override
    public String toString() {
        return "Sponsor{" +
                " nom='" + nom + '\'' +
                ", Contact='" + Contact + '\'' +
                ", Pack='" + Pack + '\'' +
                ", sponsor_picture='" + sponsor_picture + '\'' +
                '}';
    }
}