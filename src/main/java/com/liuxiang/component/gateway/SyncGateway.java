package com.liuxiang.component.gateway;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceLockDTO;
import com.liuxiang.component.gateway.template.GateWayConfigTemplate;
import com.liuxiang.component.gateway.template.Predicate;
import com.liuxiang.component.gateway.config.GatewayApolloConfig;
import com.liuxiang.component.gateway.template.Filters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author liuxiang
 * 各个服务同步gateway路由至nacos
 */
@Component
@Slf4j
@DependsOn("gatewayApolloConfig")
public class SyncGateway {

    private static final String arg = "_genkey_";

    @Value("${spring.cloud.nacos.discovery.service}")
    private String serviceName;

    private ApolloOpenApiClient client;

    @PostConstruct
    public void init() {
        log.info("sync gateway start");
        this.initConfigService();
        try {
            String key = "gateway." + this.serviceName + ".list";
            String id = this.serviceName + "-router";
            //添加或新增配置
            GateWayConfigTemplate gateWayConfigTemplate = new GateWayConfigTemplate();
            gateWayConfigTemplate.setId(id);
            gateWayConfigTemplate.setOrder(0);
            gateWayConfigTemplate.setUri("lb://" + this.serviceName);
            //predicates
            List<Predicate> predicateList = new LinkedList<>();
            Predicate predicate = new Predicate();
            predicate.setName("Path");
            Map<String, String> args = new HashMap<>();
            args.put(SyncGateway.arg + 1, "/" + this.serviceName + "/**");
            predicate.setArgs(args);
            predicateList.add(predicate);
            //filters
            List<Filters> filtersList = new LinkedList<>();
            Filters filters = new Filters();
            Map<String, String> filterArgs = new HashMap<>();
            filterArgs.put(SyncGateway.arg, "1");
            filters.setArgs(filterArgs);
            filters.setName("StripPrefix");
            filtersList.add(filters);

            gateWayConfigTemplate.setPredicates(predicateList);
            gateWayConfigTemplate.setFilters(filtersList);
            String newConfigs = JSON.toJSONString(gateWayConfigTemplate);
            OpenItemDTO dto = new OpenItemDTO();
            dto.setKey(key);
            dto.setValue(newConfigs);
            dto.setDataChangeCreatedBy(GatewayApolloConfig.APOLLO_CREATOR);
            dto.setDataChangeLastModifiedBy(GatewayApolloConfig.APOLLO_CREATOR);
            log.info("new configs {}", newConfigs);
            log.info("dto {}", dto);
            this.client.createOrUpdateItem(GatewayApolloConfig.APOLLO_APP_ID,
                    GatewayApolloConfig.APOLLO_ENV,
                    GatewayApolloConfig.APOLLO_CLUSTER,
                    GatewayApolloConfig.APOLLO_NAMESPACE,
                    dto
            );
            NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
            namespaceReleaseDTO.setReleasedBy(GatewayApolloConfig.APOLLO_CREATOR);
            namespaceReleaseDTO.setReleaseTitle(this.serviceName + " service start");
            this.client.publishNamespace(GatewayApolloConfig.APOLLO_APP_ID,
                    GatewayApolloConfig.APOLLO_ENV,
                    GatewayApolloConfig.APOLLO_CLUSTER,
                    GatewayApolloConfig.APOLLO_NAMESPACE, namespaceReleaseDTO);
            log.info("sync gateway end...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化网关路由 apollo config
     */
    public void initConfigService() {
        try {
            this.client = ApolloOpenApiClient.newBuilder()
                    .withPortalUrl(GatewayApolloConfig.APOLLO_SERVER_ADDR)
                    .withToken(GatewayApolloConfig.APOLLO_TOKEN)
                    .build();
            OpenNamespaceLockDTO lockDTO = client.getNamespaceLock(
                    GatewayApolloConfig.APOLLO_APP_ID,
                    GatewayApolloConfig.APOLLO_ENV,
                    GatewayApolloConfig.APOLLO_CLUSTER,
                    GatewayApolloConfig.APOLLO_NAMESPACE
            );
            if (lockDTO.isLocked()) {
                throw new RuntimeException("阿波罗发布失败");
            }
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误", e);
        }
    }
}
