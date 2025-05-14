// src/main/java/main/com/syos/service/impl/BillingServiceImpl.java
package main.com.syos.service.impl;

import main.com.syos.dao.BillDao;
import main.com.syos.dao.BillItemDao;
import main.com.syos.dao.ItemDao;
import main.com.syos.dao.OnlineUserDao;
import main.com.syos.model.Bill;
import main.com.syos.model.BillItem;
import main.com.syos.model.Item;
import main.com.syos.model.OnlineUser;
import main.com.syos.service.BillingService;
import main.com.syos.service.StockService;
import main.com.syos.service.exception.InsufficientCashException;
import main.com.syos.service.exception.InsufficientStockException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BillingServiceImpl implements BillingService {
    private final BillDao billDao;
    private final BillItemDao billItemDao;
    private final ItemDao itemDao;
    private final StockService stockService;
    private final OnlineUserDao userDao;

    public BillingServiceImpl(
            BillDao billDao,
            BillItemDao billItemDao,
            ItemDao itemDao,
            StockService stockService,
            OnlineUserDao userDao
    ) {
        this.billDao      = billDao;
        this.billItemDao  = billItemDao;
        this.itemDao      = itemDao;
        this.stockService = stockService;
        this.userDao      = userDao;
    }

    @Override
    public Bill checkoutOTC(Map<String, Integer> items, BigDecimal cashTendered)
            throws InsufficientStockException, InsufficientCashException {
        // 1. Allocate shelf stock
        for (var entry : items.entrySet()) {
            stockService.allocateShelfStock(entry.getKey(), entry.getValue());
        }

        // 2. Compute full price
        BigDecimal fullPrice = BigDecimal.ZERO;
        for (var entry : items.entrySet()) {
            String code = entry.getKey();
            int qty    = entry.getValue();
            Item item = itemDao.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("Unknown item: " + code));
            fullPrice = fullPrice.add(item.getUnitPrice().multiply(BigDecimal.valueOf(qty)));
        }

        // 3. No discount for OTC
        BigDecimal discount   = BigDecimal.ZERO;
        BigDecimal changeAmt  = cashTendered.subtract(fullPrice).subtract(discount);
        if (changeAmt.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientCashException("Cash tendered insufficient: owe " +
                    fullPrice.subtract(discount) + ", got " + cashTendered);
        }

        // 4. Assign next serial
        long nextSerial = billDao.findAll().stream()
                .map(Bill::getBillSerial)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;

        // 5. Build & save Bill
        Bill bill = new Bill(
                null,
                nextSerial,
                LocalDateTime.now(),
                "OTC",
                fullPrice,
                discount,
                cashTendered,
                changeAmt,
                null     // no online user
        );
        billDao.save(bill);

        // 6. Persist each line item
        for (var entry : items.entrySet()) {
            String code = entry.getKey();
            int qty    = entry.getValue();
            Item item = itemDao.findByCode(code).get();
            BillItem bi = new BillItem(
                    null,
                    bill.getBillId(),
                    code,
                    qty,
                    item.getUnitPrice()
            );
            billItemDao.save(bi);
        }

        return bill;
    }

    @Override
    public Bill checkoutOnline(String username, Map<String, Integer> items)
            throws InsufficientStockException {
        // 1. Lookup user
        OnlineUser user = userDao.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No such user: " + username));

        // 2. Allocate website stock
        for (var entry : items.entrySet()) {
            stockService.allocateWebsiteStock(entry.getKey(), entry.getValue());
        }

        // 3. Compute full price
        BigDecimal fullPrice = BigDecimal.ZERO;
        for (var entry : items.entrySet()) {
            Item item = itemDao.findByCode(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Unknown item: " + entry.getKey()));
            fullPrice = fullPrice.add(item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(entry.getValue())));
        }

        BigDecimal discount      = BigDecimal.ZERO;
        BigDecimal cashTendered  = BigDecimal.ZERO;
        BigDecimal changeAmt     = BigDecimal.ZERO;

        // 4. Assign next serial
        long nextSerial = billDao.findAll().stream()
                .map(Bill::getBillSerial)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;

        // 5. Build & save Bill
        Bill bill = new Bill(
                null,
                nextSerial,
                LocalDateTime.now(),
                "ONLINE",
                fullPrice,
                discount,
                cashTendered,
                changeAmt,
                user.getUserId()
        );
        billDao.save(bill);

        // 6. Persist line items
        for (var entry : items.entrySet()) {
            Item item = itemDao.findByCode(entry.getKey()).get();
            BillItem bi = new BillItem(
                    null,
                    bill.getBillId(),
                    entry.getKey(),
                    entry.getValue(),
                    item.getUnitPrice()
            );
            billItemDao.save(bi);
        }

        return bill;
    }
}
