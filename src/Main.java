import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        System.out.println("\t8. Voir les statistiques");
        System.out.println("\t9. Génér un rapport");
        System.out.println("\t0. Quiter");
        // Ajoutez d'autres options de menu selon vos besoins

        Scanner sc = new Scanner(System.in);
        System.out.println("Entrer votre choix:");
        int choice = sc.nextInt();

        Bibliothecaire bibliothecaire = new Bibliothecaire();
        Rapport rapport = new Rapport();

        // Traitez les choix de menu ici en utilisant des instructions conditionnelles
        switch (choice) {
            // ########## Ajouter un livre ##########
            case 1:
                Scanner scanner = new Scanner(System.in);
                System.out.println("__________________________________________");
                System.out.println("############ Ajouter un Livre ############");
                System.out.println("------------------------------------------");
                //System.out.println("Entrez l'ID du livre : ");              int id = scanner.nextInt();
                //scanner.nextLine();
                System.out.println("Entrez l'ISBN du livre : ");            String isbn   = scanner.nextLine();
                System.out.println("Entrez le titre du livre : ");          String titre  = scanner.nextLine();
                System.out.println("Entrez l'auteur du livre : ");          String auteur = scanner.nextLine();
                System.out.println("Entrez la quantité du livre : ");       int quantite  = scanner.nextInt();

                Livre livre = new Livre(isbn, titre, auteur, quantite, 0);
                bibliothecaire.ajouterLivre(livre);
                break;

            // ########## Afficher les livres ##########
            case 2:
                List<Livre> livres = bibliothecaire.afficherLivres();

                System.out.printf("\t%-15s | %-30s | %-30s | %-12s | %-12s | %-15s%n", "ISBN", "Titre", "Auteur", "Disponibles", "Empruntés", " Quantité totale");
                System.out.println("\t------------------------------------------------------------------------------------------------------------------------------------");
                for (Livre lv : livres) {
                    System.out.printf("\t%-15s | %-30s | %-30s | %-12d | %-12d | %-15d%n", lv.getIsbn(), lv.getTitle(), lv.getAuteur(), lv.getQuantite(), lv.getEmprunte(), lv.getQuantite() + lv.getEmprunte());
                }

                if (retourMenu()) {
                    return true; // Quitter le programme
                }
                break;

            // ########## Recherecher un livre ##########
            case 3:
                Scanner scann = new Scanner(System.in);
                System.out.print("Rechercher un livre par son titre ou son auteur : ");
                String mot = scann.nextLine();
                List<Livre> livres_recherches = bibliothecaire.rechercherLivre(mot);
                System.out.println("______________________________________");
                for (Livre lr: livres_recherches){
                    System.out.println("\tISBN    : " + lr.getIsbn());
                    System.out.println("\tTitre   : " + lr.getTitle());
                    System.out.println("\tAuteur  : " + lr.getAuteur());
                    System.out.println("\tQuantité: " + lr.getQuantite());
                    System.out.println("\tNb Emprunté: " + lr.getEmprunte());
                    System.out.println("________________________________");
                }
                if (retourMenu()) {
                    return true; // Quitter le programme
                }
                break;

            // ########### Emprunter un livre ###########
            case 4:
                Scanner scn = new Scanner(System.in);
                String isbnEmprunte;
                do {
                    System.out.print("Entrez l'ISBN du livre que vous souhaitez emprunter : ");
                    isbnEmprunte = scn.nextLine();
                }while (bibliothecaire.recupererLivreIsbn(isbnEmprunte) == null);


                System.out.print("Entrez le numéro de membre : ");
                int numeroMembre = scn.nextInt();
                scn.nextLine(); // Pour consommer la nouvelle ligne après le numéro

                System.out.print("Entrez le nom de l'emprunteur : ");
                String nomEmprunteur = scn.nextLine();
                bibliothecaire.emprunterLivre(isbnEmprunte, numeroMembre, nomEmprunteur);

                if (retourMenu()) {
                    return true; // Quitter le programme
                }
                break;

            // ########## Retourner un livre ##########
            case 5:
                Scanner scnr = new Scanner(System.in);
                String isbnRetour;
                do {
                    System.out.print("Entrez l'ISBN du livre que vous souhaitez retourner : ");
                    isbnRetour = scnr.nextLine();
                }while (bibliothecaire.recupererLivreIsbn(isbnRetour) == null);

                System.out.print("Entrez le numéro de membre de l'emprunteur : ");
                int numero = scnr.nextInt();

                if (bibliothecaire.emprunteurExiste(numero)) {
                    bibliothecaire.retournerLivre(isbnRetour, numero);
                } else {
                    System.out.println("L'emprunteur n'existe pas dans la base de données.");
                }

                if (retourMenu()) {
                    return true; // Quitter le programme
                }
                break;

            // ########## Supprimer un livre ##########
            case 6:
                while (true) {
                    Scanner scan = new Scanner(System.in);
                    String isbnToDelete;
                    do {
                        System.out.print("Entrez l'ISBN du livre que vous souhaitez supprimer : ");
                        isbnToDelete = scan.nextLine();
                    }while (bibliothecaire.recupererLivreIsbn(isbnToDelete) == null);

                    bibliothecaire.suppLivre(isbnToDelete);
                    System.out.print("Voulez-vous supprimer un autre livre ? (O/N): ");
                    String userInput = scan.nextLine();
                    if (!"O".equalsIgnoreCase(userInput)) {
                        break;
                    }
                }
                break;

            // ########## Modifier un livre ##########
            case 7:
                Scanner scan = new Scanner(System.in);
                String isbnModif;
                do {
                    System.out.print("Entrez l'ISBN du livre que vous souhaitez modifier : ");
                    isbnModif = scan.nextLine();
                }while (bibliothecaire.recupererLivreIsbn(isbnModif) == null);

                bibliothecaire.modifierLivre(isbnModif);
                break;

            // ########## Voir les statistiques ##########
            case 8:
                Map<String, Integer> statistiques = rapport.statistiquesLivres();

                System.out.println("Statistiques des livres :");
                System.out.println("\t-------------------------------------------------------------------------------------");
                System.out.printf("\t| %-20s | %-20s | %-20s | %-20s \n","Total de livres","Livres disponibles","Livres empruntés","Livres perdus");
                System.out.println("\t-------------------------------------------------------------------------------------");
                System.out.printf("\t| %-20d | %-20d | %-20d | %-20d \n",statistiques.get("Total de livres"), statistiques.get("Livres disponibles"), statistiques.get("Livres empruntés"), statistiques.get("Livres perdus"));
                System.out.println("\t_____________________________________________________________________________________");

                if (retourMenu()) {
                    return true; // Quitter le programme
                }
                break;


            // ########## Générer un rapport ##########
            case 9:
                break;

            // ############### Quitter #################
            case 0:
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
