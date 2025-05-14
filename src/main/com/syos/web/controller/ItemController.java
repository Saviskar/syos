// src/main/java/main/com/syos/web/controller/ItemController.java
package main.com.syos.web.controller;

import main.com.syos.dao.impl.JdbcItemDao;
import main.com.syos.model.Item;
import main.com.syos.util.DbConnectionManager;
import main.com.syos.util.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/api/items/*")
public class ItemController extends HttpServlet {
    String url = "jdbc:mysql://127.0.0.1:3306/syos";
    String user = "root";
    String pass = "1234";

    private final JdbcItemDao itemDao =
            new JdbcItemDao(new DbConnectionManager(url, user, pass));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String p = req.getPathInfo();
        if (p == null || "/".equals(p)) {
            List<Item> items = itemDao.findAll();
            JsonParser.toJson(resp, items);
        } else {
            String code = p.substring(1);
            Optional<Item> i = itemDao.findByCode(code);
            if (i.isPresent()) JsonParser.toJson(resp, i.get());
            else resp.setStatus(404);
        }
    }
}
