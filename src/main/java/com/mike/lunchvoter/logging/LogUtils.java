package com.mike.lunchvoter.logging;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@UtilityClass
@ConditionalOnBean(LoggingAspect.class)
public class LogUtils {

    private static final String IN_POINTER = ">>";
    private static final String OUT_POINTER = "<<";

    public static void writeInLog(MethodDescription methodDescription) {
        if (methodDescription == null) {
            logNoParameterError("writeInLog");
            return;
        }

        writeLog(
                methodDescription,
                IN_POINTER + " {}{}",
                methodDescription.getName(),
                methodDescription.getDescription()
        );
    }

    public static void writeOutLog(MethodDescription methodDescription, Object result) {
        if (methodDescription == null) {
            logNoParameterError("writeOutLog");
            return;
        }

        writeLog(
                methodDescription,
                OUT_POINTER + " {}({})",
                methodDescription.getName(),
                Objects.toString(result)
        );
    }

    public static void writeErrorOutLog(MethodDescription methodDescription, Throwable exception) {
        if (methodDescription == null || exception == null) {
            logNoParameterError("writeErrorOutLog");
            return;
        }

        writeLog(
                methodDescription,
                OUT_POINTER + " {} error. ({})",
                methodDescription.getName(),
                exception.getMessage()
        );
    }

    public static MethodDescription tryToGetMethodDescription(JoinPoint joinPoint) {
        try {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();

            MethodDescription result = new MethodDescription();
            result.setName(method.getName());
            result.setClassName(targetClass.getName());

            if (joinPoint.getArgs() != null) {
                result.setDescription(getReadableStringFromArgs(joinPoint.getArgs()));
            }

            return result;
        } catch (Exception e) {
            logLoggingError(e);
            return null;
        }
    }

    private static String getReadableStringFromArgs(Object[] args) {
        StringJoiner description = new StringJoiner(",", "(", ")");

        for (Object arg : args) {
            description.add(Objects.toString(arg));
        }

        return description.toString();
    }

    private static void logLoggingError(Exception e) {
        log.error("Logging error: {} {}", e.getClass().getName(), e.getMessage());
    }

    private static void logNoParameterError(String methodName) {
        log.error("Logging error: the necessary parameters are not passed to the method {}", methodName);
    }

    private static void writeLog(MethodDescription methodDescription, String line, Object... arguments) {
        try {
            Logger logger = LoggerFactory.getLogger(methodDescription.getClassName());

            switch (methodDescription.getLogLevel()) {
                case TRACE -> logger.trace(line, arguments);
                case DEBUG -> logger.debug(line, arguments);
                case INFO -> logger.info(line, arguments);
                case WARN -> logger.warn(line, arguments);
                case ERROR -> logger.error(line, arguments);
                default -> logger.warn(line, arguments);
            }
        } catch (Exception e) {
            logLoggingError(e);
        }
    }
}
