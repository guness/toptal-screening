package com.guness.toptal.client.data.repositories

import android.content.Context
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.UserRole
import com.guness.toptal.protocol.dto.manager
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val context: Context, private val gson: Gson) {

    private var profile = BehaviorSubject.createDefault<Optional<User>>(Optional.empty())
    private val session = BehaviorSubject.createDefault(false)
    private val sharedPreferences = context.getSharedPreferences("AuthStorage", Context.MODE_PRIVATE)

    init {
        restoreUser()?.let(::postProfile)
    }

    @WorkerThread
    fun postProfile(user: User) {
        sharedPreferences.edit().putString(USER_KEY, gson.toJson(user)).apply()
        profile.onNext(Optional.of(user))
        session.onNext(true)
    }

    @WorkerThread
    fun clear() {
        profile.onNext(Optional.empty())
        session.onNext(false)
        sharedPreferences.edit().remove(USER_KEY).apply()
        bearerToken = null
    }

    fun observeProfile() = profile.hide()
    fun observeManager() = profile.map { it.map { it.role.manager }.orElseGet { false } }
    fun observeAdmin() = profile.map { it.map { it.role == UserRole.ROLE_ADMIN }.orElseGet { false } }
    fun observeSession() = session.hide().distinctUntilChanged()

    var bearerToken: String? = sharedPreferences.getString(BEARER_KEY, null)
        set(value) {
            sharedPreferences.edit().putString(BEARER_KEY, value).apply()
            field = value
        }

    private fun restoreUser(): User? = sharedPreferences.getString(USER_KEY, null)
        ?.let {
            try {
                gson.fromJson<User>(it, User::class.java)
            } catch (e: Exception) {
                Timber.e(e, "Error restoring user object")
                null
            }
        }


    companion object {
        const val BEARER_KEY = "BEARER_KEY"
        const val USER_KEY = "USER_KEY"
    }
}