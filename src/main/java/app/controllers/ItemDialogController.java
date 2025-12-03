package app.controllers;

import dao.ItemDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Item;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ItemDialogController {

    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField dayField;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private TextField yearField;
    @FXML private TextField locationField;
    @FXML private TextField contactField;

    private String itemType; // "lost" or "found"
    private NewMainController mainController;
    private DialogPane dialogPane;
    private ItemDao itemDao = new ItemDao();

    private static final Map<String, String> monthMap = new HashMap<>();
    static {
        monthMap.put("January", "1");
        monthMap.put("February", "2");
        monthMap.put("March", "3");
        monthMap.put("April", "4");
        monthMap.put("May", "5");
        monthMap.put("June", "6");
        monthMap.put("July", "7");
        monthMap.put("August", "8");
        monthMap.put("September", "9");
        monthMap.put("October", "10");
        monthMap.put("November", "11");
        monthMap.put("December", "12");
    }

    @FXML
    public void initialize() {
        monthComboBox.getItems().addAll(monthMap.keySet());

        LocalDate today = LocalDate.now();
        dayField.setText(String.valueOf(today.getDayOfMonth()));
        yearField.setText(String.valueOf(today.getYear()));
        monthComboBox.setValue(getMonthName(today.getMonthValue()));

        dialogPane = (DialogPane) nameField.getScene().getRoot();
        setupButtonActions();
    }

    public void setItemType(String type) {
        this.itemType = type;
        if (dialogPane != null) {
            dialogPane.setHeaderText(type.equals("lost") ? "Report Lost Item" : "Report Found Item");
        }
    }

    public void setMainController(NewMainController mainController) {
        this.mainController = mainController;
    }

    private void setupButtonActions() {
        Button saveButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        saveButton.setOnAction(event -> handleSave());

        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleSave() {
        if (validateInput()) {
            try {
                Item item = createItemFromForm();

                if ("lost".equals(itemType)) {
                    int id = itemDao.saveLost(item);
                    mainController.updateStatus("Lost item saved successfully! ID: " + id);
                } else {
                    int id = itemDao.saveFound(item);
                    mainController.updateStatus("Found item saved successfully! ID: " + id);
                }

                if ("lost".equals(itemType)) {
                    mainController.handleRefreshLost();
                } else {
                    mainController.handleRefreshFound();
                }

                closeDialog();

            } catch (Exception e) {
                showError("Save Error", "Failed to save item: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleCancel() {
        closeDialog();
    }

    private Item createItemFromForm() {
        Item item = new Item();
        item.setName(nameField.getText().trim());
        item.setDescription(descriptionField.getText().trim());
        item.setYear(Integer.parseInt(yearField.getText().trim()));
        item.setMonth(monthMap.get(monthComboBox.getValue()));
        item.setDay(Integer.parseInt(dayField.getText().trim()));
        item.setLocation(locationField.getText().trim());
        item.setContact(contactField.getText().trim());
        return item;
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Validation Error", "Item name is required");
            nameField.requestFocus();
            return false;
        }

        try {
            int day = Integer.parseInt(dayField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());
            String month = monthComboBox.getValue();

            if (month == null) {
                showError("Validation Error", "Month is required");
                monthComboBox.requestFocus();
                return false;
            }

            if (day < 1 || day > 31) {
                showError("Validation Error", "Day must be between 1 and 31");
                dayField.requestFocus();
                return false;
            }

            if (year < 1900 || year > LocalDate.now().getYear() + 1) {
                showError("Validation Error",
                        "Year must be between 1900 and " + (LocalDate.now().getYear() + 1));
                yearField.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            showError("Validation Error", "Day and Year must be valid numbers");
            return false;
        }

        if (locationField.getText().trim().isEmpty()) {
            showError("Validation Error", "Location is required");
            locationField.requestFocus();
            return false;
        }

        return true;
    }

    private String getMonthName(int month) {
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "January";
        };
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}