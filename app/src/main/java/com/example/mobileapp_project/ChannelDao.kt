package com.example.mobileapp_project

import androidx.room.*

// Data Access Object (DAO) interface for managing CRUD operations on Channel entities
@Dao
interface ChannelDao {

    //  Inserts a new channel into the database, replacing any existing entry with the same ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(channel: Channel)

    // Updates an existing channel in the database
    @Update
    suspend fun update(channel: Channel)

    // Deletes a specified channel from the database
    @Delete
    suspend fun delete(channel: Channel)

    // Fetches a single channel by its unique ID, returning null if it doesn't exist
    @Query("SELECT * FROM Channel WHERE id = :channelId")
    suspend fun getChannelById(channelId: Int): Channel?

    // Retrieves all channels in the database as a list
    @Query("SELECT * FROM Channel")
    suspend fun getAllChannels(): List<Channel>
}