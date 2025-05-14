// src/main/java/main/com/syos/dao/impl/JdbcStockDao.java
package main.com.syos.dao.impl;

import main.com.syos.dao.StockDao;
import main.com.syos.model.Stock;
import main.com.syos.util.DbConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcStockDao implements StockDao {
    private final DbConnectionManager dbManager;

    public JdbcStockDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public List<Stock> findByBatchId(Long batchId) {
        String sql = """
            SELECT stock_id, batch_id, qty_on_shelf
              FROM Stock
             WHERE batch_id = ?
            """;
        List<Stock> list = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, batchId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Stock(
                            rs.getLong("stock_id"),
                            rs.getLong("batch_id"),
                            rs.getInt("qty_on_shelf")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying Stock by batchId", e);
        }
        return list;
    }

    @Override
    public List<Stock> findAll() {
        String sql = """
            SELECT stock_id, batch_id, qty_on_shelf
              FROM Stock
            """;
        List<Stock> list = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Stock(
                        rs.getLong("stock_id"),
                        rs.getLong("batch_id"),
                        rs.getInt("qty_on_shelf")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying all Stock", e);
        }
        return list;
    }

    @Override
    public void save(Stock stock) {
        String sql = """
            INSERT INTO Stock(batch_id, qty_on_shelf)
            VALUES (?, ?)
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, stock.getBatchId());
            ps.setInt(2, stock.getQtyOnShelf());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    stock.setStockId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Stock", e);
        }
    }

    @Override
    public void update(Stock stock) {
        String sql = """
            UPDATE Stock
               SET qty_on_shelf = ?
             WHERE stock_id = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stock.getQtyOnShelf());
            ps.setLong(2, stock.getStockId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Stock", e);
        }
    }
}
