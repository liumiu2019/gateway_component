package com.liuxiang.component.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxiang
 */
@Configuration
@ComponentScan("com.liuxiang.component.gateway")
@Slf4j
public class GatewayAutoScanBeanConfiguration {
    @Bean
    public void scanGatewayBean() {
        log.info("gateway bean scan start");
    }
}
