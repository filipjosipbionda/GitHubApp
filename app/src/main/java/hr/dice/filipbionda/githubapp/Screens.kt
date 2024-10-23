package hr.dice.filipbionda.githubapp

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object SearchRepositories

@Serializable
data class ListScreen(
    val query: String,
)

@Serializable
data class RepositoriesList(
    val query: String = "",
)

@Serializable
data class RepositoryDetails(
    val id: Long,
)

@Serializable
data class ProfileDetails(
    val ownerName: String,
    val url: String,
)
