import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

                Livre livre = new Livre(isbn, title, auteur, quantite);
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
        String query = "SELECT * FROM livres WHERE title LIKE ? OR auteur LIKE ?";

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

                Livre l = new Livre(isbn, title, auteur, quantite);
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
                    System.out.println("Entrez le nouveau titre : ");   String newTitle = scanner.nextLine();
                    livre.setTitle(newTitle);
                    modifierBD(livre);
                    break;
                case 2:
                    System.out.println("Entrez le nouvel auteur : ");   String newAuthor = scanner.nextLine();
                    livre.setAuteur(newAuthor);
                    System.out.println("Livre après la modification :");
                    afficherUnLivre(livre);
                    modifierBD(livre);
                    break;
                case 3:
                    System.out.println("Entrez le nouveau titre : ");   newTitle = scanner.nextLine();
                    System.out.println("Entrez le nouvel auteur : ");   String newAuthor2 = scanner.nextLine();
                    livre.setTitle(newTitle);
                    livre.setAuteur(newAuthor2);
                    modifierBD(livre);
                    break;
                case 4:
                    System.out.println("Entrez la nouvelle quantité : ");
                    int newQuantity = scanner.nextInt();
                    livre.setQuantite(newQuantity);
                    modifierBD(livre);
                    break;
                case 0:
                    System.out.println("Modification annulée.");
                    break;
                default:
                    System.out.println("Choix invalide. Réessayez.");
            }
        } while (choix != 0);

        System.out.println("Livre après la modification :");
        afficherUnLivre(livre);

        return livre;
    }

    public Livre recupererLivreIsbn(String isbnRecherche){
        Livre livre = null;
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT * FROM livres WHERE isbn = ?";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            ps.setString(1, isbnRecherche);

            rs = ps.executeQuery();

            if (rs.next()) {
                String isbn   = rs.getString("isbn");
                String title  = rs.getString("title");
                String auteur = rs.getString("auteur");
                int quantite  = rs.getInt("quantite");

                livre = new Livre(isbn, title, auteur, quantite);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du livre par ID !");
            e.printStackTrace();
        }
        return livre;
    }

    public void afficherUnLivre(Livre livre) {
        System.out.println("ISBN : " + livre.getIsbn());
        System.out.println("Titre : " + livre.getTitle());
        System.out.println("Auteur : " + livre.getAuteur());
        System.out.println("Quantité : " + livre.getQuantite());
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
}
