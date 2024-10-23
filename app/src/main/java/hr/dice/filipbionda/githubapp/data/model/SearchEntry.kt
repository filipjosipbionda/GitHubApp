package hr.dice.filipbionda.githubapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchEntry(
    @PrimaryKey
    val name: String,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations =
            listOf(
                name,
            )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
