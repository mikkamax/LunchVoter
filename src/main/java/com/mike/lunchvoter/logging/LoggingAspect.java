package com.mike.lunchvoter.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(value = "logging.aspect.enabled", havingValue = "true")
public class LoggingAspect {

    private static final String ENTITIES_TO_LOG = """
            within(@org.springframework.stereotype.Service *)
            ||
            within(@org.springframework.web.bind.annotation.RestController *)
            ||
            execution(* org.springframework.data.jpa.repository.JpaRepository..*(..))
            ||
            execution(* com.mike.lunchvoter.mapping..*(..))
            """;

    @Around(ENTITIES_TO_LOG)
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodDescription methodDescription = LogUtils.tryToGetMethodDescription(joinPoint);
        LogUtils.writeInLog(methodDescription);

        try {
            Object result = joinPoint.proceed();
            LogUtils.writeOutLog(methodDescription, result);
            return result;
        } catch (Throwable throwable) {
            LogUtils.writeErrorOutLog(methodDescription, throwable);
            throw throwable;
        }
    }

}
