import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/magazine";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("Connected to the database");
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Disconnected from the database");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
