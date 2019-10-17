package cc.smartcash.wallet.Activities;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest2 {

    final String userName = "1d928cc6-aaa7-4d61-9bb6-f0d2f6fbc03c@testeandroidmobile.com";
    final String password = "123456";
    final String pin = "1234";
    final String AMOUNT = String.valueOf(0.00345);
    final String EXISTENT_EMAIL_ON_WEB_WALLET = "fcb016e9-0408-4e4f-8a01-d86151fc605e@testeandroidmobile.com";
    final String NON_EXISTENT_EMAIL_ON_WEB_WALLET = UUID.randomUUID().toString() + "@testeandroidmobile.com";
    final String EXISTENT_SMS_ON_WEB_WALLET = "+5511900000000";
    final String NON_EXISTENT_SMS_ON_WEB_WALLET = "+5511911111111";
    final String EXISTENT_USERNAME_ON_WEB_WALLET = "teste";
    final String NON_EXISTENT_USERNAME_ON_WEB_WALLET = UUID.randomUUID().toString();
    final String VALID_ADDRESS = "SgPMhNeG16Ty6VaPSnAtxNJAQ2JRnhTGaQ";
    final String INVALID_ADDRESS = UUID.randomUUID().toString();
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class, false, true);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void loginFirstTime() {

        String u = userName;

        try {
            loginAndLogoutTwice(u, true);
            System.out.println("Should pass: " + u);
        } catch (Exception ex) {
            System.out.println("UnExpected error for: " + u + " error: " + ex.getMessage());
        }

        u = EXISTENT_EMAIL_ON_WEB_WALLET;
        try {
            loginAndLogoutTwice(u, true);
            System.out.println("Should pass: " + u);
        } catch (Exception ex) {
            System.out.println("UnExpected error for: " + u + " error: " + ex.getMessage());
        }

        u = NON_EXISTENT_EMAIL_ON_WEB_WALLET;
        try {
            loginAndLogoutTwice(u, false);
            System.out.println("Should NOT pass: " + u);
        } catch (Exception ex) {
            System.out.println("Expected error for: " + u + " error: " + ex.getMessage());
        }

        u = EXISTENT_SMS_ON_WEB_WALLET;
        try {
            loginAndLogoutTwice(u, true);
            System.out.println("Should pass: " + u);
        } catch (Exception ex) {
            System.out.println("UnExpected error for: " + u + " error: " + ex.getMessage());
        }

        u = NON_EXISTENT_SMS_ON_WEB_WALLET;
        try {
            loginAndLogoutTwice(u, false);
            System.out.println("Should NOT pass: " + u);
        } catch (Exception ex) {
            System.out.println("Expected error for: " + u + " error: " + ex.getMessage());
        }


        u = EXISTENT_USERNAME_ON_WEB_WALLET;
        try {
            loginAndLogoutTwice(u, true);
            System.out.println("Should pass: " + u);
        } catch (Exception ex) {
            System.out.println("UnExpected error for: " + u + " error: " + ex.getMessage());
        }


        u = NON_EXISTENT_USERNAME_ON_WEB_WALLET;
        try {
            loginAndLogoutTwice(u, false);
            System.out.println("Should NOT pass: " + u);
        } catch (Exception ex) {
            System.out.println("Expected error for: " + u + " error: " + ex.getMessage());
        }


        u = VALID_ADDRESS;
        try {
            loginAndLogoutTwice(u, true);
            System.out.println("Should pass: " + u);
        } catch (Exception ex) {
            System.out.println("UnExpected error for: " + u + " error: " + ex.getMessage());
        }

        u = INVALID_ADDRESS;
        try {
            loginAndLogoutTwice(u, false);
            System.out.println("Should NOT pass: " + u);
        } catch (Exception ex) {
            System.out.println("Expected error for: " + u + " error: " + ex.getMessage());
        }

        mActivityTestRule.finishActivity();
    }

    public void loginAndLogoutTwice(String user, boolean shouldPass) {
        try {
            firstTimeLogin(user);
            waitABit();
        } catch (NoMatchingViewException e) {
            secondTimeLogin();
            waitABit();
        }

        if (shouldPass) {
            logout();
            waitABit();

            firstTimeLogin(user);
            waitABit();

            logout();
            waitABit();

            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void sendCoinByAddress() {

        Activity activity = mActivityTestRule.getActivity();

        SmartCashApplication app = new SmartCashApplication(activity);

        try {
            firstTimeLogin(null);
            waitABit();
        } catch (NoMatchingViewException e) {
            secondTimeLogin();
            waitABit();
        }

        onView(withId(R.id.nav_send)).perform(click());
        waitABit();

        try {
            onView(withText("Allow")).perform(click());
        } catch (Exception ex) {
        }

        selectWallet();
        waitABit();

        selectOwnWalletToSend();
        waitABit();

        String SAME_ADDRESS_AS_FROM = app.getUser(activity).getWallet().get(0).getAddress();

        sendTo(SAME_ADDRESS_AS_FROM); //Should get an error

        sendTo(EXISTENT_EMAIL_ON_WEB_WALLET); //Should send directly to web wallet account of the user

        sendTo(NON_EXISTENT_EMAIL_ON_WEB_WALLET); //Should use SmartText to send to email box

        sendTo(EXISTENT_SMS_ON_WEB_WALLET); //Should send directly to web wallet account of the user

        sendTo(NON_EXISTENT_SMS_ON_WEB_WALLET); //Should use SmartText to send to SMS message

        sendTo(EXISTENT_USERNAME_ON_WEB_WALLET); //Should send directly to web wallet account of the user

        sendTo(NON_EXISTENT_USERNAME_ON_WEB_WALLET); //Should get an error

        sendTo(VALID_ADDRESS); //Should send directly to web wallet account of the user

        sendTo(INVALID_ADDRESS); //Should get an error
    }

    private void clearSendFields() {
        onView(withId(R.id.send_txt_to_address)).perform(scrollTo(), replaceText(""));
        onView(withId(R.id.txt_amount_converted)).perform(scrollTo(), replaceText(""));
        onView(withId(R.id.txt_amount_converted)).perform(scrollTo(), typeText("0"), closeSoftKeyboard());
    }

    private void sendTo(String address) {

        onView(withId(R.id.nav_send)).perform(click());
        waitABit();

        onView(withId(R.id.send_txt_to_address)).perform(scrollTo(), replaceText(address));

        //Type on field the password and check if it does match
        onView(withId(R.id.txt_amount_converted)).perform(scrollTo(), typeText(AMOUNT));

        closeSoftKeyboard();
        waitABit();

        try {
            onView(withId(R.id.send_button_fragment)).perform(scrollTo(), click());
            waitABit();
        } catch (NoMatchingViewException e) {
            System.out.println(e.getViewMatcherDescription());
        }


        //Type on field the password and check if it does match
        onView(withId(R.id.txt_password)).perform(scrollTo(), replaceText(pin), closeSoftKeyboard());
        waitABit();

        try {
            onView(withId(R.id.send_button_fragment)).perform(scrollTo(), click());
            waitABit();
        } catch (NoMatchingViewException e) {
            System.out.println(e.getViewMatcherDescription());
        }

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Matcher<View> getConstraints() {
        return allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), isDescendantOfA(anyOf(
                isAssignableFrom(ScrollView.class), isAssignableFrom(HorizontalScrollView.class), isAssignableFrom(NestedScrollView.class))));
    }

    private void clearPinAndTwoFaFields() {

        //Type on field the password and check if it does match
        onView(withId(R.id.txt_password)).perform(scrollTo(), replaceText(pin), closeSoftKeyboard());

    }

    private void closeSettings() {

        try {
            onView(withId(R.id.button_close)).perform(click());
        } catch (NoMatchingViewException e) {

        }
    }

    private void openSettings() {
        try {
            onView(withId(R.id.button_settings)).perform(click());
        } catch (NoMatchingViewException e) {

        }
    }

    private void selectCoin() {
        try {
//            onView(withId(R.id.current_price_spinner)).perform(click());
//            waitABit();
//            onData(allOf(is(instanceOf(String.class)), is("BRL")))
//                    .inRoot(isPlatformPopup()).perform(click());//           waitABit();
        } finally {

        }

    }

    private void selectOwnWalletToSend() {
        try {
            onView(withId(R.id.btn_wallet)).perform(click());
            waitABit();

            try {
                onView(withId(R.id.wallet_dialog_cancel_button)).perform(click());
            } catch (NoMatchingViewException e) {

            }
            waitABit();
        } finally {

        }
    }

    private void selectWallet() {
        try {
            onView(withId(R.id.wallet_spinner)).perform(click());
            waitABit();
            onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
            waitABit();
        } finally {

        }
    }

    private void logout() {
        try {
            onView(withId(R.id.button_exit)).perform(click());
            onView(withText(R.string.main_dialog_logout_title)).check(matches(isDisplayed()));
            onView(withId(android.R.id.button1)).perform(click());
        } catch (NoMatchingViewException e) {

        }
    }

    private void firstTimeLogin(String user) {

        //Click on button Enter/Login.
        onView(withId(R.id.btn_eye)).perform(click());

        if (Util.isNullOrEmpty(user)) {
            //Type on field the username and check if it does match
            onView(withId(R.id.txt_user)).perform(replaceText(userName))
                    .check(matches(withText(containsString(userName))));
        } else {
            //Type on field the username and check if it does match
            onView(withId(R.id.txt_user)).perform(replaceText(user))
                    .check(matches(withText(containsString(user))));
        }

        //Type on field the password and check if it does match
        onView(withId(R.id.txt_password)).perform(replaceText(password), closeSoftKeyboard())
                .check(matches(withText(containsString(password))));

        //Click on button Enter/Login.
        onView(withId(R.id.btn_login)).perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        waitABit();

        //Click on button Enter/Login.
        onView(withId(R.id.btn_eye)).perform(click());

        //Type on field the username and check if it does match
        onView(withId(R.id.txt_password)).perform(replaceText(pin))
                .check(matches(withText(containsString(pin))));

        //Type on field the password and check if it does match
        onView(withId(R.id.txt_two_fa)).perform(replaceText(pin), closeSoftKeyboard())
                .check(matches(withText(containsString(pin))));

        //Click on button Enter/Login.
        onView(withId(R.id.btn_confirm)).perform(click());
    }

    private void secondTimeLogin() {
        //Click on button Enter/Login.
        onView(withId(R.id.btn_eye)).perform(click());

        //Click on button Enter/Login.
        onView(withId(R.id.btn_eye)).perform(click());

        //Type on field the password and check if it does match
        onView(withId(R.id.txt_password)).perform(replaceText(pin), closeSoftKeyboard())
                .check(matches(withText(containsString(pin))));

        waitABit();

        //Click on button Enter/Login.
        onView(withId(R.id.btn_confirm)).perform(click());
    }

    private void waitABit() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
