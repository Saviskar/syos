package main.com.syos.model;

import java.math.BigDecimal;

public class BillItem {
    private Long billItemId;
    private final Long billId;
    private final String itemCode;
    private final int quantity;
    private final BigDecimal priceEach;

    public BillItem(Long billItemId, Long billId, String itemCode, int quantity, BigDecimal priceEach) {
        this.billItemId = billItemId;
        this.billId     = billId;
        this.itemCode   = itemCode;
        this.quantity   = quantity;
        this.priceEach  = priceEach;
    }

    public Long getBillItemId() { return billItemId; }
    public Long getBillId()     { return billId; }
    public String getItemCode() { return itemCode; }
    public int getQuantity()    { return quantity; }
    public BigDecimal getPriceEach() { return priceEach; }

    public void setBillItemId(Long billItemId) { this.billItemId = billItemId; }
}
