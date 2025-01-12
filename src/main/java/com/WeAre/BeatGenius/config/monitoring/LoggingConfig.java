// LoggingConfig.java
package com.WeAre.BeatGenius.config.monitoring;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
  private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

  @Bean
  public Filter requestIdFilter() {
    return new RequestIdFilter();
  }

  public static class RequestIdFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestIdFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      String requestId = UUID.randomUUID().toString();

      try {
        MDC.put("requestId", requestId);
        httpResponse.addHeader("X-Request-ID", requestId);
        logger.debug("Nouvelle requête avec ID: {}", requestId);
        chain.doFilter(request, response);
      } catch (Exception e) {
        logger.error("Erreur lors du traitement de la requête (ID: {})", requestId, e);
        throw e;
      } finally {
        MDC.clear();
        logger.debug("Fin du traitement de la requête (ID: {})", requestId);
      }
    }

    @Override
    public void destroy() {}
  }
}
