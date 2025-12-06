package dao;

import model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    private Item extract(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setYear(rs.getInt("year"));
        item.setMonth(rs.getString("month"));
        item.setDay(rs.getInt("day"));
        item.setLocation(rs.getString("location"));
        item.setContact(rs.getString("contact"));
        return item;
    }

    public List<Item> searchLostFlexible(
            String name, String desc, Integer year, String month, Integer day,
            String location, String contact) throws SQLException {

        String sql =
                "SELECT * FROM lost_items WHERE "
                        + " (? IS NULL OR LOWER(name) LIKE LOWER(?)) "
                        + " AND (? IS NULL OR LOWER(description) LIKE LOWER(?)) "
                        + " AND (? IS NULL OR year = ?) "
                        + " AND (? IS NULL OR LOWER(month) = LOWER(?)) "
                        + " AND (? IS NULL OR day = ?) "
                        + " AND (? IS NULL OR LOWER(location) LIKE LOWER(?)) "
                        + " AND (? IS NULL OR LOWER(contact) LIKE LOWER(?)) ";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, name);
            ps.setObject(2, name != null ? "%" + name + "%" : null);

            ps.setObject(3, desc);
            ps.setObject(4, desc != null ? "%" + desc + "%" : null);

            ps.setObject(5, year);
            ps.setObject(6, year);

            ps.setObject(7, month);
            ps.setObject(8, month);

            ps.setObject(9, day);
            ps.setObject(10, day);

            ps.setObject(11, location);
            ps.setObject(12, location != null ? "%" + location + "%" : null);

            ps.setObject(13, contact);
            ps.setObject(14, contact != null ? "%" + contact + "%" : null);

            List<Item> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {list.add(extract(rs));}
            }
            return list;
        }
    }

    public List<Item> searchFoundFlexible(
            String name, String desc, Integer year, String month, Integer day,
            String location, String contact) throws SQLException {

        String sql =
                "SELECT * FROM found_items WHERE "
                        + " (? IS NULL OR LOWER(name) LIKE LOWER(?)) "
                        + " AND (? IS NULL OR LOWER(description) LIKE LOWER(?)) "
                        + " AND (? IS NULL OR year = ?) "
                        + " AND (? IS NULL OR LOWER(month) = LOWER(?)) "
                        + " AND (? IS NULL OR day = ?) "
                        + " AND (? IS NULL OR LOWER(location) LIKE LOWER(?)) "
                        + " AND (? IS NULL OR LOWER(contact) LIKE LOWER(?)) ";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, name);
            ps.setObject(2, name != null ? "%" + name + "%" : null);

            ps.setObject(3, desc);
            ps.setObject(4, desc != null ? "%" + desc + "%" : null);

            ps.setObject(5, year);
            ps.setObject(6, year);

            ps.setObject(7, month);
            ps.setObject(8, month);

            ps.setObject(9, day);
            ps.setObject(10, day);

            ps.setObject(11, location);
            ps.setObject(12, location != null ? "%" + location + "%" : null);

            ps.setObject(13, contact);
            ps.setObject(14, contact != null ? "%" + contact + "%" : null);

            List<Item> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
            return list;
        }
    }




    public int saveLost(Item it) throws SQLException {
        String sql = "INSERT INTO lost_items(name,description,year,month,day,location,contact) VALUES (?,?,?,?,?,?,?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            p.setString(1, it.getName());
            p.setString(2, it.getDescription());
            p.setInt(3, it.getYear());
            p.setString(4, it.getMonth());
            p.setInt(5, it.getDay());
            p.setString(6, it.getLocation());
            p.setString(7, it.getContact());

            p.executeUpdate();

            try (ResultSet generatedKeys = p.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getInt(1);
            }
        }
        return -1;
    }

    public List<Item> getLostAll() throws SQLException {
        List<Item> list = new ArrayList<>();

        String sql = "SELECT * FROM lost_items";

        try (Connection c = Database.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    public void deleteLost(int id) throws SQLException {
        String sql = "DELETE FROM lost_items WHERE id = ?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            p.executeUpdate();
        }
    }


    public int saveFound(Item item) throws SQLException {
        String sql =
                "INSERT INTO found_items(name, description, year, month, day, location, contact) VALUES(?,?,?,?,?,?,?)";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setInt(3, item.getYear());
            ps.setString(4, item.getMonth());
            ps.setInt(5, item.getDay());
            ps.setString(6, item.getLocation());
            ps.setString(7, item.getContact());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getInt(1);
            }
        }
        return -1;
    }

    public List<Item> getFoundAll() throws SQLException {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT * FROM found_items";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    public void deleteFound(int id) throws SQLException {
        String sql = "DELETE FROM found_items WHERE id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            p.executeUpdate();
        }
    }
}
