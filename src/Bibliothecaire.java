import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

public class Bibliothecaire{

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
        String query = "SELECT l.*, COUNT(e.isbn_livre) as empruntes " +
                "FROM livres l " +
                "LEFT JOIN emprunts e ON l.isbn = e.isbn_livre " +
                "GROUP BY l.isbn";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                int id        = rs.getInt("id");
                String isbn   = rs.getString("isbn");
                String title  = rs.getString("title");
                String auteur = rs.getString("auteur");
                int quantite  = rs.getInt("quantite");
                int empruntes = rs.getInt("empruntes");

                Livre livre = new Livre(isbn, title, auteur, quantite, empruntes);
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
            System.out.println("Erreur lors de la suppression !");
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
                    System.out.println(quantity + " livres ont supprimé avec succes");
                } else {
                    System.out.println("Aucun livre trouvé !");
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la suppression !");
            }
        }
    }

    public List<Livre> rechercherLivre(String mot){
        List<Livre> livres = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT l.*, COUNT(e.isbn_livre) as empruntes " +
                "FROM livres l " +
                "LEFT JOIN emprunts e ON l.isbn = e.isbn_livre " +
                "WHERE l.title LIKE ? OR l.auteur LIKE ?" +
                "GROUP BY l.isbn";
        //String query = "SELECT * FROM livres WHERE title LIKE ? OR auteur LIKE ?";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            ps.setString(1, "%"+ mot +"%");
            ps.setString(2, "%"+ mot +"%");

            rs = ps.executeQuery();
            while (rs.next()){
                int id        = rs.getInt("id");
                String isbn   = rs.getString("isbn");
                String title  = rs.getString("title");
                String auteur = rs.getString("auteur");
                int quantite  = rs.getInt("quantite");
                int empruntes = rs.getInt("empruntes");

                Livre l = new Livre(isbn, title, auteur, quantite, empruntes);
                livres.add(l);
            }

            if (livres.isEmpty()) {
                System.out.println("Aucun livre trouvé pour la recherche : " + mot);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche !");
            e.printStackTrace();
        }
        return  livres;
    }

    public Livre modifierLivre(String isbnRecherche){
        Livre livre = recupererLivreIsbn(isbnRecherche);
        afficherUnLivre(livre);
        Scanner scanner = new Scanner(System.in);
        int choix;

        do {
            System.out.println("Que souhaitez vous modifier ?");
            System.out.println("1. Titre");
            System.out.println("2. Auteur");
            System.out.println("3. Titre et Auteur");
            System.out.println("4. Quantité");
            System.out.println("0. Annuler");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.println("Entrez le nouveau titre : ");       String newTitle = scanner.nextLine();
                    livre.setTitle(newTitle);
                    modifierBD(livre);
                    System.out.println("Livre après la modification :");
                    afficherUnLivre(livre);
                    break;
                case 2:
                    System.out.println("Entrez le nouvel auteur : ");       String newAuthor = scanner.nextLine();
                    livre.setAuteur(newAuthor);
                    modifierBD(livre);
                    System.out.println("Livre après la modification :");
                    afficherUnLivre(livre);
                    break;
                case 3:
                    System.out.println("Entrez le nouveau titre : ");       newTitle = scanner.nextLine();
                    System.out.println("Entrez le nouvel auteur : ");       String newAuthor2 = scanner.nextLine();
                    livre.setTitle(newTitle);
                    livre.setAuteur(newAuthor2);
                    modifierBD(livre);
                    System.out.println("Livre après la modification :");
                    afficherUnLivre(livre);
                    break;
                case 4:
                    System.out.println("Entrez la nouvelle quantité : ");   int newQuantity = scanner.nextInt();
                    livre.setQuantite(newQuantity);
                    modifierBD(livre);
                    System.out.println("Livre après la modification :");
                    afficherUnLivre(livre);
                    break;
                case 0:
                    System.out.println("Modification annulée.");
                    break;
                default:
                    System.out.println("Choix invalide. Réessayez.");
            }
        } while (choix != 0);

        System.out.println("Livre après la modification :");
        //afficherUnLivre(livre);

        return livre;
    }

    private void modifierBD(Livre livre) {
        PreparedStatement ps;
        String query = "UPDATE livres SET title = ?, auteur = ?, quantite = ? WHERE isbn = ?";
        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            ps.setString(1, livre.getTitle());
            ps.setString(2, livre.getAuteur());
            ps.setInt(3, livre.getQuantite());
            ps.setString(4, livre.getIsbn());

            int nbRows = ps.executeUpdate();

            if (nbRows > 0) {
                System.out.println("Livre mis à jour dans la BD.");
            } else {
                System.out.println("Aucun livre mis à jour dans la BD.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du livre dans la base de données !");
            e.printStackTrace();
        }
    }

    public Livre recupererLivreIsbn(String isbnRecherche){
        Livre livre = null;
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT l.*, COUNT(e.isbn_livre) as empruntes " +
                "FROM livres l " +
                "LEFT JOIN emprunts e ON l.isbn = e.isbn_livre " +
                "WHERE l.isbn = ?" +
                "GROUP BY l.isbn";

        //String query = "SELECT * FROM livres WHERE isbn = ?";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            ps.setString(1, isbnRecherche);

            rs = ps.executeQuery();

            if (rs.next()) {
                String isbn   = rs.getString("isbn");
                String title  = rs.getString("title");
                String auteur = rs.getString("auteur");
                int quantite  = rs.getInt("quantite");
                int empruntes = rs.getInt("empruntes");

                livre = new Livre(isbn, title, auteur, quantite, empruntes);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du livre par ID !");
            e.printStackTrace();
        }
        return livre;
    }

    public void afficherUnLivre(Livre livre) {
//        System.out.println("ISBN : " + livre.getIsbn());
//        System.out.println("Titre : " + livre.getTitle());
//        System.out.println("Auteur : " + livre.getAuteur());
//        System.out.println("Quantité : " + livre.getQuantite());
        System.out.printf("%-15s | %-30s | %-30s | %-6s%n", "ISBN", "Titre", "Auteur", "Quantité");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s | %-30s | %-30s | %-6d%n", livre.getIsbn(), livre.getTitle(), livre.getAuteur(), livre.getQuantite());
    }

    public void emprunterLivre(String isbn, int nMembre, String nomEmprunteur){
        PreparedStatement ps, psInsert, psUpdate, psInsertEmp, psMmLivre;
        ResultSet rs, rsMmLivre;

        try{
            String querySelect = "SELECT * FROM livres WHERE isbn = ? AND quantite > 0";
            ps = ConnectionDB.getConnection().prepareStatement(querySelect);
            ps.setString(1, isbn);
            rs = ps.executeQuery();

            if (rs.next()){ //le livre est disponible

                //verifier si l'utilisateur a deja emprunté le meme livre
                String query = "SELECT * FROM emprunts WHERE isbn_livre = ? AND id_emprunteur = ? AND status = 'emprunte' AND date_retour_reelle < CURRENT_DATE";
                psMmLivre    = ConnectionDB.getConnection().prepareStatement(query);
                psMmLivre.setString(1, isbn);
                psMmLivre.setInt(2, nMembre);
                rsMmLivre    = psMmLivre.executeQuery();

                if (!rsMmLivre.next()){ //lutilisateur ne doit pas emprunter deux livres dans la mm duree

                    //inserer les infos de l'emprunteur
                    String queryInsEmp = "INSERT INTO emprunteurs (numero_membre, name) VALUES(?, ?)";
                    psInsertEmp        = ConnectionDB.getConnection().prepareStatement(queryInsEmp);
                    psInsertEmp.setInt(1, nMembre);
                    psInsertEmp.setString(2, nomEmprunteur);

                    //inserer les infos d'emprunt
                    LocalDate datePrevue = LocalDate.now().plusDays(7);
                    String queryInsert   = "INSERT INTO emprunts (id_emprunteur, isbn_livre, status, date_retour_prevue) VALUES (?, ?, 'emprunte', ?)";
                    psInsert             = ConnectionDB.getConnection().prepareStatement(queryInsert);
                    psInsert.setInt(1, nMembre);
                    psInsert.setString(2, isbn);
                    psInsert.setDate(3, Date.valueOf(datePrevue));

                    //modifier la quantité des livres
                    String queryUpdate = "UPDATE livres SET quantite = (quantite-1) WHERE isbn = ?";
                    psUpdate           = ConnectionDB.getConnection().prepareStatement(queryUpdate);
                    psUpdate.setString(1, isbn);

                    psInsertEmp.executeUpdate();
                    psInsert.executeUpdate();
                    psUpdate.executeUpdate();

                    System.out.println("Livre emprunté avec succès !");
                }
                else{
                    System.out.println("Vous avez déja emprunté ce livre dans cette date");
                }
            }
            else{
                System.out.println("Le Livre n'est pas disponible pour le moment !");
            }
        }
        catch (Exception e){
            System.err.println("Une erreur est survenue lors de l'emprunt du livre : " + e.getMessage());
        }
    }

    public void retournerLivre(String isbn, int nMembre){
        PreparedStatement ps, ps1;
        ResultSet rs;

        try {
            // verifier si le livre est eprunté
            String querySelect = "SELECT * FROM emprunts WHERE isbn_livre = ? AND status like 'emprunte' AND id_emprunteur = ?";
            ps                 = ConnectionDB.getConnection().prepareStatement(querySelect);
            ps.setString(1, isbn);
            ps.setInt(2, nMembre);
            rs = ps.executeQuery();

            if (rs.next()){
                // update quantité (+1)
                String queryUpdate = "UPDATE livres SET quantite = quantite+1 AND status = 'retourne' AND date_retour_reelle = CURRENT_DATE WHERE isbn = ?";
                ps1                = ConnectionDB.getConnection().prepareStatement(queryUpdate);
                ps1.setString(1, isbn);
                ps1.executeUpdate();

                System.out.println("Le livre a bien été retourné !");
            }else{
                System.out.println("Ce livre n'est pas emprunté par cet utilisateur !");
            }
        }catch (Exception e){
            System.err.println("Une erreur est survenue lors du retour du livre : " + e.getMessage());
        }

    }

    public boolean emprunteurExiste(int numeroMembre) {
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM emprunteurs WHERE numero_membre = ?";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            ps.setInt(1, numeroMembre);
            rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'emprunteur : " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> AfficherEmpunteurs() {
        List<Map<String, Object>> emprunteursList = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT numero_membre, name, title, isbn, date_emprunt, date_retour_prevue " +
                "FROM emprunts e " +
                "JOIN emprunteurs emp ON e.id_emprunteur=emp.numero_membre " +
                "JOIN livres l ON l.isbn=e.isbn_livre " +
                "ORDER BY e.date_emprunt DESC";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            rs = ps.executeQuery();

            int nbColonne = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String, Object> emprunteurs = new HashMap<>();
                for (int i = 1; i <= nbColonne; i++) {
                    String nomColonne = rs.getMetaData().getColumnName(i);
                    Object valueColonne = rs.getObject(i);
                    emprunteurs.put(nomColonne, valueColonne);
                }
                emprunteursList.add(emprunteurs); // Add the map to the list
            }
        } catch (Exception e) {
            System.err.println("Erreur est survenue lors de l'affichage des emprunteurs :" + e.getMessage());
        }
        return emprunteursList;
    }


}
