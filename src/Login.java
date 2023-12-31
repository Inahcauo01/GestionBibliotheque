import java.sql.*;
import java.util.Scanner;

public class Login {
    public boolean authentier() {
        System.out.println("Welcome back");
        Scanner sc = new Scanner(System.in);

        boolean authentified = false;

        do {
            System.out.println("Enter email");
            String email = sc.nextLine();

            System.out.println("Enter password");
            String password = sc.nextLine();

            PreparedStatement ps;
            ResultSet rs;
            String query = "SELECT * FROM bibliothecaires WHERE email = ? AND password = ? ";
            try {
                ps = ConnectionDB.getConnection().prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, password);
                rs = ps.executeQuery();

                // Vérifier si rs n'est pas vide
                if (rs.next()) {        // Authentification réussie
                    authentified = true;
                    break;
                } else {
                    System.out.println("L'email ou le mot de passe est incorrect !");
                    System.out.println("Voulez vous réessayer ? (oui/non)");
                    String re = sc.nextLine();
                    if (!re.equalsIgnoreCase("oui")) {
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Une erreur est survenue lors de la connection : "+e.getMessage());
            }
        }while (true);
        return authentified;
    }
}
