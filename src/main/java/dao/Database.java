package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:lostfound.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String createLost = """
                CREATE TABLE IF NOT EXISTS lost_items(
                    id INTEGER PRIMARY KEY,
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
                    id INTEGER PRIMARY KEY,
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
