package main.com.syos.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 1) Allow your front-end origin (or use "*" to allow all)
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        // 2) Allow standard methods
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 3) Allow these headers from the browser
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // 4) Cache preflight response for 1 hour
        response.setHeader("Access-Control-Max-Age", "3600");

        // 5) For preflight requests, short-circuit and return
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 6) Continue down the filter chain
        chain.doFilter(req, res);
    }
}
