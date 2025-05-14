// src/main/java/main/com/syos/dao/OnlineUserDao.java
package main.com.syos.dao;

import main.com.syos.model.OnlineUser;
import java.util.Optional;

public interface OnlineUserDao {
    void save(OnlineUser user);
    Optional<OnlineUser> findById(Long userId);
    Optional<OnlineUser> findByUsername(String username);
    void update(OnlineUser user);
}
