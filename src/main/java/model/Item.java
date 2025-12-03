package model;

import javafx.beans.property.*;

import java.util.AbstractMap;
import java.util.Map;

public class Item {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty month = new SimpleStringProperty();
    private final IntegerProperty day = new SimpleIntegerProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final StringProperty contact = new SimpleStringProperty();

    public Item() {}

    public Item(int id, String name, String description, int year, String month,
                int day, String location, String contact) {
        setId(id);
        setName(name);
        setDescription(description);
        setYear(year);
        setMonth(month);
        setDay(day);
        setLocation(location);
        setContact(contact);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty descriptionProperty() { return description; }
    public IntegerProperty yearProperty() { return year; }
    public StringProperty monthProperty() { return month; }
    public IntegerProperty dayProperty() { return day; }
    public StringProperty locationProperty() { return location; }
    public StringProperty contactProperty() { return contact; }

    public StringProperty formattedDateProperty() {
        try {
            int monthInt = Integer.parseInt(getMonth());
            return new SimpleStringProperty(
                    String.format("%02d-%02d-%d", getDay(), monthInt, getYear())
            );
        } catch (NumberFormatException e) {
            Map<String, Integer> monthNames = Map.ofEntries(
                    new AbstractMap.SimpleEntry<>("January",1),
            new AbstractMap.SimpleEntry<>("February",2),
            new AbstractMap.SimpleEntry<>("March",3),
            new AbstractMap.SimpleEntry<>("April",4),
            new AbstractMap.SimpleEntry<>("May",5),
            new AbstractMap.SimpleEntry<>("June",6),
            new AbstractMap.SimpleEntry<>("July",7),
            new AbstractMap.SimpleEntry<>("August",8),
            new AbstractMap.SimpleEntry<>("September",9),
            new AbstractMap.SimpleEntry<>("October",10),
            new AbstractMap.SimpleEntry<>("November",11),
            new AbstractMap.SimpleEntry<>("December",12)
            );

            Integer monthInt = monthNames.get(getMonth());
            if (monthInt != null) {
                return new SimpleStringProperty(
                        String.format("%02d-%02d-%d", getDay(), monthInt, getYear())
                );
            }
            return new SimpleStringProperty(getDay() + "-" + getMonth() + "-" + getYear());
        }
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public int getYear() { return year.get(); }
    public void setYear(int year) { this.year.set(year); }

    public String getMonth() { return month.get(); }
    public void setMonth(String month) { this.month.set(month); }

    public int getDay() { return day.get(); }
    public void setDay(int day) { this.day.set(day); }

    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }

    public String getContact() { return contact.get(); }
    public void setContact(String contact) { this.contact.set(contact); }

    @Override
    public String toString() {
        return String.format("Item{id=%d, name='%s', date=%02d-%02d-%d}",
                getId(), getName(), getDay(), Integer.parseInt(getMonth()), getYear());
    }
}