package app;

import dao.Database;
import dao.ItemDao;
import model.Item;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static ItemDao itemDao = new ItemDao();
    private static Scanner scanner = new Scanner(System.in);
    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    public static void main(String[] args) {
        Database.initialize();
        System.out.println("====== Lost & Found System ======");
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n====== Console ======");
            System.out.println("1. Report Lost Item");
            System.out.println("2. Report Found Item");
            System.out.println("3. View All Lost Items");
            System.out.println("4. View All Found Items");
            System.out.println("5. Search Lost Items");
            System.out.println("6. Delete Lost Item");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> reportLostItem();
                case 2 -> reportFoundItem();
                case 3 -> viewAllLostItems();
                case 4 -> viewAllFoundItems();
                case 5 -> searchLostItems();
                case 6 -> deleteLostItem();
                case 7 -> {
                    System.out.println("Dushman naroda");
                    return;
                }
                default -> System.out.println("Invalid option, try again");
            }
        }
    }

    private static void reportLostItem() {
        System.out.println("\n===== Report Lost Item =====");
        Item item = getItemDetailsFromUser();

        try {
            int id = itemDao.saveLost(item);
            System.out.println("Lost item reported ");
        } catch (Exception e) {
            System.out.println("Error reporting lost item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void reportFoundItem() {
        System.out.println("\n===== Report Found Item =====");
        Item item = getItemDetailsFromUser();

        try {
            int id = itemDao.saveFound(item);
            System.out.println("Found item reported successfully! ID: " + id);
        } catch (Exception e) {
            System.out.println("Error reporting found item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewAllLostItems() {
        System.out.println("\n===== All Lost Items =====");
        try {
            List<Item> items = itemDao.listLost();
            if (items.isEmpty()) {
                System.out.println("No lost items found.");
            } else {
                displayItems(items);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving lost items: " + e.getMessage());
        }
    }

    private static void viewAllFoundItems() {
        System.out.println("\n===== All Found Items =====");
        try {
            List<Item> items = itemDao.getFoundAll();
            if (items.isEmpty()) {
                System.out.println("No found items found.");
            } else {
                displayItems(items);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving found items: " + e.getMessage());
        }
    }

    private static void searchLostItems() {
        System.out.print("\nEnter item name to search: ");
        String name = scanner.nextLine();

        try {
            List<Item> items = itemDao.searchLostByName(name);
            if (items.isEmpty()) {
                System.out.println("No matching lost items found");
            } else {
                System.out.println("===== Search Results ======");
                displayItems(items);
            }
        } catch (Exception e) {
            System.out.println("Error searching lost items: " + e.getMessage());
        }
    }

    private static void deleteLostItem() {
        viewAllLostItems();
        System.out.print("\nEnter ID of lost item to delete: ");
        int id = getIntInput();

        try {
            itemDao.deleteLost(id);
            System.out.println("Lost item deleted ");
        } catch (Exception e) {
            System.out.println("Error deleting lost item: " + e.getMessage());
        }
    }

    private static Item getItemDetailsFromUser() {
        Item item = new Item();

        System.out.print("Item Name: ");
        item.setName(scanner.nextLine());

        System.out.print("Description: ");
        item.setDescription(scanner.nextLine());

        int[] date = getValidatedDateFromUser();
        item.setYear(date[0]);
        item.setMonth(getMonthName(date[1]));
        item.setDay(date[2]);

        System.out.print("Location: ");
        item.setLocation(scanner.nextLine());

        System.out.print("Contact Info: ");
        item.setContact(scanner.nextLine());

        return item;
    }

    private static int[] getValidatedDateFromUser() {
        int year, month, day;

        while (true) {
            System.out.print("Year (1900-" + (CURRENT_YEAR + 1) + "): ");
            year = getIntInput();
            if (year >= 1900 && year <= CURRENT_YEAR + 1) {
                break;
            }
            System.out.println("❌ Invalid year! Please enter between 1900 and " + (CURRENT_YEAR + 1));
        }

        while (true) {
            System.out.print("Month (1-12): ");
            month = getIntInput();
            if (month >= 1 && month <= 12) {
                break;
            }
            System.out.println("❌ Invalid month! Please enter between 1 and 12");
        }

        while (true) {
            System.out.print("Day: ");
            day = getIntInput();
            if (isValidDay(year, month, day)) {
                break;
            }
            int maxDays = getMaxDaysInMonth(year, month);
            System.out.println("❌ Invalid day! Please enter between 1 and " + maxDays + " for " + getMonthName(month) + " " + year);
        }

        return new int[]{year, month, day};
    }

    private static boolean isValidDay(int year, int month, int day) {
        if (day < 1) return false;

        int maxDays = getMaxDaysInMonth(year, month);
        return day <= maxDays;
    }

    private static int getMaxDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    private static String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }

    private static void displayItems(List<Item> items) {
        System.out.println("┌─────┬────────────────────┬────────────────────┬──────────┬────────┬─────┬────────────────────┬────────────────────┐");
        System.out.println("│ ID  │ Name               │ Description        │ Year     │ Month  │ Day │ Location           │ Contact            │");
        System.out.println("├─────┼────────────────────┼────────────────────┼──────────┼────────┼─────┼────────────────────┼────────────────────┤");

        for (Item item : items) {
            System.out.printf("│ %-3d │ %-18s │ %-18s │ %-8d │ %-6s │ %-3d │ %-18s │ %-18s │%n",
                    item.getId(),
                    truncate(item.getName(), 18),
                    truncate(item.getDescription(), 18),
                    item.getYear(),
                    truncate(item.getMonth(), 6),
                    item.getDay(),
                    truncate(item.getLocation(), 18),
                    truncate(item.getContact(), 18));
        }
        System.out.println("└─────┴────────────────────┴────────────────────┴──────────┴────────┴─────┴────────────────────┴────────────────────┘");
    }

    private static String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}