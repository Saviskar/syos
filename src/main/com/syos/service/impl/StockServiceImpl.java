// src/main/java/main/com/syos/service/impl/StockServiceImpl.java
package main.com.syos.service.impl;

import main.com.syos.dao.BatchDao;
import main.com.syos.dao.StockDao;
import main.com.syos.dao.WebsiteInventoryDao;
import main.com.syos.model.Batch;
import main.com.syos.model.Stock;
import main.com.syos.model.WebsiteInventory;
import main.com.syos.service.StockService;
import main.com.syos.service.exception.InsufficientStockException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StockServiceImpl implements StockService {
    private final BatchDao batchDao;
    private final StockDao stockDao;
    private final WebsiteInventoryDao webInvDao;

    public StockServiceImpl(
            BatchDao batchDao,
            StockDao stockDao,
            WebsiteInventoryDao webInvDao
    ) {
        this.batchDao  = batchDao;
        this.stockDao  = stockDao;
        this.webInvDao = webInvDao;
    }

    @Override
    public void allocateShelfStock(String itemCode, int quantity) throws InsufficientStockException {
        // 1. Load all batches for this item
        List<Batch> batches = batchDao.findByItemCode(itemCode);
        if (batches.isEmpty()) {
            throw new InsufficientStockException("No shelf batches for item " + itemCode);
        }

        // 2. Sort by earliest expiry first, then by oldest receipt date
        batches.sort(Comparator
                .comparing(Batch::getExpiryDate)
                .thenComparing(Batch::getDateReceived)
        );

        int remaining = quantity;
        // 3. Walk through batches, decrementing shelf stock until fulfilled or exhausted
        for (Batch batch : batches) {
            if (remaining <= 0) break;
            List<Stock> stocks = stockDao.findByBatchId(batch.getBatchId());
            for (Stock stock : stocks) {
                if (remaining <= 0) break;
                int available = stock.getQtyOnShelf();
                if (available <= 0) continue;

                int toDeduct = Math.min(available, remaining);
                // create a new Stock object with the decremented qty
                Stock updated = new Stock(
                        stock.getStockId(),
                        stock.getBatchId(),
                        available - toDeduct
                );
                stockDao.update(updated);

                remaining -= toDeduct;
            }
        }

        if (remaining > 0) {
            throw new InsufficientStockException(
                    String.format("Insufficient shelf stock for %s: needed %d, only %d available",
                            itemCode, quantity, quantity - remaining)
            );
        }
    }

    @Override
    public void allocateWebsiteStock(String itemCode, int quantity) throws InsufficientStockException {
        Optional<WebsiteInventory> maybe = webInvDao.findByItemCode(itemCode);
        if (maybe.isEmpty()) {
            throw new InsufficientStockException("Item not available online: " + itemCode);
        }
        WebsiteInventory wi = maybe.get();
        int available = wi.getQtyAvailable();
        if (available < quantity) {
            throw new InsufficientStockException(
                    String.format("Insufficient online stock for %s: needed %d, only %d available",
                            itemCode, quantity, available)
            );
        }
        // update with decremented quantity
        WebsiteInventory updated = new WebsiteInventory(itemCode, available - quantity);
        webInvDao.update(updated);
    }
}
