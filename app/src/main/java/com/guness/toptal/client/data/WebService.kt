package com.guness.toptal.client.data

import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.request.CreateEntryRequest
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

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Single<LoginResponse>

    @POST("auth/logout")
    fun logout(): Completable

    @POST("users/create")
    fun createUser(@Body request: CreateUserRequest): Single<CreateUserResponse>

    @DELETE("users/{uid}")
    fun deleteUser(@Path("id") uid: String): Completable

    @PUT("users")
    fun updateUser(@Body request: User): Single<User>

    @GET("users")
    fun getUsers(): Single<GetUsersResponse>

    @GET("users/current")
    fun getUser(): Single<User>

    @POST("entries/create")
    fun createEntry(@Body request: CreateEntryRequest): Single<TimeEntry>

    @DELETE("entries/{uid}")
    fun deleteEntry(@Path("id") uid: String): Completable

    @PUT("entries")
    fun updateEntry(@Body request: TimeEntry): Single<TimeEntry>

    @GET("entries")
    fun getEntries(): Single<GetEntriesResponse>
}