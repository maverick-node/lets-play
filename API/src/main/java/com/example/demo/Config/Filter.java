package com.example.demo.Config;

import org.springframework.web.filter.OncePerRequestFilter;

public class Filter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, java.io.IOException {
        filterChain.doFilter(request, response);
    }
}
