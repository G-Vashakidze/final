import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO {
    private final Connection connection;

    public ProductDAO(DatabaseConnector databaseConnector) {
        this.connection = databaseConnector.getConnection();
    }



    void addNewProduct(String name, double price, int quantity) throws SQLException {
        String insertQuery = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, name);
            insertStatement.setDouble(2, price);
            insertStatement.setInt(3, quantity);
            insertStatement.executeUpdate();
        }
    }

    public List<String> displayProductsByQuantity() throws SQLException {
        String query = "SELECT name, SUM(quantity) AS total_quantity FROM products GROUP BY name";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            List<String> productsByQuantity = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("total_quantity");
                productsByQuantity.add(name + "-" + quantity);
            }
            return productsByQuantity;


}}}
