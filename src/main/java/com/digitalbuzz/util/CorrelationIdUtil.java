package com.digitalbuzz.util;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CorrelationIdUtil {
    
    private static final String CORRELATION_ID_KEY = "correlationId";
    
    public String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
    
    public void setCorrelationId(String correlationId) {
        MDC.put(CORRELATION_ID_KEY, correlationId);
    }
    
    public String getCorrelationId() {
        return MDC.get(CORRELATION_ID_KEY);
    }
    
    public void clearCorrelationId() {
        MDC.remove(CORRELATION_ID_KEY);
    }
}
