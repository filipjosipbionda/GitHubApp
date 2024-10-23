package hr.dice.filipbionda.githubapp.ui.searchrepositories

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.dice.filipbionda.githubapp.R
import hr.dice.filipbionda.githubapp.data.model.SearchEntry
import hr.dice.filipbionda.githubapp.ui.components.TopAppBarScreen
import hr.dice.filipbionda.githubapp.ui.theme.GitHubAppTheme
import hr.dice.filipbionda.githubapp.ui.theme.searchBackgroundColor
import hr.dice.filipbionda.githubapp.ui.theme.searchInputHintTextColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchRepositoriesScreen(
    onBackPressed: () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchRepositoriesViewModel = koinViewModel<SearchRepositoriesViewModel>()
    val state by searchRepositoriesViewModel.uiState.collectAsState()
    val searchQuery by searchRepositoriesViewModel.searchQuery.collectAsState()

    TopAppBarScreen(
        modifier = modifier,
        topBar = {
            SearchRepositoriesSearchBar(
                searchText = searchQuery,
                queryLength = state.query.length,
                isInputValid = state.isValid,
                onSearch = {
                },
                onValueChange = {
                    searchRepositoriesViewModel.onSearchTextChange(it)
                },
                onBackPressed = onBackPressed,
            )
        },
    ) {
        SearchRepositoriesScreenContent(
            suggestions = state.entries,
            onRecentEntryClick = { recentEntry ->
                onSearch(recentEntry)
            },
            isEmpty = state.isEmpty,
            modifier =
                Modifier
                    .fillMaxSize(),
        )
    }
}

@Composable
private fun SearchRepositoriesScreenContent(
    suggestions: List<SearchEntry>,
    onRecentEntryClick: (String) -> Unit,
    isEmpty: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp),
    ) {
        when {
            isEmpty -> {
                item {
                    Text(
                        text = stringResource(id = R.string.no_suggestions),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = searchInputHintTextColor,
                        modifier = Modifier.padding(start = 10.dp),
                    )
                }
            }

            else -> {
                items(suggestions.size) { index ->
                    RecentEntry(name = suggestions[index].name, {
                        onRecentEntryClick(it)
                    })
                }
            }
        }
    }
}

@Composable
fun SearchRepositoriesSearchBar(
    searchText: String,
    isInputValid: Boolean,
    queryLength: Int,
    onSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        TextField(
            modifier =
                Modifier
                    .fillMaxWidth(),
            maxLines = 1,
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
            keyboardActions =
                KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        onSearch()
                    },
                ),
            leadingIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.textfield_leadingicon_description),
                    )
                }
            },
            label = {
                Text(
                    text =
                        stringResource(
                            R.string.search_repositories,
                        ),
                    color = searchInputHintTextColor,
                )
            },
            value = searchText,
            onValueChange = {
                onValueChange(it)
            },
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = searchBackgroundColor,
                    unfocusedContainerColor = searchBackgroundColor,
                    focusedIndicatorColor =
                        if (!isInputValid) {
                            MaterialTheme.colorScheme.error
                        } else {
                            searchBackgroundColor
                        },
                    unfocusedIndicatorColor = searchBackgroundColor,
                ),
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onValueChange("")
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription =
                                stringResource(
                                    R.string.textfield_trailingicon_description,
                                ),
                            tint =
                                if (!isInputValid) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    searchInputHintTextColor
                                },
                        )
                    }
                }
            },
        )
        if (!isInputValid) {
            Text(
                text = stringResource(R.string.invalid_input, queryLength),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 10.dp, top = 4.dp),
            )
        }
    }
}

@Preview
@Composable
private fun SearchRepositoriesSearchBarPreview() {
    GitHubAppTheme {
        Surface {
            SearchRepositoriesSearchBar(
                searchText = "",
                isInputValid = false,
                onSearch = { },
                onBackPressed = {},
                queryLength = 1,
                onValueChange = {},
            )
        }
    }
}

@Composable
fun RecentEntry(
    name: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick(name) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            imageVector = Icons.Filled.AccessTime,
            tint = searchInputHintTextColor,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
private fun RecentEntryPreview() {
    Surface {
        RecentEntry(name = "Kotlin", {})
    }
}

@Preview
@Composable
private fun SearchRepositoriesContentPreview() {
    GitHubAppTheme {
        Surface {
            SearchRepositoriesScreenContent(
                suggestions = emptyList(),
                {},
                true,
                Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(
    name = "Landscape Preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=800dp,height=340dp",
    showBackground = true,
)
@Composable
private fun SearchRepositoriesLandscapePreview() {
    SearchRepositoriesScreenContent(
        emptyList(),
        onRecentEntryClick = {},
        true,
        Modifier.fillMaxSize(),
    )
}
