package dao;

import model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jdk.internal.vm.vector.VectorSupport.extract;

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

    public void saveLost(Item it) throws SQLException {
        String sql = "INSERT INTO lost_items(name,description,year,month,day,location,contact) VALUES (?,?,?,?,?,?,?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, it.getName());
            p.setString(2, it.getDescription());
            p.setInt(3, it.getYear());
            p.setString(4, it.getMonth());
            p.setInt(5, it.getDay());
            p.setString(6, it.getLocation());
            p.setString(7, it.getContact());

            p.executeUpdate();
        }
    }

    public List<Item> listLost() throws SQLException {
        List<Item> list = new ArrayList<>();

        String sql = "SELECT * FROM lost_items";

        try (Connection c = Database.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("year"),
                        rs.getString("month"),
                        rs.getInt("day"),
                        rs.getString("location"),
                        rs.getString("contact")
                ));
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

    public List<Item> searchLostByName(String name) throws SQLException {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT * FROM lost_items WHERE name LIKE ?";

        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, "%" + name + "%");
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    list.add(new Item(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("year"),
                            rs.getString("month"),
                            rs.getInt("day"),
                            rs.getString("location"),
                            rs.getString("contact")
                    ));
                }
            }
        }
        return list;
    }

    public void saveFound(Item item) throws SQLException {
        String sql = """
            INSERT INTO found_items(name, description, year, month, day, location, contact)
            VALUES(?,?,?,?,?,?,?)
        """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setInt(3, item.getYear());
            ps.setString(4, item.getMonth());
            ps.setInt(5, item.getDay());
            ps.setString(6, item.getLocation());
            ps.setString(7, item.getContact());
            ps.executeUpdate();
        }
    }

    public List<Item> getFoundAll() throws SQLException {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT * FROM found_items";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(extract(rs));
        }
        return list;
    }
}
