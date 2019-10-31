package cc.smartcash.wallet.Activities

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import cc.smartcash.wallet.R
import cc.smartcash.wallet.Utils.SmartCashApplication
import cc.smartcash.wallet.Utils.Util
import cc.smartcash.wallet.ViewHolders.TransactionViewHolder
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest2 {

    internal val userName = "1d928cc6-aaa7-4d61-9bb6-f0d2f6fbc03c@testeandroidmobile.com"
    internal val password = "123456"
    internal val pin = "1234"
    internal val AMOUNT = 0.00345.toString()
    internal val EXISTENT_EMAIL_ON_WEB_WALLET = "fcb016e9-0408-4e4f-8a01-d86151fc605e@testeandroidmobile.com"
    internal val NON_EXISTENT_EMAIL_ON_WEB_WALLET = UUID.randomUUID().toString() + "@testeandroidmobile.com"
    internal val EXISTENT_SMS_ON_WEB_WALLET = "+5511900000000"
    internal val NON_EXISTENT_SMS_ON_WEB_WALLET = "+5511911111111"
    internal val EXISTENT_USERNAME_ON_WEB_WALLET = "teste"
    internal val NON_EXISTENT_USERNAME_ON_WEB_WALLET = UUID.randomUUID().toString()
    internal val VALID_ADDRESS = "SgPMhNeG16Ty6VaPSnAtxNJAQ2JRnhTGaQ"
    internal val INVALID_ADDRESS = UUID.randomUUID().toString()


    @get:Rule
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java, false, true)

    @Test
    fun testLoginWithPin() {
        loginFirstTime(false)
    }

    @Test
    fun testLoginWithoutPin() {
        loginFirstTime(true)
    }

    @Test
    fun testSendingCoinsWithPin() {
        sendCoins(false)
    }

    @Test
    fun testSendingCoinsWithoutPin() {
        sendCoins(true)
    }

    @Test
    fun testLoginOnTwoFaAccountWithoutTypeTwoFa() {

        try {
            loginWithUserAndPassword("teste2famobile")
            waitABit()
        } catch (e: NoMatchingViewException) {
            loginOnlyWithPIN()
            waitABit()
        }
    }

    @Test
    fun testNavigationOnDialogs() {

        try {
            loginWithUserAndPassword(null)
            waitABit()
        } catch (e: NoMatchingViewException) {
            loginOnlyWithPIN()
            waitABit()
        }

        try {

            openSettings()

            selectCoin()

            closeSettings()


        } finally {
            waitABit()
        }

        waitABit()

    }

    private fun loginFirstTime(withoutPin: Boolean = false) {

        var u = userName

        try {
            loginAndLogoutTwice(u, true, withoutPin)
            println("Should pass: $u")
        } catch (ex: Exception) {
            println("UnExpected error for: " + u + " error: " + ex.message)
        }

        u = EXISTENT_EMAIL_ON_WEB_WALLET
        try {
            loginAndLogoutTwice(u, true, withoutPin)
            println("Should pass: $u")
        } catch (ex: Exception) {
            println("UnExpected error for: " + u + " error: " + ex.message)
        }

        u = NON_EXISTENT_EMAIL_ON_WEB_WALLET
        try {
            loginAndLogoutTwice(u, false, withoutPin)
            println("Should NOT pass: $u")
        } catch (ex: Exception) {
            println("Expected error for: " + u + " error: " + ex.message)
        }

        u = EXISTENT_SMS_ON_WEB_WALLET
        try {
            loginAndLogoutTwice(u, true, withoutPin)
            println("Should pass: $u")
        } catch (ex: Exception) {
            println("UnExpected error for: " + u + " error: " + ex.message)
        }

        u = NON_EXISTENT_SMS_ON_WEB_WALLET
        try {
            loginAndLogoutTwice(u, false, withoutPin)
            println("Should NOT pass: $u")
        } catch (ex: Exception) {
            println("Expected error for: " + u + " error: " + ex.message)
        }


        u = EXISTENT_USERNAME_ON_WEB_WALLET
        try {
            loginAndLogoutTwice(u, true, withoutPin)
            println("Should pass: $u")
        } catch (ex: Exception) {
            println("UnExpected error for: " + u + " error: " + ex.message)
        }


        u = NON_EXISTENT_USERNAME_ON_WEB_WALLET
        try {
            loginAndLogoutTwice(u, false, withoutPin)
            println("Should NOT pass: $u")
        } catch (ex: Exception) {
            println("Expected error for: " + u + " error: " + ex.message)
        }


        u = VALID_ADDRESS
        try {
            loginAndLogoutTwice(u, true, withoutPin)
            println("Should pass: $u")
        } catch (ex: Exception) {
            println("UnExpected error for: " + u + " error: " + ex.message)
        }

        u = INVALID_ADDRESS
        try {
            loginAndLogoutTwice(u, false, withoutPin)
            println("Should NOT pass: $u")
        } catch (ex: Exception) {
            println("Expected error for: " + u + " error: " + ex.message)
        }

        mActivityTestRule.finishActivity()
    }

    private fun loginAndLogoutTwice(user: String, shouldPass: Boolean, withoutPin: Boolean = false) {
        try {
            loginWithUserAndPassword(user, withoutPin)
            waitABit()
        } catch (e: NoMatchingViewException) {
            if (withoutPin) {
                loginOnlyWithPIN()
                waitABit()
            }
        }

        if (shouldPass) {
            logout()
            waitABit()

            loginWithUserAndPassword(user)
            waitABit()

            logout()
            waitABit()

            try {
                Thread.sleep(7000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    private fun sendCoins(withoutPin: Boolean = false) {

        val activity = mActivityTestRule.activity

        val app = SmartCashApplication(activity)

        try {
            loginWithUserAndPassword(null, withoutPin)
            waitABit()
        } catch (e: NoMatchingViewException) {
            loginOnlyWithPIN()
            waitABit()
        }

        onView(withId(R.id.nav_send)).perform(click())
        waitABit()

        try {
            onView(withText("Allow")).perform(click())
        } catch (ex: Exception) {
        }

        selectWallet()
        waitABit()

        selectOwnWalletToSend()
        waitABit()

        val SAME_ADDRESS_AS_FROM = app.getUser()!!.wallet!![0].address

        sendTo(SAME_ADDRESS_AS_FROM, withoutPin) //Should get an error

        sendTo(EXISTENT_EMAIL_ON_WEB_WALLET, withoutPin) //Should send directly to web wallet account of the user

        sendTo(NON_EXISTENT_EMAIL_ON_WEB_WALLET, withoutPin) //Should use SmartText to send to email box

        sendTo(EXISTENT_SMS_ON_WEB_WALLET, withoutPin) //Should send directly to web wallet account of the user

        sendTo(NON_EXISTENT_SMS_ON_WEB_WALLET, withoutPin) //Should use SmartText to send to SMS message

        sendTo(EXISTENT_USERNAME_ON_WEB_WALLET, withoutPin) //Should send directly to web wallet account of the user

        sendTo(NON_EXISTENT_USERNAME_ON_WEB_WALLET, withoutPin) //Should get an error

        sendTo(VALID_ADDRESS, withoutPin) //Should send directly to web wallet account of the user

        sendTo(INVALID_ADDRESS, withoutPin) //Should get an error
    }

    private fun clearSendFields() {
        onView(withId(R.id.send_txt_to_address)).perform(scrollTo(), replaceText(""))
        onView(withId(R.id.fragment_send_txt_amount_converted)).perform(scrollTo(), replaceText(""))
        onView(withId(R.id.fragment_send_txt_amount_converted)).perform(scrollTo(), typeText("0"), closeSoftKeyboard())
    }

    private fun sendTo(address: String?, withoutPin: Boolean = false) {

        onView(withId(R.id.nav_send)).perform(click())
        waitABit()

        onView(withId(R.id.send_txt_to_address)).perform(scrollTo(), replaceText(address!!))

        //Type on field the password and check if it does match
        onView(withId(R.id.fragment_send_txt_amount_converted)).perform(scrollTo(), typeText(AMOUNT), closeSoftKeyboard())
        waitABit()

        try {
            onView(withId(R.id.send_button_fragment)).perform(scrollTo(), click())
            waitABit()
        } catch (e: NoMatchingViewException) {
            println(e.viewMatcherDescription)
        }

        if (withoutPin) {
            //Type on field the password and check if it does match
            onView(withId(R.id.login_main_txt_password)).perform(scrollTo(), replaceText(password), closeSoftKeyboard())
            waitABit()
        } else {
            //Type on field the password and check if it does match
            onView(withId(R.id.login_main_txt_password)).perform(scrollTo(), replaceText(pin), closeSoftKeyboard())
            waitABit()
        }

        try {
            onView(withId(R.id.send_button_fragment)).perform(scrollTo(), click())
            waitABit()
        } catch (e: NoMatchingViewException) {
            println(e.viewMatcherDescription)
        }

        try {
            tryToInteractWithTransactionHistory()
        } finally {

        }

        try {
            Thread.sleep(60000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun tryToInteractWithTransactionHistory() {
        waitABit()
        waitABit()
        try {
            onView(withId(R.id.transaction_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<TransactionViewHolder>(0, scrollTo()))
            waitABit()

            onView(withId(R.id.transaction_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<TransactionViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.transaction_item_btn_open_details)))
            waitABit()

            onView(withId(R.id.transaction_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<TransactionViewHolder>(1, scrollTo()))
            waitABit()

            onView(withId(R.id.transaction_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<TransactionViewHolder>(1, MyViewAction.clickChildViewWithId(R.id.transaction_item_btn_open_details)))
            waitABit()

        } catch (e: NoMatchingViewException) {
            println(e.viewMatcherDescription)
        }

    }

    private fun clearPinAndTwoFaFields() {

        //Type on field the password and check if it does match
        onView(withId(R.id.login_main_txt_password)).perform(scrollTo(), replaceText(pin), closeSoftKeyboard())

    }

    private fun closeSettings() {

        try {
            onView(withId(R.id.settings_modal_button_close)).perform(click())
        } catch (e: NoMatchingViewException) {

        }

    }

    private fun openSettings() {
        try {
            onView(withId(R.id.toolbar_button_settings)).perform(click())
        } catch (e: NoMatchingViewException) {

        }
    }

    private fun selectCoin() {
        try {


        } finally {

        }

    }

    private fun selectOwnWalletToSend() {
        try {
            onView(withId(R.id.btn_wallet)).perform(click())
            waitABit()

            try {
                onView(withId(R.id.show_wallets_dialog_scan_qrcode_dialog_cancel_button)).perform(click())
            } catch (e: NoMatchingViewException) {

            }

            waitABit()
        } finally {

        }
    }

    private fun selectWallet() {
        try {
            onView(withId(R.id.fragment_send_wallet_spinner)).perform(click())
            waitABit()
            onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click())
            waitABit()
        } finally {

        }
    }

    private fun logout() {
        try {
            onView(withId(R.id.toolbar_button_exit)).perform(click())
            onView(withText(R.string.main_dialog_logout_title)).check(matches(isDisplayed()))
            onView(withId(android.R.id.button1)).perform(click())
        } catch (e: NoMatchingViewException) {

        }
    }

    private fun loginWithUserAndPassword(user: String?, withoutPin: Boolean = false) {

        //Click on button Enter/Login.
        onView(withId(R.id.login_main_btn_eye)).perform(click())

        if (Util.isNullOrEmpty(user)) {
            //Type on field the username and check if it does match
            onView(withId(R.id.login_main_txt_user)).perform(replaceText(userName))
                    .check(matches(withText(containsString(userName))))
        } else {
            //Type on field the username and check if it does match
            onView(withId(R.id.login_main_txt_user)).perform(replaceText(user!!))
                    .check(matches(withText(containsString(user))))
        }

        //Type on field the password and check if it does match
        onView(withId(R.id.login_main_txt_password)).perform(replaceText(password), closeSoftKeyboard())
                .check(matches(withText(containsString(password))))

        //Click on button Enter/Login.
        onView(withId(R.id.login_main_btn_login)).perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        waitABit()

        if (withoutPin)
            clickOnContinueWithoutPin()
        else
            fillPINThenPinConfirmationAndSavePIN()
    }

    private fun loginOnlyWithPIN() {
        //Click on button Enter/Login.
        onView(withId(R.id.pin_activity_btn_eye)).perform(click())

        //Click on button Enter/Login.
        onView(withId(R.id.pin_activity_btn_eye)).perform(click())

        //Type on field the password and check if it does match
        onView(withId(R.id.pin_activity_txt_pin)).perform(replaceText(pin), closeSoftKeyboard())
                .check(matches(withText(containsString(pin))))

        waitABit()

        //Click on button Enter/Login.
        onView(withId(R.id.pin_activity_btn_confirm)).perform(click())
    }

    private fun clickOnContinueWithoutPin() {
        //Click on button Enter/Login.
        onView(withId(R.id.pin_activity_continue_without_pin)).perform(click())

        //Confirm the dialog clicking on OK
        onView(withText("OK")).perform(click())
    }

    private fun fillPINThenPinConfirmationAndSavePIN() {
        //Click on button Enter/Login.
        onView(withId(R.id.pin_activity_btn_eye)).perform(click())

        //Type on field the username and check if it does match
        onView(withId(R.id.pin_activity_txt_pin)).perform(replaceText(pin))
                .check(matches(withText(containsString(pin))))

        //Type on field the password and check if it does match
        onView(withId(R.id.pin_activity_txt_pin_confirmation)).perform(replaceText(pin), closeSoftKeyboard())
                .check(matches(withText(containsString(pin))))

        //Click on button Enter/Login.
        onView(withId(R.id.pin_activity_btn_confirm)).perform(click())
    }

    private fun waitABit() {
        try {
            Thread.sleep(700)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

}
