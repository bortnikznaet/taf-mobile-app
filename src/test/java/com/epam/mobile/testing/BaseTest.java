package com.epam.mobile.testing;

import com.epam.mobile.testing.configuration.ConfigurationReader;
import com.epam.mobile.testing.driver.AppiumServiceManager;
import com.epam.mobile.testing.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    private static final Logger LOG = LogManager.getLogger(BaseTest.class);
    protected AppiumDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void startAppium() {
        ConfigurationReader cfg = ConfigurationReader.get();

        LOG.info("Starting Appium server...");
        AppiumServiceManager.getAppiumDriverLocalService(cfg.appiumPort());
        LOG.info("Appium server started successfully");
    }

    @BeforeClass(alwaysRun = true)
    public void createSession() {
        LOG.info("Starting test session...");
        driver = DriverManager.getDriver();
    }

    @AfterClass(alwaysRun = true)
    public void resetApp() {
        LOG.info("Cleaning up after tests...");

        DriverManager.closeApp();
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void stopAppium() {
        DriverManager.closeEmulator();
        DriverManager.closeAppium();
    }
}
