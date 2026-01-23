package com.epam.mobile.testing.page;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;

public class WelcomePage extends BasePage{

    By loginBtn = AppiumBy.accessibilityId("Log In");
    By epamLoginBtn = AppiumBy.accessibilityId("Continue with EPAM");
    By linkedInLoginBtn = AppiumBy.accessibilityId("Continue with LinkedIn");
    By appleLoginBtn = AppiumBy.accessibilityId("Continue with Apple");

    public WelcomePage tapLoginBtn() {
        click(loginBtn);
        return this;
    }

    public boolean isEpamLoginDisplayed() {
        return isDisplayed(epamLoginBtn);
    }

    public boolean isLinkedInLoginDisplayed() {
        return isDisplayed(linkedInLoginBtn);
    }

    public boolean isAppleLoginDisplayed() {
        return isDisplayed(appleLoginBtn);
    }
}
