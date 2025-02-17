package tests;

import models.Contrat;
import models.Sponsor;
import models.Transaction;
import services.ContratService;
import services.SponsorService;
import services.TransactionService;

import java.time.LocalDate;

public class MainProg {

    public static void main(String[] args) {

        SponsorService ps = new SponsorService();
        //ps.ajouter(new Sponsor("Comar", "70150140", "Silver"));
        //ps.ajouter(new Sponsor("unike", "12345678", "bronze"));
        //ps.supprimer(new Sponsor(1,"Delice", "71222470", "Gold"));
        ps.modifier(new Sponsor(9 ,"esprit", "77120140", "bronze"));
        ps.rechercher();


        ContratService cs = new ContratService();
        //cs.ajouter(new Contrat(2,"contrat1", "2025/03/30","2025/05/27",50));
        cs.rechercher();

        TransactionService ts = new TransactionService();
        LocalDate youm= LocalDate.now();
        //ts.ajouter(new Transaction(1,"credit",4100, "2025/02/02","khlas esprit"));
        //ts.ajouter(new Transaction(1,"debit",8250, "2025/01/29","flousi y aalem"));
        //ts.modifier(new Transaction(1,2,"debit",8250, "2025/01/29","flousi y aalem"));
        //ts.supprimer(new Transaction(3,1,"debit",8250, "2025/01/29","flousi y aalem"));
        ts.rechercher();
    }

}
