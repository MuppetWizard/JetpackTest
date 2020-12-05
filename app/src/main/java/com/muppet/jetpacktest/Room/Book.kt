package com.muppet.jetpacktest.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(var nane:String, var price: Int, var pages: Int) {

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}