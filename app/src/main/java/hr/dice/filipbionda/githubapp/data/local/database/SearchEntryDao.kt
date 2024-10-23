package hr.dice.filipbionda.githubapp.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.dice.filipbionda.githubapp.data.model.SearchEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(searchEntry: SearchEntry)

    @Query("DELETE FROM searchentry")
    suspend fun deleteAllEntries()

    @Query("SELECT * FROM searchentry ORDER BY name ASC")
    fun getEntriesStream(): Flow<List<SearchEntry>>
}
