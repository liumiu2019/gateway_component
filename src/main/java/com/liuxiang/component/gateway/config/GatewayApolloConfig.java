package com.liuxiang.component.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayApolloConfig {
    public static String APOLLO_APP_ID;

    public static String APOLLO_SERVER_ADDR;

    public static String APOLLO_NAMESPACE;

    public static String APOLLO_ENV;

    public static String APOLLO_CLUSTER;

    public static String APOLLO_TOKEN;

    public static String APOLLO_CREATOR;

    @Value("${apollo.server-addr}")
    public void setApolloServerAddr(String apolloServerAddr) {
        APOLLO_SERVER_ADDR = apolloServerAddr;
    }

    @Value("${apollo.app.id}")
    public void setApolloAppId(String appId) {
        APOLLO_APP_ID = appId;
    }

    @Value("${apollo.namespace}")
    public void setApolloNamespace(String apolloNamespace) {
        APOLLO_NAMESPACE = apolloNamespace;
    }

    @Value("${apollo.env}")
    public void setApolloEnv(String apolloEnv) {
        APOLLO_ENV = apolloEnv;
    }

    @Value("${apollo.cluster}")
    public void setApolloCluster(String apolloCluster) {
        APOLLO_CLUSTER = apolloCluster;
    }

    @Value("${apollo.token}")
    public void setApolloToken(String apolloToken) {
        APOLLO_TOKEN = apolloToken;
    }

    @Value("${apollo.creator}")
    public void setApolloCreator(String apolloCreator) {
        APOLLO_CREATOR = apolloCreator;
    }
}
