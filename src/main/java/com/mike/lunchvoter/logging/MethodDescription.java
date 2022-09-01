package com.mike.lunchvoter.logging;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.logging.LogLevel;

@Data
@ConditionalOnBean(LoggingAspect.class)
public class MethodDescription {
    private String name;
    private String className;
    private String description;
    private boolean includeParams = true;
    private LogLevel logLevel = LogLevel.INFO;
}
