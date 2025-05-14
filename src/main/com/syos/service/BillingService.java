// src/main/java/main/com/syos/service/BillingService.java
package main.com.syos.service;

import main.com.syos.model.Bill;
import main.com.syos.service.exception.InsufficientCashException;
import main.com.syos.service.exception.InsufficientStockException;

import java.math.BigDecimal;
import java.util.Map;

public interface BillingService {
    /**
     * Perform an over‐the‐counter checkout:
     * - Allocate stock from shelves
     * - Calculate full price, discount, and change
     * - Persist Bill and BillItems
     */
    Bill checkoutOTC(
            Map<String, Integer> items,
            BigDecimal cashTendered
    ) throws InsufficientStockException, InsufficientCashException;

    /**
     * Perform an online checkout for a registered user:
     * - Allocate stock from website inventory
     * - Persist Bill and BillItems
     */
    Bill checkoutOnline(
            String username,
            Map<String, Integer> items
    ) throws InsufficientStockException;
}
