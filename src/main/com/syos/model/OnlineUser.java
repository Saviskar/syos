package main.com.syos.model;

public class OnlineUser {
    private Long userId;
    private final String username;
    private final String passwordHash;
    private final String fullName;
    private final String email;

    public OnlineUser(Long userId, String username, String passwordHash, String fullName, String email) {
        this.userId       = userId;
        this.username     = username;
        this.passwordHash = passwordHash;
        this.fullName     = fullName;
        this.email        = email;
    }

    public Long getUserId()         { return userId; }
    public String getUsername()     { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName()     { return fullName; }
    public String getEmail()        { return email; }

    public void setUserId(Long userId) { this.userId = userId; }
}
