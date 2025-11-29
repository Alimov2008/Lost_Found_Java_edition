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
}
