import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    //static pour faire l apelle sans creation d un objet
    //return un objet de la class Connection
    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","");
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            System.out.println("Erreur lors de la connection à la base de donnees !");
        }
        return connection;
    }

}
