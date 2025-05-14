// src/main/java/main/com/syos/web/controller/ReportController.java
package main.com.syos.web.controller;

import main.com.syos.service.impl.ReportServiceImpl;
import main.com.syos.dao.impl.*;
import main.com.syos.util.DbConnectionManager;
import main.com.syos.util.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/api/reports/*")
public class ReportController extends HttpServlet {
    private final ReportServiceImpl svc;
    public ReportController() {
        String url = "jdbc:mysql://127.0.0.1:3306/syos";
        String user = "root";
        String pass = "1234";

        var db = new DbConnectionManager(url, user, pass);
        svc = new ReportServiceImpl(
                new JdbcBillDao(db),
                new JdbcReshelvingLogDao(db),
                new JdbcItemDao(db),
                new JdbcBatchDao(db),
                new JdbcStockDao(db)
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String p = req.getPathInfo();
        switch (p) {
            case "/daily-sales":
                JsonParser.toJson(resp,
                        svc.getDailySalesReport(LocalDate.parse(req.getParameter("date"))));
                break;
            case "/reshelving":
                JsonParser.toJson(resp,
                        svc.getDailyReshelvingReport(LocalDate.parse(req.getParameter("date"))));
                break;
            case "/reorder-alerts":
                JsonParser.toJson(resp, svc.getReorderAlerts());
                break;
            case "/stock-report":
                JsonParser.toJson(resp, svc.getStockReport());
                break;
            case "/all-bills":
                JsonParser.toJson(resp, svc.getAllBills());
                break;
            default:
                resp.sendError(404);
        }
    }
}
