package com.codepath.apps.SimpleTweet

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codepath.apps.SimpleTweet.models.SampleModel
import com.codepath.apps.SimpleTweet.models.SampleModelDao

@Database(entities = [SampleModel::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun sampleModelDao(): SampleModelDao?

    companion object {
        // Database name to be used
        const val NAME = "MyDatabase"
    }
}