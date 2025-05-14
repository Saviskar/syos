// src/main/java/main/com/syos/web/dto/OnlineCheckoutRequest.java
package main.com.syos.web.dto;

import java.util.Map;

public class OnlineCheckoutRequest {
    private String username;
    private Map<String,Integer> items;
    public String getUsername()                 { return username; }
    public void setUsername(String u)           { username = u; }
    public Map<String,Integer> getItems()       { return items; }
    public void setItems(Map<String,Integer> i) { items = i; }
}

