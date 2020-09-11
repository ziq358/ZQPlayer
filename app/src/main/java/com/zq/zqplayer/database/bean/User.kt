package com.zq.zqplayer.database.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var userName: String, var passWord: String) {
    @PrimaryKey
    var id = 0
}