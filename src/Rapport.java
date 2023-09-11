import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Rapport {

    public Map<String, Integer> statistiquesLivres() {
        Map<String, Integer> statistiques = new HashMap<>();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT" +
                "(SELECT SUM(l.quantite) FROM livres l) as livres_disponibles," +
                "(SELECT COUNT(e.id) FROM emprunts e WHERE e.status LIKE 'emprunte' AND e.date_retour_prevue > CURRENT_DATE AND e.date_retour_reelle IS NULL) as livres_empruntes," + // Note the added comma
                "(SELECT COUNT(*) FROM emprunts WHERE date_retour_prevue < CURRENT_DATE AND date_retour_reelle IS NULL) as livres_perdus;";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                statistiques.put("Total de livres", (rs.getInt("livres_disponibles") + rs.getInt("livres_empruntes")));
                statistiques.put("Livres disponibles", rs.getInt("livres_disponibles"));
                statistiques.put("Livres empruntés", rs.getInt("livres_empruntes"));
                statistiques.put("Livres perdus", rs.getInt("livres_perdus"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des statistiques des livres !");
            e.printStackTrace();
        }
        return statistiques;
    }


}
