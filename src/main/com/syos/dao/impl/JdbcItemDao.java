package main.com.syos.dao.impl;

import main.com.syos.dao.ItemDao;
import main.com.syos.model.Item;
import main.com.syos.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcItemDao implements ItemDao {
    private final DbConnectionManager dbManager;

    public JdbcItemDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Optional<Item> findByCode(String itemCode) {
        String sql = "SELECT item_code, name, unit_price, reorder_level FROM Item WHERE item_code = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemCode);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Item item = new Item(
                            rs.getString("item_code"),
                            rs.getString("name"),
                            rs.getBigDecimal("unit_price"),
                            rs.getInt("reorder_level")
                    );
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying Item by code", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Item> findAll() {
        String sql = "SELECT item_code, name, unit_price, reorder_level FROM Item";
        List<Item> items = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new Item(
                        rs.getString("item_code"),
                        rs.getString("name"),
                        rs.getBigDecimal("unit_price"),
                        rs.getInt("reorder_level")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error querying all Items", e);
        }
        return items;
    }

    @Override
    public void save(Item item) {
        String sql = "INSERT INTO Item(item_code, name, unit_price, reorder_level) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemCode());
            ps.setString(2, item.getName());
            ps.setBigDecimal(3, item.getUnitPrice());
            ps.setInt(4, item.getReorderLevel());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Item", e);
        }
    }

    @Override
    public void update(Item item) {
        String sql = "UPDATE Item SET name = ?, unit_price = ?, reorder_level = ? "
                + "WHERE item_code = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setBigDecimal(2, item.getUnitPrice());
            ps.setInt(3, item.getReorderLevel());
            ps.setString(4, item.getItemCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Item", e);
        }
    }

    @Override
    public void delete(String itemCode) {
        String sql = "DELETE FROM Item WHERE item_code = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Item", e);
        }
    }
}




















