package com.guness.toptal.client.tools

import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import junit.framework.AssertionFailedError
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import java.util.concurrent.TimeoutException

fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                description.appendText("with id: ${getResourceName(recyclerViewId)}, at position: $position")
                if (targetViewId > -1) {
                    description.appendText(" with child: ${getResourceName(targetViewId)}")
                }
            }

            public override fun matchesSafely(view: View): Boolean {

                this.resources = view.resources

                if (childView == null) {
                    val recyclerView = view.rootView.findViewById<View>(recyclerViewId) as? RecyclerView
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView!!.findViewById<View>(targetViewId)
                    view === targetView
                }
            }

            private fun getResourceName(@IdRes id: Int): String {
                return resources?.let {
                    try {
                        resources!!.getResourceName(id)
                    } catch (e: Resources.NotFoundException) {
                        String.format("%s (resource name not found)", Integer.valueOf(id))
                    }
                } ?: id.toString()
            }

        }
    }
}


typealias AwaitUntilCondition = () -> Boolean
typealias AwaitUntilAction = () -> Any

class AwaitUntil(val message: String?) {

    private var viewMatcher: Matcher<View>? = null

    private var viewInteraction: ViewInteraction? = null

    private var dataInteraction: DataInteraction? = null

    private var viewAssertion: ViewAssertion? = null

    private var condition: AwaitUntilCondition? = null

    private var viewAction: ViewAction? = null

    private var action: AwaitUntilAction? = null

    private var errorMessage: String? = null

    private var interval = 200L

    private var delay = 0L

    companion object {
        fun until() = AwaitUntil(null)
        fun until(message: String) = AwaitUntil(message)
    }

    fun matcher(matcher: Matcher<View>): AwaitUntil {
        viewMatcher = if (viewMatcher != null) {
            CoreMatchers.allOf(viewMatcher, matcher)
        } else {
            matcher
        }
        return this
    }

    fun view(viewInteraction: ViewInteraction): AwaitUntil {
        this.viewInteraction = viewInteraction
        return this
    }

    fun check(assertion: ViewAssertion): AwaitUntil {
        viewAssertion = assertion
        return this
    }

    fun check(condition: AwaitUntilCondition): AwaitUntil {
        this.condition = condition
        return this
    }

    fun atInterval(interval: Long): AwaitUntil {
        this.interval = interval
        return this
    }

    fun withDelay(delay: Long): AwaitUntil {
        this.delay = delay
        return this
    }

    fun withAction(action: ViewAction): AwaitUntil {
        this.viewAction = action
        return this
    }

    fun withAction(action: AwaitUntilAction): AwaitUntil {
        this.action = action
        return this
    }

    fun await(timeout: Long = 10000L) {
        checkPreconditions()

        if (delay > 0L) {
            Thread.sleep(delay)
        }

        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeout
        do {
            if (isFulfilled()) {
                performAction()
                return
            }
            Thread.sleep(interval)

        } while (System.currentTimeMillis() < endTime)

        val failMessage = if (message != null) message + "\n" else ""
        Assert.fail(failMessage + errorMessage)
    }

    private fun isFulfilled(): Boolean {
        return try {
            viewInteraction?.check(viewAssertion)
            dataInteraction?.check(viewAssertion)
            condition?.invoke() ?: true
        } catch (e: AssertionFailedError) {
            errorMessage = e.localizedMessage
            false
        } catch (e: RuntimeException) {
            errorMessage = e.localizedMessage
            false
        }
        // otherwise let it crash
    }

    private fun performAction() {
        if (viewAction != null) {
            viewInteraction?.perform(viewAction)
            dataInteraction?.perform(viewAction)
        }
        if (action != null) {
            action!!.invoke()
        }
    }

    private fun checkPreconditions() {
        if (viewInteraction == null && viewMatcher != null) {
            viewInteraction = Espresso.onView(viewMatcher)
        }
        if (viewInteraction == null && dataInteraction == null && condition == null) {
            throw IllegalStateException("Need at least one condition to perform check on")
        }
        if (viewAssertion == null && (viewInteraction != null || dataInteraction != null)) {
            viewAssertion = ViewAssertions.matches(ViewMatchers.isDisplayed())
        }
    }
}