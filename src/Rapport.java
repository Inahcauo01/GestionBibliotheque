import java.sql.*;

public class Rapport {
    public int compterLivresDisponibles() {
        int livresDisponibles = 0;
        try (Connection connection = ConnectionDB.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT COUNT(*) AS livres_disponibles FROM livres WHERE quantite > 0;"
            );
            if (resultSet.next()) {
                livresDisponibles = resultSet.getInt("livres_disponibles");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livresDisponibles;
    }

    public int compterLivresEmpruntes() {
        int livresEmpruntes = 0;
        try (Connection connection = ConnectionDB.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT COUNT(*) AS livres_empruntes " +
                            "FROM emprunts " +
                            "WHERE status = 'en cours';"
            );
            if (resultSet.next()) {
                livresEmpruntes = resultSet.getInt("livres_empruntes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livresEmpruntes;
    }

    public int compterTotalLivres() {
        int totalLivres = 0;
        try (Connection connection = ConnectionDB.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT COUNT(*) AS total_livres FROM livres;"
            );
            if (resultSet.next()) {
                totalLivres = resultSet.getInt("total_livres");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalLivres;
    }

}
