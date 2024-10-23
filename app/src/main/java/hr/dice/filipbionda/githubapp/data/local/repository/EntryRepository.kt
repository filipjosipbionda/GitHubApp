package hr.dice.filipbionda.githubapp.data.local.repository

import hr.dice.filipbionda.githubapp.data.local.database.SearchEntryDao
import hr.dice.filipbionda.githubapp.data.model.SearchEntry
import kotlinx.coroutines.flow.Flow

class EntryRepository(private val searchEntryDao: SearchEntryDao) {
    suspend fun insertEntry(searchEntry: SearchEntry) {
        searchEntryDao.insertEntry(searchEntry)
    }

    suspend fun deleteEntries() {
        searchEntryDao.deleteAllEntries()
    }

    fun getEntriesStream(): Flow<List<SearchEntry>> = searchEntryDao.getEntriesStream()
}
