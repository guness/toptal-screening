package com.guness.toptal.client.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.guness.toptal.client.R
import com.guness.toptal.client.tools.AwaitUntil.Companion.until
import com.guness.toptal.client.tools.withRecyclerView
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

@LargeTest
class UserCanLoginLogout {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun test() {

        onView(withContentDescription("Navigate up")).perform(click())

        onView(withText(R.string.bottom_nav_login_title))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.username_text)).perform(typeText("user"), closeSoftKeyboard())
        onView(withId(R.id.password_text)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())

        until("User Logs in")
            .matcher(withId(R.id.fab))
            .check(matches(isDisplayed()))
            .await()

        until("Data loads")
            .matcher(withRecyclerView(R.id.listView).atPositionOnView(1, R.id.city))
            .check(matches(withText("Grand Turk")))
            .await()

        onView(withId(R.id.empty_image)).check(matches(not(isDisplayed())))

        onView(withContentDescription("Navigate up")).perform(click())

        onView(withText(R.string.bottom_nav_logout_title))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withContentDescription("Navigate up")).perform(click())
        onView(withText(R.string.bottom_nav_login_title))
            .check(matches(isDisplayed()))
    }
}
