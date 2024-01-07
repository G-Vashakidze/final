//
//import java.sql.SQLException;
//import java.util.List;
//
//public class MainApp {
//
//    public static void main(String[] args) throws SQLException {
//        DatabaseConnector databaseConnector = new DatabaseConnector();
//        databaseConnector.openConnection();
//        ProductDAO products = new ProductDAO(databaseConnector);
//        List<String> productData = products.displayProductsByQuantity();
//        productData.stream().map(data -> {
//                        String[] parts = data.split("-");
//                        String name = parts[0];
//                        int quantity = Integer.parseInt(parts[1]);
//                        return name + " - " + quantity;
//                    })
//                    .forEach(System.out::println);
//
//
//    }
//}


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class MainApp extends Application {

    private DatabaseConnector databaseConnector;
    private ProductDAO productDAO;
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        databaseConnector = new DatabaseConnector();
        databaseConnector.openConnection();
        productDAO = new ProductDAO(databaseConnector);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Inputs for adding a new product
        TextField productNameInput = new TextField();
        productNameInput.setPromptText("Enter Product Name");

        TextField productPriceInput = new TextField();
        productPriceInput.setPromptText("Enter Product Price");

        TextField productQuantityInput = new TextField();
        productQuantityInput.setPromptText("Enter Product Quantity");

        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> {
            String productName = productNameInput.getText();
            double productPrice = Double.parseDouble(productPriceInput.getText());
            int productQuantity = Integer.parseInt(productQuantityInput.getText());

            if (!productName.isEmpty()) {
                try {
                    productDAO.addNewProduct(productName, productPrice, productQuantity);
                    refreshData();
                    clearInputFields(productNameInput, productPriceInput, productQuantityInput);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        // Add inputs and button to the layout
        root.getChildren().addAll(productNameInput, productPriceInput, productQuantityInput, addButton);

        // Create a PieChart with product data
        PieChart pieChart = createPieChart();
        root.getChildren().add(pieChart);

        // Create a scene and set it to the stage
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Product Management");
        primaryStage.show();

        // Initial data load
        refreshData();
    }

    // Helper method to refresh data and update the PieChart
    private void refreshData() {
        try {
            List<String> productData = productDAO.displayProductsByQuantity();
            pieChartData.clear();

            for (String data : productData) {
                String[] parts = data.split("-");
                String name = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                pieChartData.add(new PieChart.Data(name, quantity));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // Helper method to create a PieChart
    private @NotNull PieChart createPieChart() {
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Product Quantities");
        return pieChart;
    }

    // Helper method to clear input fields after adding a product
    private void clearInputFields(TextField nameInput, TextField priceInput, TextField quantityInput) {
        nameInput.clear();
        priceInput.clear();
        quantityInput.clear();
    }
}
