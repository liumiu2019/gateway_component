package com.liuxiang.component.gateway.template;

import lombok.Data;

import java.util.Map;

@Data
public class Filters {
    private String name;

    private Map<String, String> args;
}
