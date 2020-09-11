package com.zq.zqplayer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.zq.zqplayer.database.bean.User

@Dao
interface UserDao {
    //查询user表中所有数据
    @get:Query("SELECT * FROM user")
    val all: List<User?>?

    @Query("SELECT * FROM user WHERE 'uid' IN (:userIds)")
    fun loadAllByIds(userIds: IntArray?): List<User?>?

    //    @Query("SELECT * FROM user   LIMIT 1")
    //    void findUser(User user);

    @Query("SELECT * FROM User LIMIT 1")
    fun findUser(): User?

    @Insert
    fun insert(users: User?)

    @Delete
    fun delete(vararg users: User?)

    @Query("DELETE FROM User")
    fun deleteAllUser()
}