package com.guness.toptal.client.data.room

import androidx.room.*
import com.guness.toptal.protocol.dto.User
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface UserDao {

    @Query("SELECT * FROM ${ToptalDatabase.TABLE_USER}")
    fun users(): Flowable<List<User>>

    /**
     * @Query("SELECT * FROM repo INNER JOIN user_repo_join ON
    repo.id=user_repo_join.repoId WHERE
    user_repo_join.userId=:userId")
     */

    @Query("SELECT u.*, COUNT(DISTINCT t.id) AS entries FROM ${ToptalDatabase.TABLE_USER} u LEFT JOIN ${ToptalDatabase.TABLE_ENTRY} t ON t.userId = u.id GROUP BY 1")
    fun usersWithEntries(): Flowable<List<UserWithEntries>>

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

    @Query("SELECT * FROM  ${ToptalDatabase.TABLE_USER} WHERE id=:userId LIMIT 1")
    fun user(userId: Long): Observable<User>

    @Transaction
    fun setUsers(list: List<User>) {
        clear()
        addUsers(list)
    }
}

data class UserWithEntries(
    @Embedded
    val user: User,
    val entries: Int
)