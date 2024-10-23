package hr.dice.filipbionda.githubapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val id: Long,
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
)
