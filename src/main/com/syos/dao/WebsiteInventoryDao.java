// src/main/java/main/com/syos/dao/WebsiteInventoryDao.java
package main.com.syos.dao;

import main.com.syos.model.WebsiteInventory;
import java.util.Optional;

public interface WebsiteInventoryDao {
    /** Lookup current online stock for a given item */
    Optional<WebsiteInventory> findByItemCode(String itemCode);

    /** Initialize website inventory for a new item (if needed) */
    void save(WebsiteInventory wi);

    /** Update the available quantity after an online sale or restock */
    void update(WebsiteInventory wi);
}
