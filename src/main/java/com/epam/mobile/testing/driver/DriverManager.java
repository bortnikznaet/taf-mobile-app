package com.epam.mobile.testing.driver;

import com.epam.mobile.testing.configuration.ConfigurationReader;
import com.epam.mobile.testing.configuration.EnvironmentType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;

public class DriverManager {

    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    public static AppiumDriver driver;

    private DriverManager() {}

    public static AppiumDriver getDriver() {
        if (driver == null) {
            driver = createDriver();
            LOG.info("Driver session started. sessionId={}", driver.getSessionId());
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver == null) {
            return;
        }
        try {
            LOG.info("Quitting driver session. sessionId={}", driver.getSessionId());
            driver.quit();
        } catch (Exception e) {
            LOG.warn("Failed to quit driver gracefully: {}", e.getMessage());
        } finally {
            driver = null;
        }
    }

    private static AppiumDriver createDriver() {
        EnvironmentType envType = resolveEnvironmentType();
        LOG.info("Creating Appium driver for envType={}", envType);

        switch (envType) {
            case LOCAL:
                return createAndroidLocalDriver();
            default:
                throw new IllegalArgumentException("Unsupported env: " + envType);
        }
    }

    private static EnvironmentType resolveEnvironmentType() {
        String rawEnv = ConfigurationReader.get().env();
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

    private static AppiumDriver createAndroidLocalDriver() {
        ConfigurationReader cfg = ConfigurationReader.get();

        UiAutomator2Options options = CapabilitiesConfigurator.getLocalCapabilities()
                .setNoReset(true);

        if (cfg.appPackage() != null && !cfg.appPackage().isBlank()) {
            options.setAppPackage(cfg.appPackage());
        }
        if (cfg.appActivity() != null && !cfg.appActivity().isBlank()) {
            options.setAppActivity(cfg.appActivity());
        }

        options.setCapability("appium:uiautomator2ServerLaunchTimeout", 90000);
        options.setCapability("appium:uiautomator2ServerInstallTimeout", 90000);
        options.setCapability("appium:adbExecTimeout", 60000);
        options.setCapability("appium:newCommandTimeout", 120);

        options.setCapability("appium:avdArgs", "-no-snapshot-load -no-snapshot-save -no-boot-anim");
        options.setCapability("appium:avdLaunchTimeout", 180000);
        options.setCapability("appium:avdReadyTimeout", 180000);

        URL serverUrl = buildAppiumUrl(cfg.appiumAddress(), cfg.appiumPort());
        LOG.info("Connecting to Appium server: {}", serverUrl);

        return new AndroidDriver(serverUrl, options);
    }

    private static URL buildAppiumUrl(String host, int port) {
        String safeHost = (host == null || host.isBlank()) ? "127.0.0.1" : host.trim();
        int safePort = port > 0 ? port : 4723;

        try {
            return new URL("http", safeHost, safePort, "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Wrong Appium server URL. host=" + safeHost + ", port=" + safePort, e);
        }
    }

    public static void closeApp() {
        if (driver == null) {
            return;
        }

        String appPackage = ConfigurationReader.get().appPackage();
        if (appPackage == null || appPackage.isBlank()) {
            LOG.warn("appPackage is not set. Skip terminateApp.");
            return;
        }

        try {
            ((InteractsWithApps) driver).terminateApp(appPackage);
            LOG.info("App was terminated. package={}", appPackage);
        } catch (Exception e) {
            LOG.warn("Failed to terminate app. package={}, message={}", appPackage, e.getMessage());
        }
    }

    public static void closeAppium() {
        AppiumServiceManager.stopService();
    }

    public static void closeEmulator() {
        String udid = ConfigurationReader.get().udid();
        if (udid == null || udid.isBlank()) {
            LOG.info("UDID is not set. Skip closing emulator.");
            return;
        }

        if (!udid.startsWith("emulator-")) {
            LOG.info("UDID '{}' is not an emulator. Skip closing emulator.", udid);
            return;
        }

        try {
            Runtime.getRuntime().exec(format("adb -s %s emu kill", udid));
            waitUntilEmulatorIsDown(udid, 30);
            LOG.info("AVD was closed. udid={}", udid);
        } catch (Exception e) {
            LOG.warn("AVD was not closed. udid={}, message={}", udid, e.getMessage());
        }
    }

    private static void waitUntilEmulatorIsDown(String udid, int timeoutSeconds) {
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < end) {
            String devices = null;
            try {
                devices = Runtime.getRuntime().exec(format("adb devices")).toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (devices == null || !devices.contains(udid)) {
                return;
            }
        }

        LOG.warn("Emulator is still visible in adb after {} seconds. udid={}", timeoutSeconds, udid);
    }
}
