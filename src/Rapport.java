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
                "(SELECT COUNT(*) FROM emprunts WHERE date_retour_prevue < CURRENT_DATE AND date_retour_reelle IS NULL) as livres_perdus ," +
                "(SELECT COUNT(*) FROM emprunts WHERE date_retour_prevue < CURRENT_DATE AND date_retour_prevue < date_retour_reelle) as livres_en_retard";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                statistiques.put("Total de livres", (rs.getInt("livres_disponibles") + rs.getInt("livres_empruntes")));
                statistiques.put("Livres disponibles", rs.getInt("livres_disponibles"));
                statistiques.put("Livres empruntés", rs.getInt("livres_empruntes"));
                statistiques.put("Livres perdus", rs.getInt("livres_perdus"));
                statistiques.put("Livres retournés en retard", rs.getInt("livres_en_retard"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des statistiques des livres !");
            e.printStackTrace();
        }
        return statistiques;
    }


    public void genererRapport() {
        Bibliothecaire bibliothecaire = new Bibliothecaire();
        List<Livre> livres            = bibliothecaire.afficherLivres();
        Map<String, Integer> nbLivres = statistiquesLivres();
        List<Map<String, Object>> emprunteursList = bibliothecaire.AfficherEmpunteurs();

        try {
            String desktopPath = System.getProperty("user.home") + "/Desktop";

            BufferedWriter fichier = new BufferedWriter(new FileWriter(desktopPath+"/rapport_bibliotheque.txt"));

            LocalDate date = LocalDate.now();

            fichier.write("Date : "+date+"\n");

            fichier.write("\n## NOMBRE DES LIVRES :\n---------------------\n");
            fichier.write("Total de livres     : "+nbLivres.get("Total de livres")+"\n" +
                              "Livres disponibles  : "+nbLivres.get("Livres disponibles")+"\n" +
                              "Livres empruntés    : "+nbLivres.get("Livres empruntés")+"\n" +
                              "Livres perdus       : "+nbLivres.get("Livres perdus")+"\n" +
                              "Retournés en retard : "+nbLivres.get("Livres retournés en retard")+"\n");


            fichier.write("\n## LISTE DES LIVRES :\n---------------------\n");
            fichier.write(String.format("%-15s | %-30s | %-30s | %-12s | %-12s | %-15s%n", "ISBN", "Titre", "Auteur", "Disponibles", "Empruntés", "Quantité totale"));
            fichier.write("------------------------------------------------------------------------------------------------------------------------------------\n");

            for (Livre lv : livres) {
                fichier.write(String.format("%-15s | %-30s | %-30s | %-12d | %-12d | %-15d%n", lv.getIsbn(), lv.getTitle(), lv.getAuteur(), lv.getQuantite(), lv.getEmprunte(), lv.getQuantite() + lv.getEmprunte()));
            }

            fichier.write("\n\n## LISTE DES EMPRUNTEURS :\n-------------------------");

            fichier.write(String.format("\n%-15s | %-15s | %-30s | %-15s | %-20s | %-15s%n",
                    "Numéro Membre", "Nom", "Titre", "ISBN", "Date Emprunt", "Date Retour Prévue"));
            fichier.write("---------------------------------------------------------------------------------------------------------------------------------");

            for (Map<String, Object> emprunteur : emprunteursList) {
                String numMembre    = emprunteur.get("numero_membre").toString();
                String name         = emprunteur.get("name").toString();
                String title        = emprunteur.get("title").toString();
                String isbnlivr     = emprunteur.get("isbn").toString();
                String dateEmprunt  = emprunteur.get("date_emprunt").toString();
                String dateRetourPrevue = emprunteur.get("date_retour_prevue").toString();

                fichier.write(String.format("\n%-15s | %-15s | %-30s | %-15s | %-20s | %-15s%n", numMembre, name, title, isbnlivr, dateEmprunt, dateRetourPrevue));
            }
            fichier.write("\n---------------------------------------------------------------------------------------------------------------------------------");
            fichier.flush(); // Vide le tampon et force l'écriture dans le fichier

            System.out.println("Rapport généré avec succès.");
        }
        catch (IOException e) {
            System.err.println("Erreur lors de la création du rapport : " + e.getMessage());
        }
    }


}
