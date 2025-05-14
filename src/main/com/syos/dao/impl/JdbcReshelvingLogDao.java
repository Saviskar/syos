// src/main/java/main/com/syos/dao/impl/JdbcReshelvingLogDao.java
package main.com.syos.dao.impl;

import main.com.syos.dao.ReshelvingLogDao;
import main.com.syos.model.ReshelvingLog;
import main.com.syos.util.DbConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReshelvingLogDao implements ReshelvingLogDao {
    private final DbConnectionManager dbManager;

    public JdbcReshelvingLogDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void save(ReshelvingLog log) {
        String sql = """
            INSERT INTO ReshelvingLog(item_code, batch_id, qty_moved)
            VALUES (?, ?, ?)
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, log.getItemCode());
            ps.setLong(2, log.getBatchId());
            ps.setInt(3, log.getQtyMoved());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    log.setLogId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving ReshelvingLog", e);
        }
    }

    @Override
    public List<ReshelvingLog> findByDate(LocalDate date) {
        String sql = """
            SELECT log_id, item_code, batch_id, qty_moved, moved_at
              FROM ReshelvingLog
             WHERE DATE(moved_at) = ?
            """;
        List<ReshelvingLog> logs = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    logs.add(new ReshelvingLog(
                            rs.getLong("log_id"),
                            rs.getString("item_code"),
                            rs.getLong("batch_id"),
                            rs.getInt("qty_moved"),
                            rs.getTimestamp("moved_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying ReshelvingLog by date", e);
        }
        return logs;
    }
}
