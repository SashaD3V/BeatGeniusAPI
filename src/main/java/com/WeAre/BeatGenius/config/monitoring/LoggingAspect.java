package com.WeAre.BeatGenius.config.monitoring;

import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.api.dto.responses.marketplace.LicenseResponse;
import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

  @Around("execution(* com.WeAre.BeatGenius..*.*(..))")
  public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    if (joinPoint.getTarget().getClass().getName().contains("Mapper")) {
      return logMapping(joinPoint, logger);
    }

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      logger.debug("▶ {} avec {}", methodName, extractArgs(args));

      Object result = joinPoint.proceed();

      stopWatch.stop();
      String resultInfo = extractResultInfo(result);
      logger.debug(
          "◀ {} terminé {} ({}ms)", methodName, resultInfo, stopWatch.getTotalTimeMillis());

      return result;
    } catch (Exception e) {
      logger.error("✖ Erreur dans {} : {}", methodName, e.getMessage());
      throw e;
    }
  }

  private Object logMapping(ProceedingJoinPoint joinPoint, Logger logger) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    Object source = joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : null;
    String sourceInfo = extractArgs(new Object[] {source});

    Object result = joinPoint.proceed();
    String resultInfo = extractResultInfo(result);

    stopWatch.stop();
    logger.debug(
        "⇒ Mapping {} vers {} ({}ms)",
        sourceInfo.isEmpty() ? "null" : sourceInfo,
        resultInfo.isEmpty() ? result.getClass().getSimpleName() : resultInfo,
        stopWatch.getTotalTimeMillis());

    return result;
  }

  private String extractArgs(Object[] args) {
    if (args == null || args.length == 0) return "";

    StringBuilder info = new StringBuilder();
    for (Object arg : args) {
      if (arg instanceof BeatResponse beat) {
        info.append(String.format("Beat[id=%s, title='%s'] ", beat.getId(), beat.getTitle()));
      } else if (arg instanceof LicenseResponse license) {
        info.append(String.format("License[id=%s, type=%s] ", license.getId(), license.getType()));
      } else if (arg instanceof UserResponse user) {
        info.append(String.format("User[id=%s, artist='%s'] ", user.getId(), user.getArtistName()));
      }
    }
    return info.toString().trim();
  }

  private String extractResultInfo(Object result) {
    if (result == null) return "";

    if (result instanceof BeatResponse beat) {
      return String.format("→ Beat[id=%s, title='%s']", beat.getId(), beat.getTitle());
    }
    if (result instanceof LicenseResponse license) {
      return String.format("→ License[id=%s, type=%s]", license.getId(), license.getType());
    }
    if (result instanceof UserResponse user) {
      return String.format("→ User[id=%s, artist='%s']", user.getId(), user.getArtistName());
    }

    return "";
  }
}
