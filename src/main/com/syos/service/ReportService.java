// src/main/java/main/com/syos/service/ReportService.java
package main.com.syos.service;

import main.com.syos.model.Bill;
import main.com.syos.model.Item;
import main.com.syos.model.ReshelvingLog;
import main.com.syos.model.Stock;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    /** Total sales (bill + items) for a given day */
    List<Bill> getDailySalesReport(LocalDate date);

    /** All shelf‐stock moves (reshelving) for a given day */
    List<ReshelvingLog> getDailyReshelvingReport(LocalDate date);

    /** All items whose current total stock < reorder level */
    List<Item> getReorderAlerts();

    /** Batch‐wise current shelf stock */
    List<Stock> getStockReport();

    /** All bills (OTC + online) */
    List<Bill> getAllBills();
}
