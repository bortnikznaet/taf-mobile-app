package com.epam.mobile.testing;

import com.epam.mobile.testing.page.WelcomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginOptionsTests extends BaseTest {

    @Test
    public void verifyLoginIsAvailableViaFourProviders() {
        WelcomePage welcomePage = new WelcomePage();
        welcomePage.tapLoginBtn();

        Assert.assertTrue(welcomePage.isEpamLoginDisplayed(), "EPAM button is NOT displayed");
        Assert.assertTrue(welcomePage.isLinkedInLoginDisplayed(), "LinkedIn button is NOT displayed");
        Assert.assertTrue(welcomePage.isAppleLoginDisplayed(), "Apple button is NOT displayed");
    }
}
