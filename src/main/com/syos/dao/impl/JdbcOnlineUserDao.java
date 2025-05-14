// src/main/java/main/com/syos/dao/impl/JdbcOnlineUserDao.java
package main.com.syos.dao.impl;

import main.com.syos.dao.OnlineUserDao;
import main.com.syos.model.OnlineUser;
import main.com.syos.util.DbConnectionManager;

import java.sql.*;
import java.util.Optional;

public class JdbcOnlineUserDao implements OnlineUserDao {
    private final DbConnectionManager dbManager;

    public JdbcOnlineUserDao(DbConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void save(OnlineUser user) {
        String sql = """
            INSERT INTO OnlineUser(username, password_hash, full_name, email)
            VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setUserId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving OnlineUser", e);
        }
    }

    @Override
    public Optional<OnlineUser> findById(Long userId) {
        String sql = """
            SELECT user_id, username, password_hash, full_name, email
              FROM OnlineUser
             WHERE user_id = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new OnlineUser(
                            rs.getLong("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("full_name"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying OnlineUser by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<OnlineUser> findByUsername(String username) {
        String sql = """
            SELECT user_id, username, password_hash, full_name, email
              FROM OnlineUser
             WHERE username = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new OnlineUser(
                            rs.getLong("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("full_name"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying OnlineUser by username", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(OnlineUser user) {
        String sql = """
            UPDATE OnlineUser
               SET password_hash = ?, full_name = ?, email = ?
             WHERE user_id = ?
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPasswordHash());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setLong(4, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating OnlineUser", e);
        }
    }
}
