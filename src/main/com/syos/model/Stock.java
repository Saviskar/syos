package main.com.syos.model;

public class Stock {
    private Long stockId;
    private final Long batchId;
    private final int qtyOnShelf;

    public Stock(Long stockId, Long batchId, int qtyOnShelf) {
        this.stockId    = stockId;
        this.batchId    = batchId;
        this.qtyOnShelf = qtyOnShelf;
    }

    public Long getStockId()    { return stockId; }
    public Long getBatchId()    { return batchId; }
    public int getQtyOnShelf()  { return qtyOnShelf; }

    public void setStockId(Long stockId) { this.stockId = stockId; }
}
