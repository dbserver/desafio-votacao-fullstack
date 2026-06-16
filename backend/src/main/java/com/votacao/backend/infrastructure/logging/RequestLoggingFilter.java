package com.votacao.backend.infrastructure.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    public static final String HEADER_REQUEST_ID = "X-Request-Id";
    public static final String MDC_REQUEST_ID_KEY = "requestId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestId = resolveRequestId(request);
        String path = resolvePath(request);
        long start = System.nanoTime();

        MDC.put(MDC_REQUEST_ID_KEY, requestId);
        response.setHeader(HEADER_REQUEST_ID, requestId);

        try {
            log.info("Request started {} {}", request.getMethod(), path);
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info(
                    "Request finished {} {} status={} durationMs={}",
                    request.getMethod(),
                    path,
                    response.getStatus(),
                    durationMs
            );
            MDC.remove(MDC_REQUEST_ID_KEY);
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_REQUEST_ID);
        if (StringUtils.hasText(headerValue)) {
            return headerValue.trim();
        }
        return UUID.randomUUID().toString();
    }

    private String resolvePath(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (!StringUtils.hasText(queryString)) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + queryString;
    }
}
