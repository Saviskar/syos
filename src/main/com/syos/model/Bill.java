package main.com.syos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bill {
    private Long billId;
    private final Long billSerial;
    private final LocalDateTime billDate;
    private final String type;         // "OTC" or "ONLINE"
    private final BigDecimal fullPrice;
    private final BigDecimal discount;
    private final BigDecimal cashTendered;
    private final BigDecimal changeAmt;
    private final Long onlineUserId;   // null for OTC

    public Bill(Long billId, Long billSerial, LocalDateTime billDate, String type,
                BigDecimal fullPrice, BigDecimal discount,
                BigDecimal cashTendered, BigDecimal changeAmt, Long onlineUserId) {
        this.billId        = billId;
        this.billSerial    = billSerial;
        this.billDate      = billDate;
        this.type          = type;
        this.fullPrice     = fullPrice;
        this.discount      = discount;
        this.cashTendered  = cashTendered;
        this.changeAmt     = changeAmt;
        this.onlineUserId  = onlineUserId;
    }

    public Long getBillId()           { return billId; }
    public Long getBillSerial()       { return billSerial; }
    public LocalDateTime getBillDate(){ return billDate; }
    public String getType()           { return type; }
    public BigDecimal getFullPrice()  { return fullPrice; }
    public BigDecimal getDiscount()   { return discount; }
    public BigDecimal getCashTendered(){ return cashTendered; }
    public BigDecimal getChangeAmt()  { return changeAmt; }
    public Long getOnlineUserId()     { return onlineUserId; }

    public void setBillId(Long billId) { this.billId = billId; }
}
