package hr.dice.filipbionda.githubapp.ui.searchrepositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.dice.filipbionda.githubapp.data.local.repository.EntryRepository
import hr.dice.filipbionda.githubapp.data.model.SearchEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("OPT_IN_USAGE")
class SearchRepositoriesViewModel(private val repository: EntryRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val isValid = MutableStateFlow(true)

    val uiState =
        combine(
            repository.getEntriesStream(),
            isValid,
            _searchQuery.debounce(500),
        ) {
                entries, isValid, query ->
            val trimmedQuery = query.trim()

            val filteredEntries =
                if (trimmedQuery.isBlank()) {
                    entries
                } else {
                    entries.filter { entry ->
                        entry.doesMatchSearchQuery(query)
                    }
                }
            SearchRepositoriesUiState(
                isEmpty = entries.isEmpty(),
                isValid = isValid,
                entries = filteredEntries,
                query = getInputFormatted(query),
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            SearchRepositoriesUiState(),
        )

    fun onSearchTextChange(text: String) {
        isValid.update {
            true
        }
        _searchQuery.update {
            text
        }
    }

    fun onSearchPressed() {
        isValid.update {
            getInputFormatted(searchQuery.value).length >= 2
        }
        if (isValid.value) {
            cacheEntry()
        }
    }

    private fun cacheEntry() {
        val searchEntry =
            SearchEntry(
                name = getInputFormatted(searchQuery.value),
            )

        viewModelScope.launch {
            repository.insertEntry(searchEntry)
        }
    }

    private fun getInputFormatted(input: String): String {
        return input.trim().replace(Regex("\\s+"), " ")
    }
}
