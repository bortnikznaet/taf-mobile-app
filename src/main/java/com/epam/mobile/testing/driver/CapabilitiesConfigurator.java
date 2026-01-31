package com.epam.mobile.testing.driver;

import com.epam.mobile.testing.configuration.ConfigurationReader;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class CapabilitiesConfigurator {

    private static final Logger LOG = LogManager.getLogger(CapabilitiesConfigurator.class);

    private  CapabilitiesConfigurator(){
    }

    public static UiAutomator2Options getLocalCapabilities() {
        ConfigurationReader cfg = ConfigurationReader.get();

        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2")
                .setPlatformName(cfg.platformName())
                .setPlatformVersion(cfg.platformVersion())
                .setDeviceName(cfg.localDeviceName());

        if (cfg.avdName() != null && !cfg.avdName().isBlank()) {
            options.setCapability("appium:avd", cfg.avdName());
            LOG.info("AVD capability was set: {}", cfg.avdName());
        }

        String appPath = cfg.appPath();
        if (appPath != null && !appPath.isBlank()) {
            Path apk = Paths.get(appPath).normalize();
            if (Files.exists(apk) && Files.isRegularFile(apk) && appPath.toLowerCase().endsWith(".apk")) {
                options.setApp(apk.toAbsolutePath().toString());
                LOG.info("App capability was set: {}", apk.toAbsolutePath());
            } else {
                LOG.info("App capability is not set (app.path is not a .apk file): {}", appPath);
            }
        }

        return options;
    }

    public static UiAutomator2Options getBrowserStackCapabilities() {
        ConfigurationReader cfg = ConfigurationReader.get();

        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2")
                .setPlatformName(cfg.platformName());

        String deviceName = cfg.browserStackDeviceName();
        if (deviceName != null && !deviceName.isBlank()) {
            options.setCapability("appium:deviceName", deviceName);
        }

        String osVersion = cfg.browserStackOsVersion();
        if (osVersion != null && !osVersion.isBlank()) {
            options.setCapability("appium:platformVersion", osVersion);
        }

        // App (bs://...)
        String appUrl = cfg.browserStackAppUrl();
        if (appUrl != null && !appUrl.isBlank()) {
            options.setApp(appUrl);
            LOG.info("BrowserStack app capability was set: {}", appUrl);
        }

        Map<String, Object> bstackOptions = new HashMap<>();

        putIfNotBlank(bstackOptions, "userName", cfg.browserStackUser());
        putIfNotBlank(bstackOptions, "accessKey", cfg.browserStackKey());

        putIfNotBlank(bstackOptions, "projectName", cfg.browserStackProject());
        putIfNotBlank(bstackOptions, "buildName", cfg.browserStackBuild());
        putIfNotBlank(bstackOptions, "sessionName", cfg.browserStackSessionName());
        putIfNotBlank(bstackOptions, "appiumVersion", cfg.browserStackAppiumVersion());

        options.setCapability("bstack:options", bstackOptions);

        return options;
    }

    private static void putIfNotBlank(Map<String, Object> map, String key, String value) {
        if (value != null && !value.isBlank()) {
            map.put(key, value);
        }
    }

}
