import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bibliothecaire{

    public void ajouterLivre(){

    }
    public void ajouterLivre(Livre livre) {
        Connection connection = ConnectionDB.getConnection();

        if (connection != null) {
            String query = "insert into livres (id, isbn, title, auteur, quantite) VALUES (?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, livre.getId());
                preparedStatement.setString(2, livre.getIsbn());
                preparedStatement.setString(3, livre.getTitle());
                preparedStatement.setString(4, livre.getAuteur());
                preparedStatement.setInt(5, livre.getQuantite());

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

}
