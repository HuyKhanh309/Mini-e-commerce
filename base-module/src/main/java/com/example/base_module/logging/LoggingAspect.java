package com.example.base_module.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private static final Log log = LogFactory.getLog(LoggingAspect.class);

	@Around("execution(* com.example..service..*(..))")
	public Object logging(ProceedingJoinPoint pjp) throws Throwable {
		log.info("Entering method: " + pjp.getSignature().getName());
    long start = System.currentTimeMillis();
		Object result = pjp.proceed();
    long end = System.currentTimeMillis();
		log.info("Exiting method: " + pjp.getSignature().getName());
    log.info("Execution time: " + (end - start) + " ms");
		return result;
	}
}