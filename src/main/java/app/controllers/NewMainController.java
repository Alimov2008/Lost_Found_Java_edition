package app.controllers;

import app.MainApplication;
import dao.ItemDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Item;

import java.io.IOException;
import java.util.List;

public class NewMainController {

    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private ComboBox<String> viewTypeCombo;
    @FXML private ComboBox<String> searchTypeCombo;
    @FXML private ComboBox<String> deleteTypeCombo;
    @FXML private TextField searchField;
    @FXML private TextField deleteIdField;

    @FXML private TableView<Item> lostTable;
    @FXML private TableView<Item> foundTable;
    @FXML private Label lostCountLabel;
    @FXML private Label foundCountLabel;
    @FXML private Label statusLabel;

    @FXML private VBox formContainer;

    private ItemDao itemDao = new ItemDao();
    private String currentAction = "";

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupTables();
        loadAllItems();
        updateStatus("Ready");
    }

    private void setupComboBoxes() {
        String[] options = {"Lost", "Found"};

        reportTypeCombo.getItems().addAll(options);
        viewTypeCombo.getItems().addAll(options);
        searchTypeCombo.getItems().addAll(options);
        deleteTypeCombo.getItems().addAll(options);

        reportTypeCombo.setValue("Lost");
        viewTypeCombo.setValue("Lost");
        searchTypeCombo.setValue("Lost");
        deleteTypeCombo.setValue("Lost");
    }

    private void setupTables() {
        // Setup Lost Items Table
        TableColumn<Item, Integer> lostIdCol = new TableColumn<>("ID");
        lostIdCol.setCellValueFactory(cellData ->
                cellData.getValue().idProperty().asObject());
        lostIdCol.setPrefWidth(50);

        TableColumn<Item, String> lostNameCol = new TableColumn<>("Name");
        lostNameCol.setCellValueFactory(cellData ->
                cellData.getValue().nameProperty());
        lostNameCol.setPrefWidth(150);

        TableColumn<Item, String> lostDescCol = new TableColumn<>("Description");
        lostDescCol.setCellValueFactory(cellData ->
                cellData.getValue().descriptionProperty());
        lostDescCol.setPrefWidth(200);

        TableColumn<Item, String> lostDateCol = new TableColumn<>("Date");
        lostDateCol.setCellValueFactory(cellData ->
                cellData.getValue().formattedDateProperty());
        lostDateCol.setPrefWidth(100);

        TableColumn<Item, String> lostLocationCol = new TableColumn<>("Location");
        lostLocationCol.setCellValueFactory(cellData ->
                cellData.getValue().locationProperty());
        lostLocationCol.setPrefWidth(150);

        lostTable.getColumns().addAll(lostIdCol, lostNameCol, lostDescCol, lostDateCol, lostLocationCol);

        // Setup Found Items Table
        TableColumn<Item, Integer> foundIdCol = new TableColumn<>("ID");
        foundIdCol.setCellValueFactory(cellData ->
                cellData.getValue().idProperty().asObject());
        foundIdCol.setPrefWidth(50);

        TableColumn<Item, String> foundNameCol = new TableColumn<>("Name");
        foundNameCol.setCellValueFactory(cellData ->
                cellData.getValue().nameProperty());
        foundNameCol.setPrefWidth(150);

        TableColumn<Item, String> foundDescCol = new TableColumn<>("Description");
        foundDescCol.setCellValueFactory(cellData ->
                cellData.getValue().descriptionProperty());
        foundDescCol.setPrefWidth(200);

        TableColumn<Item, String> foundDateCol = new TableColumn<>("Date");
        foundDateCol.setCellValueFactory(cellData ->
                cellData.getValue().formattedDateProperty());
        foundDateCol.setPrefWidth(100);

        TableColumn<Item, String> foundLocationCol = new TableColumn<>("Location");
        foundLocationCol.setCellValueFactory(cellData ->
                cellData.getValue().locationProperty());
        foundLocationCol.setPrefWidth(150);

        foundTable.getColumns().addAll(foundIdCol, foundNameCol, foundDescCol, foundDateCol, foundLocationCol);
    }

    @FXML
    private void handleReportButton() {
        showActionUI("report");
        reportTypeCombo.setVisible(true);
        loadReportForm();
    }

    @FXML
    private void handleViewButton() {
        showActionUI("view");
        viewTypeCombo.setVisible(true);
        loadViewForm();
    }

    @FXML
    private void handleSearchButton() {
        showActionUI("search");
        searchTypeCombo.setVisible(true);
        searchField.setVisible(true);
        loadSearchForm();
    }

    @FXML
    private void handleDeleteButton() {
        showActionUI("delete");
        deleteTypeCombo.setVisible(true);
        deleteIdField.setVisible(true);
        loadDeleteForm();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void showActionUI(String action) {
        reportTypeCombo.setVisible(false);
        viewTypeCombo.setVisible(false);
        searchTypeCombo.setVisible(false);
        deleteTypeCombo.setVisible(false);
        searchField.setVisible(false);
        deleteIdField.setVisible(false);

        formContainer.getChildren().clear();

        currentAction = action;
        updateStatus("Action: " + action.substring(0, 1).toUpperCase() + action.substring(1));
    }

    private void loadReportForm() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/report_form.fxml")
            );
            Parent form = loader.load();
            ReportFormController controller = loader.getController();
            controller.setMainController(this);

            formContainer.getChildren().add(form);

        } catch (IOException e) {
            showError("Error", "Cannot load report form: " + e.getMessage());
        }
    }

    private void loadViewForm() {
        VBox form = new VBox(10);
        form.setStyle("-fx-padding: 10;");

        Label label = new Label("Select item type and click view:");
        Button viewButton = new Button("View Selected");
        viewButton.setOnAction(e -> handleViewSelected());

        form.getChildren().addAll(label, viewButton);
        formContainer.getChildren().add(form);
    }

    private void loadSearchForm() {
        VBox form = new VBox(10);
        form.setStyle("-fx-padding: 10;");

        Label label = new Label("Search items:");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> handleSearch());

        form.getChildren().addAll(label, searchButton);
        formContainer.getChildren().add(form);
    }

    private void loadDeleteForm() {
        VBox form = new VBox(10);
        form.setStyle("-fx-padding: 10;");

        Label label = new Label("Enter ID to delete:");
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> handleDelete());

        form.getChildren().addAll(label, deleteButton);
        formContainer.getChildren().add(form);
    }

    private void handleViewSelected() {
        String type = viewTypeCombo.getValue().toLowerCase();

        try {
            if ("lost".equals(type)) {
                List<Item> items = itemDao.listLost();
                lostTable.getItems().setAll(items);
                lostCountLabel.setText(String.valueOf(items.size()));
                updateStatus("Showing all lost items (" + items.size() + " items)");
            } else {
                List<Item> items = itemDao.getFoundAll();
                foundTable.getItems().setAll(items);
                foundCountLabel.setText(String.valueOf(items.size()));
                updateStatus("Showing all found items (" + items.size() + " items)");
            }
        } catch (Exception e) {
            showError("View Error", e.getMessage());
        }
    }

    private void handleSearch() {
        String type = searchTypeCombo.getValue().toLowerCase();
        String term = searchField.getText();

        if (term.isEmpty()) {
            showError("Search Error", "Please enter a search term");
            return;
        }

        try {
            if ("lost".equals(type)) {
                List<Item> items = itemDao.searchLostByName(term);
                lostTable.getItems().setAll(items);
                lostCountLabel.setText(String.valueOf(items.size()));
                updateStatus("Found " + items.size() + " matching lost items");
            } else {
                List<Item> items = itemDao.searchFoundByName(term);
                foundTable.getItems().setAll(items);
                foundCountLabel.setText(String.valueOf(items.size()));
                updateStatus("Found " + items.size() + " matching found items");
            }
        } catch (Exception e) {
            showError("Search Error", e.getMessage());
        }
    }

    private void handleDelete() {
        String type = deleteTypeCombo.getValue().toLowerCase();
        String idText = deleteIdField.getText();

        if (idText.isEmpty()) {
            showError("Delete Error", "Please enter an ID");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete " + type + " item");
            confirm.setContentText("Are you sure you want to delete item ID: " + id + "?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                if ("lost".equals(type)) {
                    itemDao.deleteLost(id);
                    loadLostItems();
                    updateStatus("Deleted lost item with ID: " + id);
                } else {
                    itemDao.deleteFound(id);
                    loadFoundItems();
                    updateStatus("Deleted found item with ID: " + id);
                }
                deleteIdField.clear();
            }
        } catch (NumberFormatException e) {
            showError("Delete Error", "ID must be a number");
        } catch (Exception e) {
            showError("Delete Error", e.getMessage());
        }
    }

    private void loadAllItems() {
        loadLostItems();
        loadFoundItems();
    }

    private void loadLostItems() {
        try {
            List<Item> items = itemDao.listLost();
            lostTable.getItems().setAll(items);
            lostCountLabel.setText(String.valueOf(items.size()));
        } catch (Exception e) {
            showError("Error loading lost items", e.getMessage());
        }
    }

    private void loadFoundItems() {
        try {
            List<Item> items = itemDao.getFoundAll();
            foundTable.getItems().setAll(items);
            foundCountLabel.setText(String.valueOf(items.size()));
        } catch (Exception e) {
            showError("Error loading found items", e.getMessage());
        }
    }

    public void onItemSaved(String type, Item item) {
        if ("lost".equals(type)) {
            loadLostItems();
            updateStatus("Lost item saved: " + item.getName());
        } else {
            loadFoundItems();
            updateStatus("Found item saved: " + item.getName());
        }

        formContainer.getChildren().clear();
        reportTypeCombo.setVisible(false);
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}