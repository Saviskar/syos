// src/main/java/main/com/syos/web/controller/BillingController.java
package main.com.syos.web.controller;

import main.com.syos.service.impl.BillingServiceImpl;
import main.com.syos.dao.impl.*;
import main.com.syos.service.StockService;
import main.com.syos.service.impl.StockServiceImpl;
import main.com.syos.util.DbConnectionManager;
import main.com.syos.util.JsonParser;
import main.com.syos.web.dto.CheckoutRequest;
import main.com.syos.web.dto.OnlineCheckoutRequest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/checkout/*")
public class BillingController extends HttpServlet {
    private final BillingServiceImpl svc;
    public BillingController() {
        String url = "jdbc:mysql://127.0.0.1:3306/syos";
        String user = "root";
        String pass = "1234";

        var db  = new DbConnectionManager(url,user,pass);
        var itemDao = new JdbcItemDao(db);
        var batchDao= new JdbcBatchDao(db);
        var stockDao= new JdbcStockDao(db);
        var webInvDao = new JdbcWebsiteInventoryDao(db);
        StockService ss = new StockServiceImpl(batchDao, stockDao, webInvDao);
        this.svc = new BillingServiceImpl(
                new JdbcBillDao(db),
                new JdbcBillItemDao(db),
                itemDao,
                ss,
                new JdbcOnlineUserDao(db)
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String p = req.getPathInfo();
        if ("/otc".equals(p)) {
            CheckoutRequest r = JsonParser.fromJson(req.getReader(), CheckoutRequest.class);
            try {
                JsonParser.toJson(resp, svc.checkoutOTC(r.getItems(), r.getCashTendered()));
            } catch (Exception e) {
                resp.sendError(400, e.getMessage());
            }
        } else if ("/online".equals(p)) {
            OnlineCheckoutRequest r = JsonParser.fromJson(req.getReader(), OnlineCheckoutRequest.class);
            try {
                JsonParser.toJson(resp, svc.checkoutOnline(r.getUsername(), r.getItems()));
            } catch (Exception e) {
                resp.sendError(400, e.getMessage());
            }
        } else {
            resp.sendError(404);
        }
    }
}
