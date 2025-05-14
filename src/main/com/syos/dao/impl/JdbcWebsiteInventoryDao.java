// src/main/java/main/com/syos/dao/impl/JdbcWebsiteInventoryDao.java
package main.com.syos.dao.impl;

import main.com.syos.dao.WebsiteInventoryDao;
import main.com.syos.model.WebsiteInventory;
import main.com.syos.util.DbConnectionManager;

import java.sql.*;
import java.util.Optional;

public class JdbcWebsiteInventoryDao implements WebsiteInventoryDao {
    private final DbConnectionManager dbManager;

    public JdbcWebsiteInventoryDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Optional<WebsiteInventory> findByItemCode(String itemCode) {
        String sql = """
            SELECT item_code, qty_available
              FROM WebsiteInventory
             WHERE item_code = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new WebsiteInventory(
                            rs.getString("item_code"),
                            rs.getInt("qty_available")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying WebsiteInventory", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(WebsiteInventory wi) {
        String sql = """
            INSERT INTO WebsiteInventory(item_code, qty_available)
            VALUES (?, ?)
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, wi.getItemCode());
            ps.setInt(2, wi.getQtyAvailable());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving WebsiteInventory", e);
        }
    }

    @Override
    public void update(WebsiteInventory wi) {
        String sql = """
            UPDATE WebsiteInventory
               SET qty_available = ?
             WHERE item_code = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, wi.getQtyAvailable());
            ps.setString(2, wi.getItemCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating WebsiteInventory", e);
        }
    }
}
