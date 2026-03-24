package br.com.posxp.clientesapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setIncludeHeaders(false);
        filter.setIncludeClientInfo(true);
        filter.setMaxPayloadLength(1000);
        filter.setAfterMessagePrefix("HTTP REQUEST: ");
        return filter;
    }
}

