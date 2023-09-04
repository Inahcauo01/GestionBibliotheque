import java.sql.*;

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
                e.printStackTrace();
                System.out.println("Erreur lors de l'insertion !");
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Erreur lors de la connection à la BD !");
        }
    }

}
