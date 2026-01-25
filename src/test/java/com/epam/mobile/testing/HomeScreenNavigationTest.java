package com.epam.mobile.testing;

import com.epam.mobile.testing.page.HomePage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HomeScreenNavigationTest extends BaseTest {

    @Test
    public void verifyLoginIsAvailableViaFourProviders() {
        HomePage homePage = new HomePage();
        homePage.tapNavigationDrawer();

        SoftAssert soft = new SoftAssert();

        soft.assertTrue(homePage.isListBooksDisplayed(), "List Books button is NOT displayed");
        soft.assertTrue(homePage.isRateFeedbackAppDisplayed(), "Rate / Feedback App button is NOT displayed");
        soft.assertTrue(homePage.isShareBtnDisplayed(), "Share button is NOT displayed");

        soft.assertAll();
    }
}
