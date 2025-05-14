// src/main/java/main/com/syos/dao/BillItemDao.java
package main.com.syos.dao;

import main.com.syos.model.BillItem;
import java.util.List;

public interface BillItemDao {
    void save(BillItem billItem);
    List<BillItem> findByBillId(Long billId);
    void deleteByBillId(Long billId);
}
