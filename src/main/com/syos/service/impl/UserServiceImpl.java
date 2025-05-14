// src/main/java/main/com/syos/service/impl/UserServiceImpl.java
package main.com.syos.service.impl;

import main.com.syos.dao.OnlineUserDao;
import main.com.syos.model.OnlineUser;
import main.com.syos.service.UserService;
import main.com.syos.service.exception.AuthenticationException;
import main.com.syos.service.exception.UserAlreadyExistsException;
import main.com.syos.util.PasswordUtil;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final OnlineUserDao userDao;

    public UserServiceImpl(OnlineUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public OnlineUser register(String username, String password, String fullName, String email)
            throws UserAlreadyExistsException {
        // 1. Check for existing user
        Optional<OnlineUser> existing = userDao.findByUsername(username);
        if (existing.isPresent()) {
            throw new UserAlreadyExistsException("Username already taken: " + username);
        }

        // 2. Hash password and create user
        String hashed = PasswordUtil.hash(password);
        OnlineUser user = new OnlineUser(null, username, hashed, fullName, email);

        // 3. Persist and return
        userDao.save(user);
        return user;
    }

    @Override
    public OnlineUser authenticate(String username, String password)
            throws AuthenticationException {
        // 1. Lookup user
        OnlineUser user = userDao.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        // 2. Verify hash
        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return user;
    }
}
