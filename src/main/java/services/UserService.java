package services;

import models.Role;
import models.User;
import utils.MyDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(User user) {


        String req = " INSERT INTO `user`(`nom`, `prenom`, `email`,`password`,`num_telephone`,`date_de_naissance`,`genre`,`role`,`image`) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getNum_telephone());
            ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
            ps.setString(7, user.getGenre());
            ps.setString(8, user.getRole().getValue());
            //ps.setString(8, user.getRole());
            ps.setString(9, user.getImage());

            ps.executeUpdate();
            System.out.println("Add  established successfully");

        } catch (Exception e) {
            e.getMessage();
        }


    }

    @Override
    public void supprimer(User user) {

        String req = " DELETE FROM `user` WHERE `id_user` = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);

            ps.setInt(1, user.getId_user());
            ps.executeUpdate();
            System.out.println("Delete established successfully");

        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void modifier(User user) {
        String req = "UPDATE `user` SET `nom`=?, `prenom`=?, `email`=?, `password`=?, `num_telephone`=?, `date_de_naissance`=?, `genre`=?, `role`=?, `image`=? WHERE id_user = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getNum_telephone());
            ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
            ps.setString(7, user.getGenre());
            ps.setString(8, user.getRole().getValue());
            ps.setString(9, user.getImage());
            ps.setInt(10, user.getId_user());

            System.out.println("Modification en cours pour ID: " + user.getId_user()); // 🔍 Vérification

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie !");
            } else {
                System.out.println("Aucune mise à jour effectuée !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*@Override
    public void modifier(User user) {

        String req ="UPDATE `user` SET `nom`=?,`prenom`=?,`email`=?,`password`=?,`num_telephone`=?,`date_de_naissance`=?,`genre`=?,`role`=?,`image`=? WHERE id_user =?";
        try
        {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getNum_telephone());
            ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
            ps.setString(7, user.getGenre());
            ps.setString(8, user.getRole().getValue());
            //ps.setString(8, user.getRole());
            ps.setString(9, user.getImage());
            ps.setInt(10, user.getId_user());
            ps.executeUpdate();
            System.out.println("Modifier ");
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée !");
            }


        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }*/

    @Override
    public List<User> recherche() {
        String req = "SELECT * FROM `user`";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId_user(rs.getInt("id_user"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setNum_telephone(rs.getInt("num_telephone"));
                u.setDate_de_naissance(rs.getDate("date_de_naissance").toLocalDate());
                u.setGenre(rs.getString("genre"));
                u.setRole(Role.fromString(rs.getString("role")));
                //u.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
                // u.setRole(rs.getString("role"));
                u.setImage(rs.getString("image"));
                users.add(u);

            }
            System.out.println(users);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }


@Override
    public User verifierUtilisateur(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("num_telephone"),
                        rs.getDate("date_de_naissance").toLocalDate(),
                        rs.getString("genre"),
                        models.Role.fromString(rs.getString("role")),
                        rs.getString("image")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // L'utilisateur n'existe pas
    }
}

