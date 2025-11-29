package model;

public class Item {
    private int id;
    private String name;
    private String description;
    private int year;
    private String month;
    private int day;
    private String location;
    private String contact;

    public Item() {}

    public Item(int id, String name, String description, int year, String month,
                int day, String location, String contact) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
        this.location = location;
        this.contact = contact;
    }

    public Item(String part, String part1, int i, String part2, int i1, String part3, String part4) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
