package com.digitalbuzz.middleware;

import com.digitalbuzz.util.CorrelationIdUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestTracingFilter extends OncePerRequestFilter {
    
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private final CorrelationIdUtil correlationIdUtil;
    
    public RequestTracingFilter(CorrelationIdUtil correlationIdUtil) {
        this.correlationIdUtil = correlationIdUtil;
    }
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = correlationIdUtil.generateCorrelationId();
            }
            
            correlationIdUtil.setCorrelationId(correlationId);
            response.setHeader(CORRELATION_ID_HEADER, correlationId);
            
            filterChain.doFilter(request, response);
        } finally {
            correlationIdUtil.clearCorrelationId();
        }
    }
}
