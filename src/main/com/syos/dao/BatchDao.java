// src/main/java/main/com/syos/dao/BatchDao.java
package main.com.syos.dao;

import main.com.syos.model.Batch;

import java.util.List;
import java.util.Optional;

public interface BatchDao {
    Optional<Batch> findById(Long batchId);
    List<Batch> findByItemCode(String itemCode);
    List<Batch> findAll();
    void save(Batch batch);
    void update(Batch batch);
    void delete(Long batchId);
}
