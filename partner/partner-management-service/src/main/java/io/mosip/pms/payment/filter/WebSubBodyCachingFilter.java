package io.mosip.pms.payment.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSubBodyCachingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;

        if (httpReq.getRequestURI().contains("/callback/partnermanagement/")) {
            ContentCachingRequestWrapper wrapped = new ContentCachingRequestWrapper(httpReq);
            chain.doFilter(wrapped, res);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // no-op
    }
}