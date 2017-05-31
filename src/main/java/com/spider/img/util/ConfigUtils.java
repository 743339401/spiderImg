package com.spider.img.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zhangkai on 17/5/27.
 */
public class ConfigUtils {
    private static final String CONF_PATH = "config.conf";
    private static final Config config = load(CONF_PATH);

    /**
     * Load the config from the resource base path (currently use the conf/application.conf).
     *
     * @param resourceBasename - the resource base name (config file path)
     * @return an Config object which contains the whole config info
     */
    public static Config load(String resourceBasename) {
        return ConfigFactory.load(resourceBasename);
    }

    /**
     * Get the configuration info by config item name.
     *
     * @param itemName - the config item name
     * @return an Config object which contains the config info
     */
    public static Config getConfig(String itemName) {
        return config.getConfig(itemName);
    }
}
