package app.controllers;

import dao.ItemDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Item;

import java.time.LocalDate;

public class ReportFormController {

    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField dayField;
    @FXML private TextField monthField;
    @FXML private TextField yearField;
    @FXML private TextField locationField;
    @FXML private TextField contactField;

    private NewMainController mainController;
    private ItemDao itemDao = new ItemDao();

    @FXML
    public void initialize() {
        typeCombo.getItems().addAll("Lost", "Found");
        typeCombo.setValue("Lost");

        LocalDate today = LocalDate.now();
        dayField.setText(String.valueOf(today.getDayOfMonth()));
        monthField.setText(String.valueOf(today.getMonthValue()));
        yearField.setText(String.valueOf(today.getYear()));
    }

    public void setMainController(NewMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleSubmit() {
        if (validateInput()) {
            try {
                Item item = createItemFromForm();
                String type = typeCombo.getValue().toLowerCase();

                if ("lost".equals(type)) {
                    int id = itemDao.saveLost(item);
                    mainController.onItemSaved(type, item);
                } else {
                    int id = itemDao.saveFound(item);
                    mainController.onItemSaved(type, item);
                }

            } catch (Exception e) {
                showError("Save Error", "Failed to save  " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Item createItemFromForm() {
        Item item = new Item();
        item.setName(nameField.getText().trim());
        item.setDescription(descriptionField.getText().trim());
        item.setYear(Integer.parseInt(yearField.getText().trim()));
        item.setMonth(monthField.getText().trim());
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
            int month = Integer.parseInt(monthField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());

            if (day < 1 || day > 31) {
                showError("Validation Error", "Day must be between 1 and 31");
                dayField.requestFocus();
                return false;
            }

            if (month < 1 || month > 12) {
                showError("Validation Error", "Month must be between 1 and 12");
                monthField.requestFocus();
                return false;
            }

            if (year < 1900 || year > LocalDate.now().getYear() + 1) {
                showError("Validation Error",
                        "Year must be between 1900 and " + (LocalDate.now().getYear() + 1));
                yearField.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            showError("Validation Error", "Date fields must be valid numbers");
            return false;
        }

        if (locationField.getText().trim().isEmpty()) {
            showError("Validation Error", "Location is required");
            locationField.requestFocus();
            return false;
        }

        return true;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}