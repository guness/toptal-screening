package com.guness.toptal.client.data

import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.request.CreateUserRequest
import com.guness.toptal.protocol.request.LoginRequest
import com.guness.toptal.protocol.response.CreateUserResponse
import com.guness.toptal.protocol.response.GetEntriesResponse
import com.guness.toptal.protocol.response.GetUsersResponse
import com.guness.toptal.protocol.response.LoginResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface WebService {

    // -- Auth --
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Single<LoginResponse>

    @POST("auth/logout")
    fun logout(): Completable

    @POST("auth/register")
    fun register(@Body request: CreateUserRequest): Single<CreateUserResponse>

    // -- User --
    @POST("user/create")
    fun createUser(@Body request: CreateUserRequest): Single<User>

    @DELETE("user/{id}")
    fun deleteUser(@Path("id") uid: String): Completable

    @PUT("users")
    fun updateUser(@Body request: User): Single<User>

    @GET("users")
    fun getUsers(): Single<GetUsersResponse>

    @GET("user")
    fun getUser(): Single<User>

    // -- Time Entry --
    @POST("zone")
    fun createEntry(@Body request: TimeEntry, @Query("userId") userId: String? = null): Single<TimeEntry>

    @DELETE("zone/{id}")
    fun deleteEntry(@Path("id") uid: String): Completable

    @PUT("zone")
    fun updateEntry(@Body request: TimeEntry): Single<TimeEntry>

    @GET("zone")
    fun getEntries(): Single<GetEntriesResponse>
}