// src/main/java/main/com/syos/dao/StockDao.java
package main.com.syos.dao;

import main.com.syos.model.Stock;
import java.util.List;

public interface StockDao {
    List<Stock> findByBatchId(Long batchId);
    List<Stock> findAll();
    void save(Stock stock);
    void update(Stock stock);
}
