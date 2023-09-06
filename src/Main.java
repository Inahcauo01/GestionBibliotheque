import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Login login = new Login();
        boolean authenticated = login.authentier();
        boolean menu = false;

        if (authenticated) {
            while (!menu){
                menu = displayMenu();
                if (menu) {
                    break; // Quitter la boucle si l'utilisateur choisit de quitter
                }
            }
        } else {
            System.out.println("L'email ou le mot de passe est incorrect !");
        }
    }

    private static boolean displayMenu() {
        // Afficher le menu ici
        System.out.println("_______________________________________");
        System.out.println("################# Menu ################");
        System.out.println("---------------------------------------");
        System.out.println("\t1. Ajouter un livre");
        System.out.println("\t2. Afficher les livres");
        System.out.println("\t3. Rechercher un livre");
        System.out.println("\t4. Emprunter un livre");
        System.out.println("\t5. Retourner un livre");
        System.out.println("\t6. Supprimer un livre");
        System.out.println("\t7. Modifier un livre");
        System.out.println("\t8. Génér un rapport");
        System.out.println("\t9. Quiter");
        // Ajoutez d'autres options de menu selon vos besoins

        Scanner sc = new Scanner(System.in);
        System.out.println("Entrer votre choix:");
        int choice = sc.nextInt();

        Bibliothecaire bibliothecaire = new Bibliothecaire();
        // Traitez les choix de menu ici en utilisant des instructions conditionnelles
        switch (choice) {
            case 1:
                Scanner scanner = new Scanner(System.in);
                System.out.println("__________________________________________");
                System.out.println("############ Ajouter un Livre ############");
                System.out.println("------------------------------------------");
                System.out.println("Entrez l'ID du livre : ");              int id = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Entrez l'ISBN du livre : ");            String isbn   = scanner.nextLine();
                System.out.println("Entrez le titre du livre : ");          String titre  = scanner.nextLine();
                System.out.println("Entrez l'auteur du livre : ");          String auteur = scanner.nextLine();
                System.out.println("Entrez la quantité du livre : ");       int quantite  = scanner.nextInt();

                Livre livre = new Livre(id, isbn, titre, auteur, quantite);
                bibliothecaire.ajouterLivre(livre);
                break;
            case 2:
                List<Livre> livres = bibliothecaire.afficherLivres();
                System.out.println("______________________________________");
                System.out.println("######### Afficher les livres ########");
                System.out.println("--------------------------------------");
                for (Livre lv : livres) {
                    System.out.println("\tID      : " + lv.getId());
                    System.out.println("\tISBN    : " + lv.getIsbn());
                    System.out.println("\tTitle   : " + lv.getTitle());
                    System.out.println("\tAuteur  : " + lv.getAuteur());
                    System.out.println("\tQuantité: " + lv.getQuantite());
                    System.out.println("________________________________");
                }
                if (retourMenu()) {
                    return true; // Quitter le programme
                }
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                while (true) {
                    Scanner scann = new Scanner(System.in);
                    System.out.print("Entrez l'ISBN du livre que vous souhaitez supprimer : ");
                    String isbnToDelete = scann.nextLine();
                    //bibliothecaire.suppTousLivres(isbnToDelete);
                    bibliothecaire.suppLivre(isbnToDelete);
                    System.out.print("Voulez-vous supprimer un autre livre ? (O/N): ");
                    String userInput = scann.nextLine();
                    if (!"O".equalsIgnoreCase(userInput)) {
                        break;
                    }
                }
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                System.out.println("Deconnection ...");
                return true;
            default:
                System.out.println("Veuillez choisir un choix");
        }
        return false;
    }

    private static boolean retourMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous continuer ? (O/N)");
        String userInput = sc.next();
        return !"O".equalsIgnoreCase(userInput);
    }

}
