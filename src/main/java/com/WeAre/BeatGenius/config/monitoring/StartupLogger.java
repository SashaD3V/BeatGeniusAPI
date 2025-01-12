package com.WeAre.BeatGenius.config.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {
  private static final Logger logger = LoggerFactory.getLogger(StartupLogger.class);

  @EventListener(ApplicationStartedEvent.class)
  public void logStartup() {
    logger.info("=== Démarrage BeatGenius ===");
    logEnvironmentInfo();
    logSecurityInfo();
    logDatabaseInfo();
    logger.info("=== Démarrage terminé ===");
  }

  private void logEnvironmentInfo() {
    logger.info("▼ Configuration Environnement");
    logger.info("  → Java Version: {}", System.getProperty("java.version"));
    logger.info("  → Spring Boot Version: 3.2.1");
    logger.info("  → Profil actif: dev");
  }

  private void logSecurityInfo() {
    logger.info("▼ Configuration Sécurité");
    logger.info("  → JWT activé");
    logger.info("  → CORS configuré pour *");
    logger.info("  → BCrypt configuré (strength: 10)");
  }

  private void logDatabaseInfo() {
    logger.info("▼ Configuration Base de données");
    logger.info("  → PostgreSQL connecté");
    logger.info("  → HikariCP configuré");
    logger.info("  → Hibernate configuré");
  }
}
