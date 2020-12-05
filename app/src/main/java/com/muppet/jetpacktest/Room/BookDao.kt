package com.muppet.jetpacktest.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insert(book: Book) : Long

    @Query("select * from Book")
    fun loadAllBooks() : List<Book>
}