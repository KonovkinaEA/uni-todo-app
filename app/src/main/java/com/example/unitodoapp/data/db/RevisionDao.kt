package com.example.unitodoapp.data.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RevisionDao {

    @Query("SELECT revision.revision FROM revision WHERE revision.id = 1")
    fun getCurrentRevision(): Long

    @Query("UPDATE revision SET revision = :newRevision WHERE revision.id = 1")
    fun updateRevision(newRevision: Long)

    @Query("UPDATE revision SET revision = 1 WHERE revision.id = 1")
    fun setRevisionToOne()
}