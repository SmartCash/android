package cc.smartcash.smarthub.activities

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import cc.smartcash.smarthub.models.Coin
import org.hamcrest.Description
import org.hamcrest.Matcher


object MyViewAction {
    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }

    fun withMyValue(expectedName: String): Matcher<Any> {
        return object : BoundedMatcher<Any, Coin>(Coin::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("with expectedName: $expectedName")
            }

            override fun matchesSafely(myValue: Coin): Boolean {
                return myValue.name.equals(expectedName, true)
            }
        }
    }
}