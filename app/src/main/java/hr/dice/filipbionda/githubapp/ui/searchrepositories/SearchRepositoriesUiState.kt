package hr.dice.filipbionda.githubapp.ui.searchrepositories

import hr.dice.filipbionda.githubapp.data.model.SearchEntry

data class SearchRepositoriesUiState(
    val isEmpty: Boolean = false,
    val query: String = "",
    val entries: List<SearchEntry> = emptyList(),
    val isValid: Boolean = true,
)
