package com.WeAre.BeatGenius.config.monitoring;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class RequestLoggingFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestId = UUID.randomUUID().toString();

    try {
      MDC.put("requestId", requestId);
      MDC.put("method", httpRequest.getMethod());
      MDC.put("uri", httpRequest.getRequestURI());

      String authHeader = httpRequest.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        MDC.put("hasToken", "true");
      }

      logger.debug("Réception requête {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());

      chain.doFilter(request, response);

    } finally {
      logger.debug("Fin requête");
      MDC.clear();
    }
  }
}
