package com.guness.toptal.client.data

import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.request.CreateUserRequest
import com.guness.toptal.protocol.request.LoginRequest
import com.guness.toptal.protocol.request.UpdateEntryRequest
import com.guness.toptal.protocol.request.UpdateUserRequest
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
    @POST("user")
    fun createUser(@Body request: CreateUserRequest): Single<User>

    @DELETE("user/{id}")
    fun deleteUser(@Path("id") id: Long): Completable

    @PUT("user/{id}")
    fun updateUser(@Path("id") id: Long, @Body request: UpdateUserRequest): Single<User>

    @PUT("user")
    fun updateUser(@Body request: UpdateUserRequest): Single<User>

    @GET("user/all")
    fun getUsers(): Single<GetUsersResponse>

    @GET("user")
    fun getUser(): Single<User>

    // -- Time Entry --
    @POST("zone")
    fun createEntry(@Body request: TimeEntry, @Query("userId") userId: Long? = null): Single<TimeEntry>

    @DELETE("zone/{id}")
    fun deleteEntry(@Path("id") id: Long): Completable

    @PUT("zone/{id}")
    fun updateEntry(@Path("id") id: Long, @Body request: UpdateEntryRequest): Single<TimeEntry>

    @GET("zone")
    fun getEntries(): Single<GetEntriesResponse>
}