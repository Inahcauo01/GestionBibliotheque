import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Login login = new Login();
        boolean authenticated = login.authentier();

        if (authenticated) {
            // Si l'utilisateur est authentifié, affichez le menu
            displayMenu();
        } else {
            System.out.println("L'email ou le mot de passe est incorrect !");
        }
    }

    private static void displayMenu() {
        // Afficher le menu ici
        System.out.println("Menu options:");
        System.out.println("1. Ajouter un livre");
        System.out.println("2. Afficher les livres");
        System.out.println("3. Rechercher un livre");
        System.out.println("4. Emprunter un livre");
        System.out.println("5. Retourner un livre");
        System.out.println("6. Supprimer un livre");
        System.out.println("7. Modifier un livre");
        System.out.println("8. Génér un rapport");
        System.out.println("9. Quiter");
        // Ajoutez d'autres options de menu selon vos besoins

        Scanner sc = new Scanner(System.in);
        System.out.println("Entrer votre choix:");
        int choice = sc.nextInt();

        Bibliothecaire bibliothecaire = new Bibliothecaire();
        // Traitez les choix de menu ici en utilisant des instructions conditionnelles
        switch (choice) {
            case 1:
                Scanner scanner = new Scanner(System.in);

                System.out.println("Entrez l'ID du livre : ");              int id = scanner.nextInt();
                scanner.nextLine(); // Pour consommer la nouvelle ligne restante

                System.out.println("Entrez l'ISBN du livre : ");            String isbn   = scanner.nextLine();
                System.out.println("Entrez le titre du livre : ");          String titre  = scanner.nextLine();
                System.out.println("Entrez l'auteur du livre : ");          String auteur = scanner.nextLine();
                System.out.println("Entrez la quantité du livre : ");       int quantite  = scanner.nextInt();

                Livre livre = new Livre(id, isbn, titre, auteur, quantite);
                bibliothecaire.ajouterLivre(livre);
                break;
            case 2:
                List<Livre> livres = bibliothecaire.afficherLivres();
                for (Livre lv : livres) {
                    System.out.println("ID      : " + lv.getId());
                    System.out.println("ISBN    : " + lv.getIsbn());
                    System.out.println("Title   : " + lv.getTitle());
                    System.out.println("Auteur  : " + lv.getAuteur());
                    System.out.println("Quantité: " + lv.getQuantite());
                    System.out.println("_______________________________");
                }
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            default:
                System.out.println("Veuillez choisir un choix");
        }
    }
}
