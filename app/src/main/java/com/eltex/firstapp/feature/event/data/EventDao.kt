package com.eltex.firstapp.feature.event.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Query("SELECT * FROM Events ORDER BY id DESC")
    fun getAll(): List<EventEntity>

    @Query(
        """
      UPDATE Events
      SET likedByMe = CASE WHEN likedByMe = 1 THEN 0 ELSE 1 END,
          likes = CASE WHEN likedByMe = 1 THEN likes - 1 ELSE likes + 1 END
      WHERE id = :id
  """
    )
    fun likeById(id: Long)

    @Query(
        """
      UPDATE Events
      SET participantsByMe = CASE WHEN participantsByMe = 1 THEN 0 ELSE 1 END,
          participants = CASE WHEN participantsByMe = 1 THEN participants - 1 ELSE participants + 1 END
      WHERE id = :id
  """
    )
    fun participateById(id: Long)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(entity: EventEntity): Long

    @Update
    fun update(entity: EventEntity)

    @Query("SELECT * FROM Events WHERE id = :id")
    fun getById(id: Long): EventEntity

    @Query("DELETE FROM Events WHERE id = :id")
    fun deleteById(id: Long)
}