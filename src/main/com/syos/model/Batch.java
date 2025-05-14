package main.com.syos.model;

import java.time.LocalDate;

public class Batch {
    private Long batchId;
    private final String itemCode;
    private final LocalDate dateReceived;
    private final int qtyReceived;
    private final LocalDate expiryDate;

    public Batch(Long batchId, String itemCode, LocalDate dateReceived, int qtyReceived, LocalDate expiryDate) {
        this.batchId      = batchId;
        this.itemCode     = itemCode;
        this.dateReceived = dateReceived;
        this.qtyReceived  = qtyReceived;
        this.expiryDate   = expiryDate;
    }

    public Long getBatchId()         { return batchId; }
    public String getItemCode()      { return itemCode; }
    public LocalDate getDateReceived(){ return dateReceived; }
    public int getQtyReceived()      { return qtyReceived; }
    public LocalDate getExpiryDate() { return expiryDate; }

    public void setBatchId(Long batchId) { this.batchId = batchId; }
}
