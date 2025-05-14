// src/main/java/main/com/syos/web/dto/RegisterRequest.java
package main.com.syos.web.dto;

public class RegisterRequest {
    private String username, password, fullName, email;
    public String getUsername()   { return username; }
    public void setUsername(String u) { username = u; }
    public String getPassword()   { return password; }
    public void setPassword(String p) { password = p; }
    public String getFullName()   { return fullName; }
    public void setFullName(String f) { fullName = f; }
    public String getEmail()      { return email; }
    public void setEmail(String e) { email = e; }
}
