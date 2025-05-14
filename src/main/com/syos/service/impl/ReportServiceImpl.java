// src/main/java/main/com/syos/service/impl/ReportServiceImpl.java
package main.com.syos.service.impl;

import main.com.syos.dao.*;
import main.com.syos.model.*;
import main.com.syos.service.ReportService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl implements ReportService {
    private final BillDao billDao;
    private final ReshelvingLogDao reshelvingLogDao;
    private final ItemDao itemDao;
    private final BatchDao batchDao;
    private final StockDao stockDao;

    public ReportServiceImpl(
            BillDao billDao,
            ReshelvingLogDao reshelvingLogDao,
            ItemDao itemDao,
            BatchDao batchDao,
            StockDao stockDao
    ) {
        this.billDao = billDao;
        this.reshelvingLogDao = reshelvingLogDao;
        this.itemDao = itemDao;
        this.batchDao = batchDao;
        this.stockDao = stockDao;
    }

    @Override
    public List<Bill> getDailySalesReport(LocalDate date) {
        // All bills for that day
        return billDao.findByDate(date);
    }

    @Override
    public List<ReshelvingLog> getDailyReshelvingReport(LocalDate date) {
        // All shelf‐stock moves for that day
        return reshelvingLogDao.findByDate(date);
    }

    @Override
    public List<Item> getReorderAlerts() {
        // Items whose total shelf stock < reorderLevel
        List<Item> alerts = new ArrayList<>();
        for (Item item : itemDao.findAll()) {
            // Sum up all shelf stock for this item
            int totalOnShelf = batchDao.findByItemCode(item.getItemCode()).stream()
                    .mapToInt(batch -> stockDao.findByBatchId(batch.getBatchId()).stream()
                            .mapToInt(Stock::getQtyOnShelf).sum()
                    ).sum();

            if (totalOnShelf < item.getReorderLevel()) {
                alerts.add(item);
            }
        }
        return alerts;
    }

    @Override
    public List<Stock> getStockReport() {
        // Batch‐wise shelf stock
        return stockDao.findAll();
    }

    @Override
    public List<Bill> getAllBills() {
        // All transactions
        return billDao.findAll();
    }
}
