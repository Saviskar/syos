// src/main/java/main/com/syos/service/StockService.java
package main.com.syos.service;

import main.com.syos.service.exception.InsufficientStockException;

public interface StockService {
    /**
     * Allocate (decrement) quantity from shelf stock batches for a given item.
     * Throws if available shelf stock < requested quantity.
     */
    void allocateShelfStock(String itemCode, int quantity) throws InsufficientStockException;

    /**
     * Allocate (decrement) quantity from website inventory for a given item.
     * Throws if available website stock < requested quantity.
     */
    void allocateWebsiteStock(String itemCode, int quantity) throws InsufficientStockException;
}
