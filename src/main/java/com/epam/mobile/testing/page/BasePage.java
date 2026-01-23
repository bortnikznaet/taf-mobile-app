package com.epam.mobile.testing.page;

import com.epam.mobile.testing.driver.DriverManager;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.epam.mobile.testing.driver.DriverManager.driver;

public class BasePage {
    protected WebDriverWait wait;

    public  BasePage() {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory
                .initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

}
