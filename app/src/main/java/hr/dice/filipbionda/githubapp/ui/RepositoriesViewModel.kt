package hr.dice.filipbionda.githubapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.dice.filipbionda.githubapp.data.model.GitHubRepo
import hr.dice.filipbionda.githubapp.data.model.SearchResult
import hr.dice.filipbionda.githubapp.data.remote.api.result.ApiResult
import hr.dice.filipbionda.githubapp.data.remote.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoriesViewModel(
    private val query: String,
    private val repository: ApiRepository,
) : ViewModel() {
    private var currentPage = 1
    private val perPage = 20
    val bufferIndex = 5
    private val _items = MutableStateFlow<MutableList<GitHubRepo>>(mutableListOf())
    val items = _items.asStateFlow()

    private val _screenState = MutableLiveData<ApiResult<SearchResult>>(ApiResult.Loading)
    val screenState: LiveData<ApiResult<SearchResult>> = _screenState

    private val _gitHubRepo = MutableStateFlow<GitHubRepo?>(null)
    val gitHubRepo = _gitHubRepo.asStateFlow()

    init {
        loadRepositories()
    }

    fun loadRepositories() {
        viewModelScope.launch {
            _screenState.value = repository.searchRepositoriesByName(query, currentPage, perPage)

            if (_screenState.value is ApiResult.Success) {
                val result = (_screenState.value as ApiResult.Success).data
                _items.value.addAll(result.items)
                currentPage += 1
            }
        }
    }

    fun setLoadingState() {
        _screenState.postValue(ApiResult.Loading)
    }

    fun findRepository(id: Long) {
        val item =
            items.value.find {
                it.id == id
            }

        _gitHubRepo.value = item
    }
}
