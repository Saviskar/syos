// src/main/java/main/com/syos/dao/impl/JdbcBatchDao.java
package main.com.syos.dao.impl;

import main.com.syos.dao.BatchDao;
import main.com.syos.model.Batch;
import main.com.syos.util.DbConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBatchDao implements BatchDao {
    private final DbConnectionManager dbManager;

    public JdbcBatchDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Optional<Batch> findById(Long batchId) {
        String sql = """
            SELECT batch_id, item_code, date_received, qty_received, expiry_date
              FROM Batch
             WHERE batch_id = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, batchId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Batch(
                            rs.getLong("batch_id"),
                            rs.getString("item_code"),
                            rs.getDate("date_received").toLocalDate(),
                            rs.getInt("qty_received"),
                            rs.getDate("expiry_date").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying Batch by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Batch> findByItemCode(String itemCode) {
        String sql = """
            SELECT batch_id, item_code, date_received, qty_received, expiry_date
              FROM Batch
             WHERE item_code = ?
            """;
        List<Batch> batches = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    batches.add(new Batch(
                            rs.getLong("batch_id"),
                            rs.getString("item_code"),
                            rs.getDate("date_received").toLocalDate(),
                            rs.getInt("qty_received"),
                            rs.getDate("expiry_date").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying Batches by itemCode", e);
        }
        return batches;
    }

    @Override
    public List<Batch> findAll() {
        String sql = """
            SELECT batch_id, item_code, date_received, qty_received, expiry_date
              FROM Batch
            """;
        List<Batch> batches = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                batches.add(new Batch(
                        rs.getLong("batch_id"),
                        rs.getString("item_code"),
                        rs.getDate("date_received").toLocalDate(),
                        rs.getInt("qty_received"),
                        rs.getDate("expiry_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying all Batches", e);
        }
        return batches;
    }

    @Override
    public void save(Batch batch) {
        String sql = """
            INSERT INTO Batch(item_code, date_received, qty_received, expiry_date)
            VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, batch.getItemCode());
            ps.setDate(2, Date.valueOf(batch.getDateReceived()));
            ps.setInt(3, batch.getQtyReceived());
            ps.setDate(4, Date.valueOf(batch.getExpiryDate()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    batch.setBatchId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Batch", e);
        }
    }

    @Override
    public void update(Batch batch) {
        String sql = """
            UPDATE Batch
               SET item_code = ?, date_received = ?, qty_received = ?, expiry_date = ?
             WHERE batch_id = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, batch.getItemCode());
            ps.setDate(2, Date.valueOf(batch.getDateReceived()));
            ps.setInt(3, batch.getQtyReceived());
            ps.setDate(4, Date.valueOf(batch.getExpiryDate()));
            ps.setLong(5, batch.getBatchId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Batch", e);
        }
    }

    @Override
    public void delete(Long batchId) {
        String sql = "DELETE FROM Batch WHERE batch_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, batchId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Batch", e);
        }
    }
}
