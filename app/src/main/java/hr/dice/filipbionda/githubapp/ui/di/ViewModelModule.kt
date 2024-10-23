package hr.dice.filipbionda.githubapp.ui.di

import hr.dice.filipbionda.githubapp.ui.RepositoriesViewModel
import hr.dice.filipbionda.githubapp.ui.home.HomeScreenViewModel
import hr.dice.filipbionda.githubapp.ui.searchrepositories.SearchRepositoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::SearchRepositoriesViewModel)
        viewModelOf(::HomeScreenViewModel)
        viewModel { (query: String) -> RepositoriesViewModel(query, get()) }
    }
