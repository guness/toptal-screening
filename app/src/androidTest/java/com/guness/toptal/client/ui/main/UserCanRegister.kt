package com.guness.toptal.client.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.guness.toptal.client.R
import com.guness.toptal.client.tools.AwaitUntil.Companion.until
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random


@LargeTest
class UserCanRegister {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun userCanLogin() {

        onView(withContentDescription("Navigate up")).perform(click())

        onView(withText(R.string.bottom_nav_register_title))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.name_text)).perform(typeText("Sinan Gunes"), closeSoftKeyboard())
        val random = Random(0).nextInt(10000, 99999)
        onView(withId(R.id.username_text)).perform(typeText("user_$random"), closeSoftKeyboard())
        onView(withId(R.id.password_text)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())

        until("User Registers")
            .matcher(withId(R.id.fab))
            .check(matches(isDisplayed()))
            .await()

        onView(withId(R.id.empty_image)).check(matches(isDisplayed()))
    }
}
