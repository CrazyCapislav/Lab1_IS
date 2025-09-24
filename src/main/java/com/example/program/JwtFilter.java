package com.example.program;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();
        
        if (path.startsWith("/api/") && !path.startsWith("/api/auth/login")) {
            String auth = req.getHeader("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) {
                res.sendRedirect("/redirect-error.html");
                return;
            }
            
            String token = auth.substring(7);
            if (!jwtUtil.validateToken(token)) {
                res.sendRedirect("/redirect-error.html");
                return;
            }
            
            req.setAttribute("currentUser", jwtUtil.getUsernameFromToken(token));
        }
        
        chain.doFilter(request, response);
    }
}
