package com.eltex.firstapp.feature.event.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Query("SELECT * FROM Events ORDER BY id DESC")
    fun getAll(): List<EventEntity>
    @Insert
    fun insert(entity: EventEntity): Long
    @Update
    fun update(entity: EventEntity)
    @Query("SELECT * FROM Events WHERE id = :id")
    fun getById(id: Long): EventEntity
    @Query("DELETE FROM Events WHERE id = :id")
    fun deleteById(id: Long)
}