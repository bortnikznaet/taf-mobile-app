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
    private static final String ENV_NAME_SYS_PROP = "env.name";

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

    private String resolveConfigFile() {
        String configFile = System.getProperty(CONFIG_FILE_SYS_PROP);
        if (configFile != null && !configFile.isBlank()) {
            return configFile.trim();
        }

        String envName = System.getProperty(ENV_NAME_SYS_PROP);
        if (envName != null && !envName.isBlank()) {
            return envName.trim() + ".properties";
        }

        return DEFAULT_CONFIG_FILE;
    }


    private void loadProperties() {
        String configFile = resolveConfigFile();

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

    public String browserStackUser() {
        return properties.getProperty("browserstack.user");
    }

    public String browserStackKey() {
        return properties.getProperty("browserstack.key");
    }

    public String browserStackAppUrl() {
        return properties.getProperty("browserstack.app.url");
    }

    public String browserStackDeviceName() {
        return properties.getProperty("browserstack.device.name");
    }

    public String browserStackOsVersion() {
        return properties.getProperty("browserstack.os.version");
    }

    public String browserStackProject() {
        return properties.getProperty("browserstack.project");
    }

    public String browserStackBuild() {
        return properties.getProperty("browserstack.build");
    }

    public String browserStackSessionName() {
        return properties.getProperty("browserstack.session.name");
    }

    public String browserStackAppiumVersion() {
        return properties.getProperty("browserstack.appium.version");
    }

    public EnvironmentType environmentType() {
        String rawEnv = env();
        if (rawEnv == null || rawEnv.isBlank()) {
            LOG.warn("env.type is not set. Falling back to LOCAL");
            return EnvironmentType.LOCAL;
        }

        try {
            return EnvironmentType.valueOf(rawEnv.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.warn("Unknown env.type='{}'. Falling back to LOCAL", rawEnv);
            return EnvironmentType.LOCAL;
        }
    }

}
