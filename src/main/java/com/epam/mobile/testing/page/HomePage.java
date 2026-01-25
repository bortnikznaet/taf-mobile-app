package com.epam.mobile.testing.page;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    By navigationDrawerBtn = AppiumBy.accessibilityId("Open navigation drawer");
    By listBooksBtn = AppiumBy.androidUIAutomator("new UiSelector().text(\"List books\")");
    By rateFeedbackAppBtn = AppiumBy.androidUIAutomator("new UiSelector().text(\"Rate/Feedback app\")");
    By shareBtn = AppiumBy.androidUIAutomator("new UiSelector().text(\"Share\")");

    public HomePage tapNavigationDrawer() {
        click(navigationDrawerBtn);
        return this;
    }

    public boolean isListBooksDisplayed() {
        return isDisplayed(listBooksBtn);
    }

    public boolean isRateFeedbackAppDisplayed() {
        return isDisplayed(rateFeedbackAppBtn);
    }

    public boolean isShareBtnDisplayed() {
        return isDisplayed(shareBtn);
    }
}
