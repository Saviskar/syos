// src/main/java/main/com/syos/dao/impl/JdbcBillItemDao.java
package main.com.syos.dao.impl;

import main.com.syos.dao.BillItemDao;
import main.com.syos.model.BillItem;
import main.com.syos.util.DbConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcBillItemDao implements BillItemDao {
    private final DbConnectionManager dbManager;

    public JdbcBillItemDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void save(BillItem bi) {
        String sql = """
            INSERT INTO BillItem(bill_id, item_code, quantity, price_each)
            VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, bi.getBillId());
            ps.setString(2, bi.getItemCode());
            ps.setInt(3, bi.getQuantity());
            ps.setBigDecimal(4, bi.getPriceEach());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    bi.setBillItemId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving BillItem", e);
        }
    }

    @Override
    public List<BillItem> findByBillId(Long billId) {
        String sql = """
            SELECT bill_item_id, bill_id, item_code, quantity, price_each
              FROM BillItem
             WHERE bill_id = ?
            """;
        List<BillItem> items = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new BillItem(
                            rs.getLong("bill_item_id"),
                            rs.getLong("bill_id"),
                            rs.getString("item_code"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("price_each")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying BillItems by billId", e);
        }
        return items;
    }

    @Override
    public void deleteByBillId(Long billId) {
        String sql = "DELETE FROM BillItem WHERE bill_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, billId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting BillItems for billId", e);
        }
    }
}
