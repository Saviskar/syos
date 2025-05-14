// src/main/java/main/com/syos/service/exception/UserAlreadyExistsException.java
package main.com.syos.service.exception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
