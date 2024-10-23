package hr.dice.filipbionda.githubapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepo(
    val id: Long,
    val name: String,
    val owner: Owner,
    val description: String?,
    @SerialName("stargazers_count")
    val starGazersCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    val private: Boolean,
)
