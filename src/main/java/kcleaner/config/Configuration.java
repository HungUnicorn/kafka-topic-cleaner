package kcleaner.config;

import lombok.Getter;

@Getter
public class Configuration {
    private final String bootStrapServers;
    private boolean isDeleteEnabled;


    public Configuration(String bootStrapServers, String isDeleteEnabled) {
        this.bootStrapServers = bootStrapServers;
        this.isDeleteEnabled = Boolean.parseBoolean(isDeleteEnabled);
    }
}
