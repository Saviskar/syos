// src/main/java/main/com/syos/dao/impl/JdbcBillDao.java
package main.com.syos.dao.impl;

import main.com.syos.dao.BillDao;
import main.com.syos.model.Bill;
import main.com.syos.util.DbConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBillDao implements BillDao {
    private final DbConnectionManager dbManager;

    public JdbcBillDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void save(Bill bill) {
        String sql = """
            INSERT INTO Bill
              (bill_serial, bill_date, type, full_price, discount, cash_tendered, change_amt, online_user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, bill.getBillSerial());
            ps.setTimestamp(2, Timestamp.valueOf(bill.getBillDate()));
            ps.setString(3, bill.getType());               // type is a String in your model
            ps.setBigDecimal(4, bill.getFullPrice());
            ps.setBigDecimal(5, bill.getDiscount());
            ps.setBigDecimal(6, bill.getCashTendered());
            ps.setBigDecimal(7, bill.getChangeAmt());
            if (bill.getOnlineUserId() != null) {
                ps.setLong(8, bill.getOnlineUserId());
            } else {
                ps.setNull(8, Types.BIGINT);
            }

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bill.setBillId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Bill", e);
        }
    }

    @Override
    public Optional<Bill> findById(Long billId) {
        String sql = """
            SELECT bill_id, bill_serial, bill_date, type, full_price,
                   discount, cash_tendered, change_amt, online_user_id
              FROM Bill
             WHERE bill_id = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToBill(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying Bill by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Bill> findByDate(LocalDate date) {
        String sql = """
            SELECT bill_id, bill_serial, bill_date, type, full_price,
                   discount, cash_tendered, change_amt, online_user_id
              FROM Bill
             WHERE DATE(bill_date) = ?
            """;
        List<Bill> results = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToBill(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying Bills by date", e);
        }
        return results;
    }

    @Override
    public List<Bill> findAll() {
        String sql = """
            SELECT bill_id, bill_serial, bill_date, type, full_price,
                   discount, cash_tendered, change_amt, online_user_id
              FROM Bill
            """;
        List<Bill> results = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(mapRowToBill(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying all Bills", e);
        }
        return results;
    }

    @Override
    public void update(Bill bill) {
        String sql = """
            UPDATE Bill
               SET bill_serial = ?, bill_date = ?, type = ?, full_price = ?,
                   discount = ?, cash_tendered = ?, change_amt = ?, online_user_id = ?
             WHERE bill_id = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bill.getBillSerial());
            ps.setTimestamp(2, Timestamp.valueOf(bill.getBillDate()));
            ps.setString(3, bill.getType());
            ps.setBigDecimal(4, bill.getFullPrice());
            ps.setBigDecimal(5, bill.getDiscount());
            ps.setBigDecimal(6, bill.getCashTendered());
            ps.setBigDecimal(7, bill.getChangeAmt());
            if (bill.getOnlineUserId() != null) {
                ps.setLong(8, bill.getOnlineUserId());
            } else {
                ps.setNull(8, Types.BIGINT);
            }
            ps.setLong(9, bill.getBillId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Bill", e);
        }
    }

    @Override
    public void delete(Long billId) {
        String sql = "DELETE FROM Bill WHERE bill_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, billId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Bill", e);
        }
    }

    //-------------------------------------------------------------------------
    // Internal helper to reduce duplication
    private Bill mapRowToBill(ResultSet rs) throws SQLException {
        return new Bill(
                rs.getLong("bill_id"),
                rs.getLong("bill_serial"),
                rs.getTimestamp("bill_date").toLocalDateTime(),
                rs.getString("type"),
                rs.getBigDecimal("full_price"),
                rs.getBigDecimal("discount"),
                rs.getBigDecimal("cash_tendered"),
                rs.getBigDecimal("change_amt"),
                rs.getObject("online_user_id") != null ? rs.getLong("online_user_id") : null
        );
    }
}
