import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bibliothecaire{

    public void ajouterLivre(){

    }
    public void ajouterLivre(Livre livre) {
        Connection connection = ConnectionDB.getConnection();

        if (connection != null) {
            String query = "insert into livres (isbn, title, auteur, quantite) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, livre.getIsbn());
                preparedStatement.setString(2, livre.getTitle());
                preparedStatement.setString(3, livre.getAuteur());
                preparedStatement.setInt(4, livre.getQuantite());

                preparedStatement.executeUpdate();
                System.out.println("Le livre a bien été ajouté !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'insertion !");
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Erreur lors de la deconnection !");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Erreur lors de la connection à la BD !");
        }
    }

    public List<Livre> afficherLivres(){
        List<Livre> livres = new ArrayList<>();

        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM livres";
        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                int id        = rs.getInt("id");
                String isbn   = rs.getString("isbn");
                String title  = rs.getString("title");
                String auteur = rs.getString("auteur");
                int quantite  = rs.getInt("quantite");

                Livre livre = new Livre(id, isbn, title, auteur, quantite);
                livres.add(livre);
            }
        }catch (Exception e){
            System.out.println("La connection à la base de données est echouée !");
        }
        return livres;
    }

    public void suppTousLivres(String isbn) {
        PreparedStatement ps;
        String query = "DELETE FROM livres WHERE isbn = ?";
        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            ps.setString(1, isbn);
            int nb_livre = ps.executeUpdate();

            if (nb_livre > 0) {
                System.out.println(nb_livre + " livre(s) a bien supprimé");
            } else {
                System.out.println("Aucun livre trouvé !");
            }
        } catch (Exception e) {
            System.out.println("La connexion à la base de données a échoué !");
        }
    }

    public void suppLivre(String isbn){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Entrez la quantité que vous souhaitez supprimer : (Entrer A pour supprimer tous)");
        String quantity = scanner.nextLine();

        if ("A".equalsIgnoreCase(quantity)) {
            suppTousLivres(isbn);
        }
        else {
            PreparedStatement ps;
            String query = "UPDATE livres set quantite = (quantite- ?) WHERE isbn = ?";
            try {
                ps = ConnectionDB.getConnection().prepareStatement(query);
                ps.setString(1, quantity);
                ps.setString(2, isbn);
                int nb_livre = ps.executeUpdate();

                if (nb_livre > 0) {
                    System.out.println(nb_livre + " livres ont supprimé avec succes");
                } else {
                    System.out.println("Aucun livre trouvé !");
                }
            } catch (Exception e) {
                System.out.println("La connexion à la base de données a échoué !");
            }
        }
    }
}
