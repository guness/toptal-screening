package com.guness.toptal.client.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.guness.toptal.client.R
import com.guness.toptal.client.tools.AwaitUntil.Companion.until
import com.guness.toptal.client.tools.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

@LargeTest
class UserCanCRUDEntry {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun test() {
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.input_edit_text)).perform(typeText("Darwin"))
        onView(withId(android.R.id.text1)).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.button)).perform(click())

        onView(withId(R.id.empty_image)).check(matches(not(isDisplayed())))

        until("List loads")
            .matcher(withRecyclerView(R.id.listView).atPositionOnView(1, R.id.city))
            .check(matches(withText("Darwin")))
            .withAction(click())
            .withDelay(1000)
            .await()

        onView(withId(R.id.input_edit_text)).perform(clearText(), typeText("New York"))
        onView(withId(android.R.id.text1)).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.button)).perform(click())

        until("List loads")
            .matcher(withRecyclerView(R.id.listView).atPositionOnView(1, R.id.city))
            .check(matches(withText("New York")))
            .withAction(click())
            .withDelay(1000)
            .await()

        onView(withId(R.id.delete)).perform(click())

        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click())

        onView(withId(R.id.empty_image)).check(matches(isDisplayed()))
    }
}

