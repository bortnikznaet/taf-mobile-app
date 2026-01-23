package com.epam.mobile.testing;

import com.epam.mobile.testing.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    private static final Logger LOG = LogManager.getLogger(BaseTest.class);
    protected AppiumDriver driver;

    @BeforeClass
    public void createSession() {
        LOG.info("Starting test session...");
        driver = DriverManager.getDriver();
    }

    @AfterClass
    public void resetApp() {
        LOG.info("Cleaning up after tests...");

        DriverManager.closeApp();
        DriverManager.quitDriver();
        DriverManager.closeAppium();
        DriverManager.closeEmulator();
    }
}
