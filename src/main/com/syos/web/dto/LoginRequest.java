// src/main/java/main/com/syos/web/dto/LoginRequest.java
package main.com.syos.web.dto;

public class LoginRequest {
    private String username, password;
    public String getUsername()   { return username; }
    public void setUsername(String u) { username = u; }
    public String getPassword()   { return password; }
    public void setPassword(String p) { password = p; }
}
