package hr.dice.filipbionda.githubapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import hr.dice.filipbionda.githubapp.Home
import hr.dice.filipbionda.githubapp.ListScreen
import hr.dice.filipbionda.githubapp.ProfileDetails
import hr.dice.filipbionda.githubapp.RepositoriesList
import hr.dice.filipbionda.githubapp.RepositoryDetails
import hr.dice.filipbionda.githubapp.SearchRepositories
import hr.dice.filipbionda.githubapp.ui.home.HomeScreen
import hr.dice.filipbionda.githubapp.ui.profiledetails.ProfileDetailsScreen
import hr.dice.filipbionda.githubapp.ui.repositorieslist.RepositoriesListScreen
import hr.dice.filipbionda.githubapp.ui.repositorydetails.RepositoryDetailsScreen
import hr.dice.filipbionda.githubapp.ui.searchrepositories.SearchRepositoriesScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(onSearchRepositories = { navController.navigate(SearchRepositories) })
        }
        composable<SearchRepositories> {
            SearchRepositoriesScreen(
                onSearch = { query ->
                    navController.navigate(
                        RepositoriesList(
                            query = query,
                        ),
                    )
                },
                onBackPressed = {
                    navController.popBackStack()
                },
            )
        }
        navigation<ListScreen>(
            startDestination = RepositoriesList(),
        ) {
            composable<RepositoriesList> { navBackStackEntry ->
                val query: String = navBackStackEntry.toRoute<RepositoriesList>().query

                val repositoriesViewModel =
                    navBackStackEntry.sharedViewModel<RepositoriesViewModel>(
                        navController = navController,
                        parameters = { parametersOf(query) },
                    )

                RepositoriesListScreen(
                    repositoriesViewModel = repositoriesViewModel,
                    openRepositoryDetails = { id ->
                        navController.navigate(
                            RepositoryDetails(
                                id = id,
                            ),
                        )
                    },
                )
            }
            composable<RepositoryDetails> { navBackStackEntry ->
                val id = navBackStackEntry.toRoute<RepositoryDetails>().id
                val repositoriesViewModel =
                    navBackStackEntry.sharedViewModel<RepositoriesViewModel>(
                        navController = navController,
                    )
                repositoriesViewModel.findRepository(id)
                RepositoryDetailsScreen(
                    repositoryViewModel = repositoriesViewModel,
                    navigateToProfileDetails = { ownerName, url ->
                        navController.navigate(
                            ProfileDetails(
                                ownerName = ownerName,
                                url = url,
                            ),
                        )
                    },
                    navigateBack = { navController.popBackStack() },
                )
            }
            composable<ProfileDetails> { navBackStackEntry ->
                val ownerName = navBackStackEntry.toRoute<ProfileDetails>().ownerName
                val url = navBackStackEntry.toRoute<ProfileDetails>().url

                ProfileDetailsScreen(
                    ownerName = ownerName,
                    url = url,
                    navigateBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
    noinline parameters: ParametersDefinition? = null,
): T {
    val navGraphRoute =
        destination.parent?.route ?: return if (parameters != null) {
            koinViewModel(parameters = parameters)
        } else {
            koinViewModel()
        }

    val parentEntry =
        remember(key1 = this) {
            navController.getBackStackEntry(navGraphRoute)
        }

    return if (parameters != null) {
        koinViewModel(
            viewModelStoreOwner = parentEntry,
            parameters = parameters,
        )
    } else {
        koinViewModel(viewModelStoreOwner = parentEntry)
    }
}
