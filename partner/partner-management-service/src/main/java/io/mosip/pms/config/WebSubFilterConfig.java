package io.mosip.pms.config;

import io.mosip.kernel.websub.api.filter.MultipleReadRequestBodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebSubFilterConfig {

    @Bean
    public FilterRegistrationBean<MultipleReadRequestBodyFilter> multipleReadRequestBodyFilter() {
        FilterRegistrationBean<MultipleReadRequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MultipleReadRequestBodyFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/callback/*");
        return registrationBean;
    }
}