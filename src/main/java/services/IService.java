package services;

import models.utilisateur.User;

import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    void ajouter(T t) throws SQLException;
    void supprimer(T t);
    void modifier(T t);
    List<T> recherche();
   // public User verifierUtilisateur(String email, String password);



}
