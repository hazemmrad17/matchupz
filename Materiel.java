package models.logistics;

public class Materiel {
    private int id_materiel;
    private int id_fournisseur;
    private String nom;
    private TypeMateriel type;
    private int quantite;
    private EtatMateriel etat;
    private float prix_unitaire;
    private transient String nomFournisseur;
    private String barcode;
    private transient boolean repairRequested;
    private String imagePath;

    // Constructors
    public Materiel() {}

    public Materiel(int id_fournisseur, String nom, TypeMateriel type, int quantite, EtatMateriel etat, float prix_unitaire, String barcode) {
        this.id_fournisseur = id_fournisseur;
        this.nom = nom;
        this.type = type;
        this.quantite = quantite;
        this.etat = etat;
        this.prix_unitaire = prix_unitaire;
        this.barcode = barcode;
    }

    public Materiel(int id_materiel, int id_fournisseur, String nom, TypeMateriel type, int quantite, EtatMateriel etat, float prix_unitaire, String barcode, String imagePath) {
        this.id_materiel = id_materiel;
        this.id_fournisseur = id_fournisseur;
        this.nom = nom;
        this.type = type;
        this.quantite = quantite;
        this.etat = etat;
        this.prix_unitaire = prix_unitaire;
        this.barcode = barcode;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getId_materiel() { return id_materiel; }
    public void setId_materiel(int id_materiel) { this.id_materiel = id_materiel; }
    public int getId_fournisseur() { return id_fournisseur; }
    public void setId_fournisseur(int id_fournisseur) { this.id_fournisseur = id_fournisseur; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public TypeMateriel getType() { return type; }
    public void setType(TypeMateriel type) { this.type = type; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public EtatMateriel getEtat() { return etat; }
    public void setEtat(EtatMateriel etat) { this.etat = etat; }
    public float getPrix_unitaire() { return prix_unitaire; }
    public void setPrix_unitaire(float prix_unitaire) { this.prix_unitaire = prix_unitaire; }
    public String getNomFournisseur() { return nomFournisseur; }
    public void setNomFournisseur(String nomFournisseur) { this.nomFournisseur = nomFournisseur; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public boolean isRepairRequested() { return repairRequested; }
    public void setRepairRequested(boolean repairRequested) { this.repairRequested = repairRequested; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return "Materiel{id_materiel=" + id_materiel + ", id_fournisseur=" + id_fournisseur +
                ", nom='" + nom + "', type=" + type + ", quantite=" + quantite +
                ", etat=" + etat + ", prix_unitaire=" + prix_unitaire +
                ", barcode='" + barcode + "', nomFournisseur='" + nomFournisseur +
                "', imagePath='" + imagePath + "'}";
    }
}