package com.epam.mobile.testing.driver;

import com.epam.mobile.testing.configuration.ConfigurationReader;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
