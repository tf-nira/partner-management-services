package io.mosip.pms.payment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) req);

        logger.info("Incoming request: {} {}", wrappedRequest.getMethod(), wrappedRequest.getRequestURI());

        Enumeration<String> headers = wrappedRequest.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            logger.info("Header: {} = {}", header, wrappedRequest.getHeader(header));
        }

        chain.doFilter(wrappedRequest, res); // pass wrapped request, not original

        // body is available AFTER chain executes
        String body = new String(wrappedRequest.getContentAsByteArray());
        logger.info("Request body: {}", body);
    }

    @Override
    public void destroy() {
    }
}