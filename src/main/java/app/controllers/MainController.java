package app.controllers;

import app.MainApplication;
import dao.ItemDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Item;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private TabPane mainTabPane;
    @FXML private TableView<Item> lostTable;
    @FXML private TableView<Item> foundTable;
    @FXML private TextField searchLostField;
    @FXML private TextField searchFoundField;
    @FXML private Label statusLabel;

    private ItemDao itemDao = new ItemDao();

    @FXML
    public void initialize() {
        setupTables();
        loadLostItems();
        loadFoundItems();
        updateStatus("Ready");
    }

    private void setupTables() {
        //lost item table
        TableColumn<Item, Integer> lostIdCol = new TableColumn<>("ID");
        lostIdCol.setCellValueFactory(cellData ->
                cellData.getValue().idProperty().asObject());

        TableColumn<Item, String> lostNameCol = new TableColumn<>("Name");
        lostNameCol.setCellValueFactory(cellData ->
                cellData.getValue().nameProperty());

        TableColumn<Item, String> lostDateCol = new TableColumn<>("Date");
        lostDateCol.setCellValueFactory(cellData ->
                cellData.getValue().formattedDateProperty());

        TableColumn<Item, String> lostLocationCol = new TableColumn<>("Location");
        lostLocationCol.setCellValueFactory(cellData ->
                cellData.getValue().locationProperty());

        lostTable.getColumns().addAll(lostIdCol, lostNameCol, lostDateCol, lostLocationCol);

        //found item table
        TableColumn<Item, Integer> foundIdCol = new TableColumn<>("ID");
        foundIdCol.setCellValueFactory(cellData ->
                cellData.getValue().idProperty().asObject());

        TableColumn<Item, String> foundNameCol = new TableColumn<>("Name");
        foundNameCol.setCellValueFactory(cellData ->
                cellData.getValue().nameProperty());

        TableColumn<Item, String> foundDateCol = new TableColumn<>("Date");
        foundDateCol.setCellValueFactory(cellData ->
                cellData.getValue().formattedDateProperty());

        TableColumn<Item, String> foundLocationCol = new TableColumn<>("Location");
        foundLocationCol.setCellValueFactory(cellData ->
                cellData.getValue().locationProperty());

        foundTable.getColumns().addAll(foundIdCol, foundNameCol, foundDateCol, foundLocationCol);
    }

    private void loadLostItems() {
        try {
            List<Item> items = itemDao.getLostAll();
            lostTable.getItems().setAll(items);
            updateStatus("loaded " + items.size() + " lost items");
        } catch (Exception e) {
            showError("error loading lost items", e.getMessage());
        }
    }

    private void loadFoundItems() {
        try {
            List<Item> items = itemDao.getFoundAll();
            foundTable.getItems().setAll(items);
            updateStatus("Loaded " + items.size() + " found items");
        } catch (Exception e) {
            showError("Error loading found items", e.getMessage());
        }
    }

    //event handlers
    @FXML
    private void handleReportLost() {
        showItemDialog("lost");
    }

    @FXML
    private void handleReportFound() {
        showItemDialog("found");
    }

    @FXML
    private void handleSearchLost() {
        String searchTerm = searchLostField.getText();
        if (searchTerm.isEmpty()) {
            loadLostItems();
        } else {
            try {
                List<Item> items = itemDao.searchLostByName(searchTerm);
                lostTable.getItems().setAll(items);
                updateStatus("found " + items.size() + " matching lost items");
            } catch (Exception e) {
                showError("Search Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleSearchFound() {
        updateStatus("dushman, nothing to see here yet");
    }

    @FXML
    private void handleRefreshLost() {
        loadLostItems();
    }

    @FXML
    private void handleRefreshFound() {
        loadFoundItems();
    }

    @FXML
    private void handleDeleteLost() {
        Item selected = lostTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Delete Error", "Please select an item to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Lost Item");
        confirm.setContentText("Are you sure you want to delete: " + selected.getName() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                itemDao.deleteLost(selected.getId());
                loadLostItems();
                updateStatus("Deleted lost item: " + selected.getName());
            } catch (Exception e) {
                showError("Delete Error", "Failed to delete item: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteFound() {
        Item selected = foundTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Delete Error", "Please select an item to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Found Item");
        confirm.setContentText("Are you sure you want to delete: " + selected.getName() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                itemDao.deleteFound(selected.getId());
                loadFoundItems();
                updateStatus("Deleted found item: " + selected.getName());
            } catch (Exception e) {
                showError("Delete Error", "Failed to delete item: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Lost & Found System");
        alert.setHeaderText("Lost & Found System");
        alert.setContentText("Version 1.0\nA system for managing lost and found items.");
        alert.showAndWait();
    }

    private void showItemDialog(String type) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/item_dialog.fxml")
            );
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(type.equals("lost") ? "Report Lost Item" : "Report Found Item");
            dialog.setDialogPane(loader.load());

            ItemDialogController controller = loader.getController();
            controller.setItemType(type);
            controller.setMainController(this);

            dialog.showAndWait();

        } catch (IOException e) {
            showError("Error", "Cannot open dialog: " + e.getMessage());
            e.printStackTrace();
        }
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