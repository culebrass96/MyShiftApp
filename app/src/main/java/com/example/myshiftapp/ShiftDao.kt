package com.example.myshiftapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {
    @Query("SELECT * FROM shifts ORDER BY date ASC")
    fun getAllShifts(): Flow<List<ShiftEntity>>

    @Insert
    suspend fun insertShift(shift: ShiftEntity)

    @Query("DELETE FROM shifts")
    suspend fun deleteAll()
}
