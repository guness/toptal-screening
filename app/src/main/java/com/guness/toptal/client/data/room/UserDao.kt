package com.guness.toptal.client.data.room

import androidx.room.*
import com.guness.toptal.protocol.dto.User
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Query("SELECT * FROM ${ToptalDatabase.TABLE_USER}")
    fun users(): Flowable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(list: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Delete
    fun deleteUsers(list: List<User>)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM ${ToptalDatabase.TABLE_USER}")
    fun clear()
}
