package com.mike.lunchvoter.logging;

import lombok.Data;
import org.springframework.boot.logging.LogLevel;

@Data
public class MethodDescription {
    private String name;
    private String className;
    private String description;
    private boolean includeParams = true;
    private LogLevel logLevel = LogLevel.INFO;
}
