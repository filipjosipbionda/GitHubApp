package hr.dice.filipbionda.githubapp.ui.repositorieslist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.dice.filipbionda.githubapp.R
import hr.dice.filipbionda.githubapp.data.model.GitHubRepo
import hr.dice.filipbionda.githubapp.data.model.SearchResult
import hr.dice.filipbionda.githubapp.data.remote.api.result.ApiResult
import hr.dice.filipbionda.githubapp.ui.RepositoriesViewModel
import hr.dice.filipbionda.githubapp.ui.components.TopAppBarScreen
import hr.dice.filipbionda.githubapp.ui.theme.GitHubAppTheme
import hr.dice.filipbionda.githubapp.ui.theme.cardBodyTextColor
import hr.dice.filipbionda.githubapp.ui.theme.headerTextColor
import hr.dice.filipbionda.githubapp.ui.theme.searchInputHintTextColor
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RepositoriesListScreen(
    repositoriesViewModel: RepositoriesViewModel,
    openRepositoryDetails: (Long) -> Unit,
) {
    val screenState by repositoriesViewModel.screenState.observeAsState(ApiResult.Loading)
    val bufferIndex = repositoriesViewModel.bufferIndex
    val repositories by repositoriesViewModel.items.collectAsState()
    val configuration = LocalConfiguration.current
    val isInLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    when (val uiState = screenState) {
        is ApiResult.Success -> {
            SuccessContent(
                repositories = repositories,
                searchResult = uiState.data,
                buffer = bufferIndex,
                onNextPage = {
                    repositoriesViewModel.loadRepositories()
                },
                onItemClick = {
                    openRepositoryDetails(it)
                },
            )
        }

        is ApiResult.Error -> {
            ErrorContent(
                message = uiState.message,
                onTryAgain = {
                    repositoriesViewModel.setLoadingState()
                    repositoriesViewModel.loadRepositories()
                },
                isInLandscape = isInLandscape,
                errorIcon = {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = stringResource(id = R.string.error_content_icon_description),
                        tint = searchInputHintTextColor,
                        modifier =
                            Modifier
                                .size(
                                    if (isInLandscape) 200.dp else 300.dp,
                                )
                                .padding(
                                    top = if (isInLandscape) 10.dp else 20.dp,
                                ),
                    )
                },
            )
        }

        is ApiResult.NetworkError -> {
            ErrorContent(
                message = uiState.message,
                onTryAgain = {
                    repositoriesViewModel.setLoadingState()
                    repositoriesViewModel.loadRepositories()
                },
                isInLandscape = isInLandscape,
                errorIcon = {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = stringResource(id = R.string.error_content_icon_description),
                        tint = searchInputHintTextColor,
                        modifier =
                            Modifier
                                .size(if (isInLandscape) 150.dp else 300.dp)
                                .padding(
                                    top = if (isInLandscape) 10.dp else 20.dp,
                                ),
                    )
                },
            )
        }

        else -> {
            LoadingContent()
        }
    }
}

@Composable
private fun SuccessContent(
    searchResult: SearchResult,
    repositories: List<GitHubRepo>,
    onNextPage: () -> Unit,
    buffer: Int,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    val previousIndex = remember { mutableIntStateOf(0) }
    val shouldLoadNextPage by remember {
        derivedStateOf {
            val currentIndex = listState.firstVisibleItemIndex
            val isScrollingDown = currentIndex > previousIndex.intValue
            val isAtBufferPosition = currentIndex > 0 && currentIndex % buffer == 0

            previousIndex.intValue = currentIndex
            isScrollingDown && isAtBufferPosition
        }
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            onNextPage()
        }
    }

    TopAppBarScreen(
        topBar = {
            RepositoryListHeader(
                numberOfRepositories = searchResult.totalCount,
            )
            HorizontalDivider(thickness = 2.dp, color = Color.White)
        },
    ) {
        LazyColumn(
            state = listState,
            modifier =
                modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            items(searchResult.totalCount) { index ->
                val repository = repositories[index]
                RepositoriesListItem(
                    author = repository.owner.login,
                    repositoryName = repository.name,
                    description = repository.description,
                    stars = repository.starGazersCount,
                    onClick = {
                        onItemClick(repository.id)
                    },
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
        )
    }
}

@Preview
@Composable
private fun LoadingContentPreview() {
    GitHubAppTheme {
        Surface {
            LoadingContent()
        }
    }
}

@Composable
private fun ErrorContent(
    message: String?,
    isInLandscape: Boolean,
    errorIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize(),
    ) {
        errorIcon()
        Spacer(
            modifier = Modifier.height(height = if (isInLandscape) 10.dp else 40.dp),
        )
        Text(
            stringResource(R.string.error_content_placeholder_text),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier.padding(
                    bottom = if (isInLandscape) 10.dp else 40.dp,
                ),
        )
        message?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
            )
        }
        Spacer(
            modifier = Modifier.height(height = if (isInLandscape) 10.dp else 100.dp),
        )
        ElevatedButton(
            onClick = onTryAgain,
            colors = ButtonDefaults.buttonColors(),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
        ) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}

@Preview
@Composable
private fun NetworkErrorContentPreview() {
    GitHubAppTheme {
        Surface {
            ErrorContent(
                message = "Make sure you have an active internet connection!",
                isInLandscape = false,
                onTryAgain = {},
                errorIcon = {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = stringResource(id = R.string.error_content_icon_description),
                        tint = searchInputHintTextColor,
                        modifier =
                            Modifier
                                .padding(bottom = 30.dp)
                                .size(300.dp),
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun ErrorContentPreview() {
    GitHubAppTheme {
        Surface {
            ErrorContent(
                message = "There was an error. Try again",
                onTryAgain = {},
                isInLandscape = false,
                errorIcon = {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = stringResource(id = R.string.error_content_icon_description),
                        tint = searchInputHintTextColor,
                        modifier =
                            Modifier
                                .padding(bottom = 30.dp)
                                .size(300.dp),
                    )
                },
            )
        }
    }
}

@Composable
private fun RepositoryListHeader(
    numberOfRepositories: Number,
    modifier: Modifier = Modifier,
) {
    val formattedNumberOfRepositories =
        NumberFormat.getInstance(Locale.US).format(numberOfRepositories)
    Text(
        text = stringResource(R.string.header_text, formattedNumberOfRepositories),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = headerTextColor,
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .padding(15.dp),
    )
}

@Preview
@Composable
private fun RepositoriesListHeaderPreview() {
    GitHubAppTheme {
        RepositoryListHeader(
            numberOfRepositories = 1300000,
        )
    }
}

@Composable
private fun RepositoriesListItem(
    onClick: () -> Unit,
    author: String,
    repositoryName: String,
    description: String?,
    stars: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Text(
            text = "$author/$repositoryName",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 10.dp, top = 6.dp),
        )
        description?.let {
            Text(
                text = it,
                fontSize = 12.sp,
                color = cardBodyTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 4.dp),
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp, bottom = 6.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(14.dp),
                tint = cardBodyTextColor,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text =
                    if (stars > 1000) {
                        stringResource(
                            R.string.stars_count_text,
                            stars / 1000,
                        )
                    } else {
                        "$stars"
                    },
                color = cardBodyTextColor,
                fontSize = 12.sp,
            )
        }
    }
}

@Preview
@Composable
private fun RepositoriesListItemPreview() {
    GitHubAppTheme {
        RepositoriesListItem(
            author = "flutter",
            repositoryName = "flutter",
            description = "Test description is very good and we should stick on it very good and practical",
            stars = 10,
            onClick = {},
        )
    }
}
