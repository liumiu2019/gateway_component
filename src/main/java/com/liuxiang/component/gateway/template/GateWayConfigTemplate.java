package com.liuxiang.component.gateway.template;

import lombok.Data;

import java.util.List;

@Data
public class GateWayConfigTemplate {
    private String id;

    private Integer order;

    private List<Predicate> predicates;

    private String uri;

    private List<Filters> filters;
}
