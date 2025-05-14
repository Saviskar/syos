// src/main/java/main/com/syos/dao/ReshelvingLogDao.java
package main.com.syos.dao;

import main.com.syos.model.ReshelvingLog;
import java.time.LocalDate;
import java.util.List;

public interface ReshelvingLogDao {
    /** Record a move from back‐store to shelf */
    void save(ReshelvingLog log);

    /** Retrieve today's moves for the end-of-day reshelving report */
    List<ReshelvingLog> findByDate(LocalDate date);
}
