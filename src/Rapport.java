import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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


    public void genererRapport() {
        Bibliothecaire bibliothecaire = new Bibliothecaire();
        List<Livre> livres = bibliothecaire.afficherLivres();

        try {
            String desktopPath = System.getProperty("user.home") + "/Desktop";

            BufferedWriter writer = new BufferedWriter(new FileWriter(desktopPath+"/rapport_bibliotheque.txt"));

            LocalDate date = LocalDate.now();

            writer.write("Date : "+date+"\n");
            writer.write("\n## LISTE DES LIVRES :\n---------------------\n");
            writer.write(String.format("%-15s | %-30s | %-30s | %-12s | %-12s | %-15s%n", "ISBN", "Titre", "Auteur", "Disponibles", "Empruntés", "Quantité totale"));
            writer.write("------------------------------------------------------------------------------------------------------------------------------------\n");

            for (Livre lv : livres) {
                writer.write(String.format("%-15s | %-30s | %-30s | %-12d | %-12d | %-15d%n", lv.getIsbn(), lv.getTitle(), lv.getAuteur(), lv.getQuantite(), lv.getEmprunte(), lv.getQuantite() + lv.getEmprunte()));
                writer.flush(); // Vide le tampon et force l'écriture dans le fichier
            }

            System.out.println("Rapport généré avec succès.");
        }
        catch (IOException e) {
            System.err.println("Erreur lors de la création du rapport : " + e.getMessage());
        }
    }


}
