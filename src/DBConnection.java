import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Class to establish a connection with the MySQL database
public class DBConnection {

    String url = "jdbc:mysql://localhost:3307/HealthConnect";
    String username = "root";
    String password = "";

    // Method to create and return a database connection
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}