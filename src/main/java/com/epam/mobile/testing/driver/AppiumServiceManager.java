package com.epam.mobile.testing.driver;

import com.epam.mobile.testing.configuration.ConfigurationReader;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.LOG_LEVEL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.SESSION_OVERRIDE;

public class AppiumServiceManager {

    private static final Logger LOG = LogManager.getLogger(AppiumServiceManager.class);
    private static final String ERROR_LOG_LEVEL = "error";
    private static final String KILL_NODE_COMMAND = "taskkill /F /IM node.exe";
    private static AppiumDriverLocalService appiumDriverLocalService;

    private AppiumServiceManager() {
    }

    public static void getAppiumDriverLocalService(int port) {
        if (appiumDriverLocalService == null || !appiumDriverLocalService.isRunning()) {
            startService(port);
        }
    }

    public static void startService(int port) {
        if (appiumDriverLocalService != null && appiumDriverLocalService.isRunning()) {
            LOG.info("Appium server already running on {}", appiumDriverLocalService.getUrl());
            return;
        }

        makePortAvailableIfOccupied(port);

        appiumDriverLocalService = new AppiumServiceBuilder()
                .withIPAddress(ConfigurationReader.get().appiumAddress())
                .usingPort(port)
                .withArgument(SESSION_OVERRIDE)
                .withArgument(LOG_LEVEL, ERROR_LOG_LEVEL)
                .build();

        appiumDriverLocalService.start();
        LOG.info("Appium server started on port {}", port);
    }

    public static void stopService() {
        Optional.ofNullable(appiumDriverLocalService).ifPresent(service -> {
            try {
                service.stop();
                LOG.info("Appium server stopped");
            } catch (Exception e) {
                LOG.warn("Failed to stop Appium server: {}", e.getMessage());
            } finally {
                appiumDriverLocalService = null;
            }
        });
    }

    private static void makePortAvailableIfOccupied(int port) {
        if (!isPortFree(port)) {
            LOG.warn("Port {} is occupied. Trying to free it by killing Node processes.", port);
            try {
                Runtime.getRuntime().exec(KILL_NODE_COMMAND);
                LOG.info("Executed command: {}", KILL_NODE_COMMAND);
            } catch (IOException e) {
                LOG.error("Couldn't execute command '{}'. Message: {}", KILL_NODE_COMMAND, e.getMessage());
            }
        }
    }

    private static boolean isPortFree(int port) {
        boolean isFree = true;
        try (ServerSocket ignored = new ServerSocket(port)) {
            LOG.info("Specified port - {} is available and ready to use", port);
        } catch (Exception e) {
            isFree = false;
            LOG.warn("Specified port - {} is occupied by some process, process will be terminated", port);
        }
        return isFree;
    }

}
