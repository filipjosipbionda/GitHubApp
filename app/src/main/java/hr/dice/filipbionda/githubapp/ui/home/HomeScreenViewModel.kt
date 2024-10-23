package hr.dice.filipbionda.githubapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.dice.filipbionda.githubapp.data.local.repository.EntryRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: EntryRepository) : ViewModel() {
    fun clearSearchEntries() {
        viewModelScope.launch {
            repository.deleteEntries()
        }
    }
}
