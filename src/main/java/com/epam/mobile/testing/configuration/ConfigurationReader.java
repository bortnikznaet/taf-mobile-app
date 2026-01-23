package com.epam.mobile.testing.configuration;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationReader {

    private static final Logger LOG = LogManager.getLogger(ConfigurationReader.class);

    private static final String DEFAULT_CONFIG_FILE = "test.properties";
    private static final String CONFIG_FILE_SYS_PROP = "config.file";
    private static final Properties properties = new Properties();
    private static ConfigurationReader instance;

    private ConfigurationReader() {

    }

    public static ConfigurationReader get() {
        if (instance == null) {
            synchronized (ConfigurationReader.class) {
                if (instance == null) {
                    instance = new ConfigurationReader();
                    instance.loadProperties();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        String configFile = System.getProperty(CONFIG_FILE_SYS_PROP, DEFAULT_CONFIG_FILE).trim();
        LOG.info("Loading configuration from: {}", configFile);

        Path fsPath = Paths.get(configFile);
        if (Files.exists(fsPath)) {
            try (InputStream in = Files.newInputStream(fsPath)) {
                properties.load(in);
                LOG.info("Configuration loaded from filesystem: {}", fsPath.toAbsolutePath());
                return;
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load config from filesystem path: " + fsPath.toAbsolutePath(), e);
            }
        }

        try (InputStream in = ConfigurationReader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (in == null) {
                throw new IllegalStateException(
                        "Config file not found. Tried filesystem path and classpath resource: " + configFile);
            }
            properties.load(in);
            LOG.info("Configuration loaded from classpath resource: {}", configFile);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config from classpath resource: " + configFile, e);
        }
    }


    public String env() {
        return properties.getProperty("env.type");
    }

    public String appPath() {
        return properties.getProperty("app.path");
    }

    public String appPackage() {
        return properties.getProperty("app.package");
    }

    public String appActivity() {
        return properties.getProperty("app.activity");
    }

    public String platformName() {
        return properties.getProperty("platform.name");
    }

    public String platformVersion() {
        return properties.getProperty("platform.version");
    }

    public String localDeviceName() {
        return properties.getProperty("local.device.name");
    }

    public String udid() {
        return properties.getProperty("udid");
    }

    public String avdName() {
        return properties.getProperty("avd.name");
    }

    public String appiumAddress() {
        return properties.getProperty("appium.address");
    }

    public int appiumPort() {
        return NumberUtils.toInt(properties.getProperty("appium.port"));
    }
}
