package com.spike.springcloud.grafana.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.io.IOException;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "RequestLoggingFilter", urlPatterns = "/*")
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    private final static Logger LOG = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;

        if (isIncludePayload() && isFirstRequest && !(request instanceof RepeatableContentCachingRequestWrapper)) {
            requestToUse = new RepeatableContentCachingRequestWrapper(request);
        }

        boolean shouldLog = shouldLog(requestToUse);
        if (shouldLog && isFirstRequest) {
            beforeRequest(requestToUse, getBeforeMessage(requestToUse));
        }
        try {
            filterChain.doFilter(requestToUse, response);
        } finally {
            if (shouldLog && !isAsyncStarted(requestToUse)) {
                afterRequest(requestToUse, getAfterMessage(requestToUse));
            }
            // We also need the response!!!
        }
    }

    /**
     * Get the message to write to the log before the request.
     *
     * @see #createMessage
     */
    private String getBeforeMessage(HttpServletRequest request) {
        return createMessage(request, DEFAULT_BEFORE_MESSAGE_PREFIX, DEFAULT_BEFORE_MESSAGE_SUFFIX);
    }

    private String getAfterMessage(HttpServletRequest request) {
        return createMessage(request, DEFAULT_AFTER_MESSAGE_PREFIX, DEFAULT_AFTER_MESSAGE_SUFFIX);
    }
}