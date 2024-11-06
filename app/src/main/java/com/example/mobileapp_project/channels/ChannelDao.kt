package com.example.mobileapp_project.channels

import androidx.room.*

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(channel: Channel)

    @Update
    suspend fun update(channel: Channel)

    @Delete
    suspend fun delete(channel: Channel)

    @Query("SELECT * FROM Channel WHERE id = :channelId")
    suspend fun getChannelById(channelId: Int): Channel?

    @Query("SELECT * FROM Channel")
    suspend fun getAllChannels(): List<Channel>
}
