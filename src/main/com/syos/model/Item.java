package main.com.syos.model;

import java.math.BigDecimal;

public class Item {
    private final String itemCode;
    private final String name;
    private final BigDecimal unitPrice;
    private final int reorderLevel;

    public Item(String itemCode, String name, BigDecimal unitPrice, int reorderLevel) {
        this.itemCode    = itemCode;
        this.name        = name;
        this.unitPrice   = unitPrice;
        this.reorderLevel = reorderLevel;
    }

    public String getItemCode() { return itemCode; }
    public String getName()     { return name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getReorderLevel()      { return reorderLevel; }
}
