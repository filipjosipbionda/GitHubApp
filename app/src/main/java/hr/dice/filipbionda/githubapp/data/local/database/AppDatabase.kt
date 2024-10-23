package hr.dice.filipbionda.githubapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.dice.filipbionda.githubapp.data.model.SearchEntry

@Database(entities = [SearchEntry::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): SearchEntryDao
}
