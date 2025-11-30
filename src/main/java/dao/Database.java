package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

public class Database {

    private static String getDatabasePath() {
        try {
            String userHome = System.getProperty("user.home");
            Path appDir = Paths.get(userHome, "LostFoundApp");

            if (!Files.exists(appDir)) {
                Files.createDirectories(appDir);
            }

            return appDir.resolve("lostfound.db").toString();
        } catch (Exception e) {
            // Fallback to current directory
            return "lostfound.db";
        }
    }

    private static final String URL = "jdbc:sqlite:" + getDatabasePath();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String createLost = """
                CREATE TABLE IF NOT EXISTS lost_items(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    year INTEGER,
                    month TEXT,
                    day INTEGER,
                    location TEXT,
                    contact TEXT
                );
                """;

        String createFound = """
                CREATE TABLE IF NOT EXISTS found_items(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    year INTEGER,
                    month TEXT,
                    day INTEGER,
                    location TEXT,
                    contact TEXT
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createLost);
            stmt.execute(createFound);
            System.out.println("Database initialized at: " + getDatabasePath());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getDatabaseLocation() {
        return getDatabasePath();
    }
}