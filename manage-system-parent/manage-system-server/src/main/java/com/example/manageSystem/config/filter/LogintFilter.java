package com.example.manageSystem.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogintFilter implements Filter {

    public void init(FilterConfiguration filterConfig) throws ServletException {
        System.out.println("init");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        if (!requestURI.contains("login")) {
            if (session != null && session.getAttribute("user") != null) {
                filterChain.doFilter(request, response);
            } else {
                String requestType = request.getHeader("X-Requested-With");
                if (requestType != null && "XMLHttpRequest".equals(requestType)) {
                    response.getWriter().write("登录过期，请重新登录!");
                } else {
                    response.sendRedirect("http://192.168.0.106:8080"+ "/login");
                }
                return;
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    public void destroy() {
        System.out.println("destroy");
    }
}
