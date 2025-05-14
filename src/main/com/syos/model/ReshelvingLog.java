package main.com.syos.model;

import java.time.LocalDateTime;

public class ReshelvingLog {
    private Long logId;
    private final String itemCode;
    private final Long batchId;
    private final int qtyMoved;
    private final LocalDateTime movedAt;

    public ReshelvingLog(Long logId, String itemCode, Long batchId, int qtyMoved, LocalDateTime movedAt) {
        this.logId     = logId;
        this.itemCode  = itemCode;
        this.batchId   = batchId;
        this.qtyMoved  = qtyMoved;
        this.movedAt   = movedAt;
    }

    public Long getLogId()          { return logId; }
    public String getItemCode()     { return itemCode; }
    public Long getBatchId()        { return batchId; }
    public int getQtyMoved()        { return qtyMoved; }
    public LocalDateTime getMovedAt(){ return movedAt; }

    public void setLogId(Long logId) { this.logId = logId; }
}
