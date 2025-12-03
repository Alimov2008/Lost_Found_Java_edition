package app;

import dao.ItemDao;
import model.Item;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField yearField;
    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField locationField;
    @FXML private TextField contactField;
    @FXML private Button submitButton;
    @FXML private Label statusLabel;

    @FXML private TableView<Item> lostTable;
    @FXML private TableColumn<Item, String> lostIdCol;
    @FXML private TableColumn<Item, String> lostNameCol;
    @FXML private TableColumn<Item, String> lostDescCol;
    @FXML private TableColumn<Item, String> lostDateCol;
    @FXML private TableColumn<Item, String> lostLocationCol;
    @FXML private TableColumn<Item, String> lostContactCol;

    @FXML private TableView<Item> foundTable;
    @FXML private TableColumn<Item, String> foundIdCol;
    @FXML private TableColumn<Item, String> foundNameCol;
    @FXML private TableColumn<Item, String> foundDescCol;
    @FXML private TableColumn<Item, String> foundDateCol;
    @FXML private TableColumn<Item, String> foundLocationCol;
    @FXML private TableColumn<Item, String> foundContactCol;

    private final ItemDao itemDao = new ItemDao();
    private ObservableList<Item> lostList = FXCollections.observableArrayList();
    private ObservableList<Item> foundList = FXCollections.observableArrayList();

    private enum Mode { NONE, REPORT, SEARCH, DELETE }
    private enum Type { NONE, LOST, FOUND }

    private Mode currentMode = Mode.NONE;
    private Type currentType = Type.NONE;

    @FXML
    public void initialize() {
        configureTableColumns();

        refreshAllTables();

        submitButton.setDisable(true);
    }

    private void configureTableColumns() {
        lostIdCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        lostNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        lostDescCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        lostDateCol.setCellValueFactory(c -> new SimpleStringProperty(formatDate(c.getValue())));
        lostLocationCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLocation()));
        lostContactCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getContact()));

        foundIdCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        foundNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        foundDescCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        foundDateCol.setCellValueFactory(c -> new SimpleStringProperty(formatDate(c.getValue())));
        foundLocationCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLocation()));
        foundContactCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getContact()));

        lostTable.setItems(lostList);
        foundTable.setItems(foundList);

        lostTable.getSelectionModel().selectedItemProperty().addListener((s, oldV, newV) -> {
            if (newV != null) populateFormFromItem(newV);
        });
        foundTable.getSelectionModel().selectedItemProperty().addListener((s, oldV, newV) -> {
            if (newV != null) populateFormFromItem(newV);
        });
    }

    private String formatDate(Item it) {
        if (it == null) return "";
        return String.format("%02d-%s-%d", it.getDay(), it.getMonth(), it.getYear());
    }

    private void populateFormFromItem(Item it) {
        idField.setText(String.valueOf(it.getId()));
        nameField.setText(it.getName());
        descriptionField.setText(it.getDescription());
        yearField.setText(String.valueOf(it.getYear()));
        monthField.setText(it.getMonth());
        dayField.setText(String.valueOf(it.getDay()));
        locationField.setText(it.getLocation());
        contactField.setText(it.getContact());
    }

    private void refreshAllTables() {
        try {
            List<Item> lost = itemDao.getLostAll();
            List<Item> found = itemDao.getFoundAll();
            lostList.setAll(lost);
            foundList.setAll(found);
            statusLabel.setText("Loaded: Lost=" + lost.size() + " Found=" + found.size());
        } catch (SQLException e) {
            showError("Error loading items: " + e.getMessage());
        }
    }

    @FXML void onReportLost() { prepareFormFor(Mode.REPORT, Type.LOST); }
    @FXML void onReportFound() { prepareFormFor(Mode.REPORT, Type.FOUND); }

    @FXML void onViewLost() {
        try {
            List<Item> list = itemDao.getLostAll();
            lostList.setAll(list);
            statusLabel.setText("Viewing all lost items (" + list.size() + ")");
        } catch (SQLException e) {
            showError("Error viewing lost items: " + e.getMessage());
        }
    }
    @FXML void onViewFound() {
        try {
            List<Item> list = itemDao.getFoundAll();
            foundList.setAll(list);
            statusLabel.setText("Viewing all found items (" + list.size() + ")");
        } catch (SQLException e) {
            showError("Error viewing found items: " + e.getMessage());
        }
    }

    @FXML void onSearchLost() { prepareFormFor(Mode.SEARCH, Type.LOST); }
    @FXML void onSearchFound() { prepareFormFor(Mode.SEARCH, Type.FOUND); }

    @FXML void onDeleteLost() { prepareFormFor(Mode.DELETE, Type.LOST); }
    @FXML void onDeleteFound() { prepareFormFor(Mode.DELETE, Type.FOUND); }

    @FXML void onExit() {
        Platform.exit();
    }

    private void prepareFormFor(Mode mode, Type type) {
        currentMode = mode;
        currentType = type;
        submitButton.setDisable(false);
        switch (mode) {
            case REPORT -> submitButton.setText("Report " + (type == Type.LOST ? "Lost" : "Found"));
            case SEARCH -> submitButton.setText("Search " + (type == Type.LOST ? "Lost" : "Found"));
            case DELETE -> submitButton.setText("Delete " + (type == Type.LOST ? "Lost" : "Found"));
            default -> submitButton.setText("Submit");
        }
        statusLabel.setText("Mode: " + currentMode + " Type: " + currentType);
        clearFormFields();
    }

    @FXML
    void onSubmit() {
        try {
            switch (currentMode) {
                case REPORT -> handleReport();
                case SEARCH -> handleSearch();
                case DELETE -> handleDelete();
                default -> showError("No action selected. Use left menu to choose an action.");
            }
        } catch (Exception e) {
            showError("Action failed: " + e.getMessage());
        }
    }

    private void handleReport() throws SQLException {
        Item item = collectItemFromForm(false);
        if (item == null) return; // validation failed
        if (currentType == Type.LOST) {
            int id = itemDao.saveLost(item);
            statusLabel.setText("Lost item reported (id=" + id + ")");
            refreshLostTableOnly();
        } else {
            int id = itemDao.saveFound(item);
            statusLabel.setText("Found item reported (id=" + id + ")");
            refreshFoundTableOnly();
        }
        clearFormFields();
    }

    private void handleSearch() throws SQLException {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("To search, enter Name (partial or full).");
            return;
        }
        if (currentType == Type.LOST) {
            lostList.setAll(itemDao.searchLostByName(name));
            statusLabel.setText("Search results for lost items: " + lostList.size());
        } else {
            foundList.setAll(itemDao.searchFoundByName(name));
            statusLabel.setText("Search results for found items: " + foundList.size());
        }
    }

    private void handleDelete() throws SQLException {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            showError("To delete, enter ID in the ID field.");
            return;
        }
        int id;
        try { id = Integer.parseInt(idText); } catch (NumberFormatException e) { showError("Invalid ID."); return; }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete " + currentType + " item id=" + id + "?");
        alert.setContentText("This action cannot be undone.");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) {
            statusLabel.setText("Delete cancelled");
            return;
        }

        if (currentType == Type.LOST) {
            itemDao.deleteLost(id);
            statusLabel.setText("Deleted lost item id=" + id);
            refreshLostTableOnly();
        } else {
            itemDao.deleteFound(id);
            statusLabel.setText("Deleted found item id=" + id);
            refreshFoundTableOnly();
        }
        clearFormFields();
    }

    @FXML
    void onClear() {
        clearFormFields();
        statusLabel.setText("");
    }

    private void clearFormFields() {
        idField.clear();
        nameField.clear();
        descriptionField.clear();
        yearField.clear();
        monthField.clear();
        dayField.clear();
        locationField.clear();
        contactField.clear();
    }

    private Item collectItemFromForm(boolean includeId) {
        Item item = new Item();
        try {
            if (includeId && !idField.getText().trim().isEmpty()) {
                item.setId(Integer.parseInt(idField.getText().trim()));
            }
        } catch (NumberFormatException e) {
            showError("ID must be a number.");
            return null;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Name is required.");
            return null;
        }
        item.setName(name);
        item.setDescription(descriptionField.getText().trim());

        int year = parseIntOrDefault(yearField.getText().trim(), 0);
        String month = monthField.getText().trim();
        int day = parseIntOrDefault(dayField.getText().trim(), 0);

        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);

        item.setLocation(locationField.getText().trim());
        item.setContact(contactField.getText().trim());

        return item;
    }

    private int parseIntOrDefault(String s, int def) {
        if (s == null || s.isEmpty()) return def;
        try { return Integer.parseInt(s); } catch (NumberFormatException ignored) { return def; }
    }

    private void refreshLostTableOnly() {
        try {
            lostList.setAll(itemDao.getLostAll());
        } catch (SQLException e) {
            showError("Error refreshing lost: " + e.getMessage());
        }
    }

    private void refreshFoundTableOnly() {
        try {
            foundList.setAll(itemDao.getFoundAll());
        } catch (SQLException e) {
            showError("Error refreshing found: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        statusLabel.setText("ERROR: " + msg);
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
