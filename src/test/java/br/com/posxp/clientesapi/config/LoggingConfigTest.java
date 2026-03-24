package br.com.posxp.clientesapi.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

class LoggingConfigTest {

    @Test
    void deveCriarFiltroDeLogDeRequisicao() {
        LoggingConfig config = new LoggingConfig();

        CommonsRequestLoggingFilter filter = config.requestLoggingFilter();

        assertNotNull(filter);
        assertTrue((Boolean) ReflectionTestUtils.getField(filter, "includePayload"));
        assertTrue((Boolean) ReflectionTestUtils.getField(filter, "includeQueryString"));
        assertTrue((Boolean) ReflectionTestUtils.getField(filter, "includeClientInfo"));
    }
}
