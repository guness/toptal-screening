package com.guness.toptal.client.data.repositories

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guness.toptal.client.core.TestInjectTarget
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import tools.ApplicationStub
import tools.TestUtils.application
import tools.mocks.admin1
import tools.mocks.manager1
import tools.mocks.user1

@RunWith(AndroidJUnit4::class)
@Config(application = ApplicationStub::class, manifest = Config.NONE)
class ProfileModelTest {

    private lateinit var profileModel: ProfileModel
    private lateinit var target: TestInjectTarget

    @Before
    fun setUp() {
        target = TestInjectTarget()
        application.injector.inject(target)
        profileModel = ProfileModel(application, target.gson)
    }

    @Test
    fun testProfile() {
        profileModel.postProfile(user1)
        assertTrue(profileModel.observeProfile().blockingFirst().isPresent)

        profileModel.clear()
        assertFalse(profileModel.observeProfile().blockingFirst().isPresent)

        profileModel.postProfile(user1)
        ProfileModel(application, target.gson).also {
            assertTrue(it.observeProfile().blockingFirst().isPresent)
            it.clear()
        }

        ProfileModel(application, target.gson).also {
            assertFalse(it.observeProfile().blockingFirst().isPresent)
        }
    }

    @Test
    fun testClear() {
        profileModel.bearerToken = "Some Random Token"
        profileModel.postProfile(admin1)

        profileModel.clear()
        assertNull(profileModel.bearerToken)
        assertFalse(profileModel.observeSession().blockingFirst())
    }

    @Test
    fun testObserveManager() {
        profileModel.postProfile(user1)
        assertFalse(profileModel.observeManager().blockingFirst())

        profileModel.postProfile(manager1)
        assertTrue(profileModel.observeManager().blockingFirst())

        profileModel.postProfile(admin1)
        assertTrue(profileModel.observeManager().blockingFirst())

        profileModel.clear()
        assertFalse(profileModel.observeManager().blockingFirst())
    }

    @Test
    fun testObserveAdmin() {
        profileModel.postProfile(user1)
        assertFalse(profileModel.observeAdmin().blockingFirst())

        profileModel.postProfile(manager1)
        assertFalse(profileModel.observeAdmin().blockingFirst())

        profileModel.postProfile(admin1)
        assertTrue(profileModel.observeAdmin().blockingFirst())

        profileModel.clear()
        assertFalse(profileModel.observeAdmin().blockingFirst())
    }

    @Test
    fun testObserveSession() {
        assertFalse(profileModel.observeSession().blockingFirst())
        profileModel.postProfile(user1)
        assertTrue(profileModel.observeSession().blockingFirst())
        profileModel.clear()
        assertFalse(profileModel.observeSession().blockingFirst())
    }

    @Test
    fun testBearerToken() {
        assertNull(profileModel.bearerToken)
        profileModel.bearerToken = "RandomToken"

        assertEquals("RandomToken", profileModel.bearerToken)

        ProfileModel(application, target.gson).also {
            assertEquals("RandomToken", it.bearerToken)
            it.clear()
        }

        ProfileModel(application, target.gson).also {
            assertNull(it.bearerToken)
        }
    }
}