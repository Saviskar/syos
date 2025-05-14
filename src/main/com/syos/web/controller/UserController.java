// src/main/java/main/com/syos/web/controller/UserController.java
package main.com.syos.web.controller;

import main.com.syos.service.impl.UserServiceImpl;
import main.com.syos.dao.impl.JdbcOnlineUserDao;
import main.com.syos.service.exception.AuthenticationException;
import main.com.syos.service.exception.UserAlreadyExistsException;
import main.com.syos.util.DbConnectionManager;
import main.com.syos.util.JsonParser;
import main.com.syos.web.dto.RegisterRequest;
import main.com.syos.web.dto.LoginRequest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/users/*")
public class UserController extends HttpServlet {
    String url = "jdbc:mysql://127.0.0.1:3306/syos";
    String user = "root";
    String pass = "1234";

    private final UserServiceImpl svc = new UserServiceImpl(
            new JdbcOnlineUserDao(new DbConnectionManager(url, user, pass))
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String p = req.getPathInfo();
        if ("/register".equals(p)) {
            RegisterRequest r = JsonParser.fromJson(req.getReader(), RegisterRequest.class);
            try {
                JsonParser.toJson(resp, svc.register(r.getUsername(), r.getPassword(), r.getFullName(), r.getEmail()));
            } catch (UserAlreadyExistsException e) {
                resp.sendError(409, e.getMessage());
            }
        } else if ("/login".equals(p)) {
            LoginRequest r = JsonParser.fromJson(req.getReader(), LoginRequest.class);
            try {
                JsonParser.toJson(resp, svc.authenticate(r.getUsername(), r.getPassword()));
            } catch (AuthenticationException e) {
                resp.sendError(401, e.getMessage());
            }
        } else {
            resp.sendError(404);
        }
    }
}
