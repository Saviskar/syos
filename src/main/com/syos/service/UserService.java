// src/main/java/main/com/syos/service/UserService.java
package main.com.syos.service;

import main.com.syos.model.OnlineUser;
import main.com.syos.service.exception.AuthenticationException;
import main.com.syos.service.exception.UserAlreadyExistsException;

public interface UserService {
    /**
     * Register a new online user.
     * Throws if username is already taken.
     */
    OnlineUser register(
            String username,
            String password,
            String fullName,
            String email
    ) throws UserAlreadyExistsException;

    /**
     * Authenticate an existing user by credentials.
     * Throws if username/password mismatch.
     */
    OnlineUser authenticate(
            String username,
            String password
    ) throws AuthenticationException;
}
