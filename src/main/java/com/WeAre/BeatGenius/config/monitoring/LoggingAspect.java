// LoggingAspect.java
package com.WeAre.BeatGenius.config.monitoring;

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
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      logger.debug("Début d'exécution de {}", methodName);
      Object result = joinPoint.proceed();
      stopWatch.stop();
      logger.debug("Fin d'exécution de {} en {} ms", methodName, stopWatch.getTotalTimeMillis());
      return result;
    } catch (Exception e) {
      logger.error("Erreur dans {} : {}", methodName, e.getMessage());
      throw e;
    }
  }
}
