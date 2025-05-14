// src/main/java/main/com/syos/web/dto/CheckoutRequest.java
package main.com.syos.web.dto;

import java.math.BigDecimal;
import java.util.Map;

public class CheckoutRequest {
    private Map<String,Integer> items;
    private BigDecimal cashTendered;
    public Map<String,Integer> getItems()        { return items; }
    public void setItems(Map<String,Integer> i)  { items = i; }
    public BigDecimal getCashTendered()          { return cashTendered; }
    public void setCashTendered(BigDecimal c)    { cashTendered = c; }
}
