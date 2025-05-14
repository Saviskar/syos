package main.com.syos.model;

public class WebsiteInventory {
    private final String itemCode;
    private final int qtyAvailable;

    public WebsiteInventory(String itemCode, int qtyAvailable) {
        this.itemCode     = itemCode;
        this.qtyAvailable = qtyAvailable;
    }

    public String getItemCode()    { return itemCode; }
    public int getQtyAvailable()   { return qtyAvailable; }
}
