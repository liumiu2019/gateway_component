package com.liuxiang.component.gateway.template;

import lombok.Data;

import java.util.Map;

@Data
public class Predicate {
    private Map<String, String> args;

    private String name;
}
