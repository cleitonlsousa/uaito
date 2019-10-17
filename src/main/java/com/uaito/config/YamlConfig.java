package com.uaito.config;

import java.util.Map;

public class YamlConfig {

    private String environment;
    private Map<String, String> var;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Map<String, String> getVar() {
        return var;
    }

    public void setVar(Map<String, String> var) {
        this.var = var;
    }
}
