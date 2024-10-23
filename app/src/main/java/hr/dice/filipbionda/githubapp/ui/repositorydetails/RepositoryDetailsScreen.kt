package hr.dice.filipbionda.githubapp.ui.repositorydetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hr.dice.filipbionda.githubapp.R
import hr.dice.filipbionda.githubapp.data.model.GitHubRepo
import hr.dice.filipbionda.githubapp.data.model.Owner
import hr.dice.filipbionda.githubapp.ui.RepositoriesViewModel
import hr.dice.filipbionda.githubapp.ui.theme.GitHubAppTheme
import hr.dice.filipbionda.githubapp.ui.theme.headerTextColor
import hr.dice.filipbionda.githubapp.ui.theme.repositoryDetailsContentColor

private const val BASE_URL = "https://github.com/%s?tab=repositories"

@Composable
fun RepositoryDetailsScreen(
    repositoryViewModel: RepositoriesViewModel,
    navigateToProfileDetails: (String, String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val repository by repositoryViewModel.gitHubRepo.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            repository?.let {
                RepositoryDetailsTopAppBar(
                    title = it.owner.login,
                    navigateBack = navigateBack,
                )
            }
        },
    ) { contentPadding ->
        repository?.let {
            RepositoryDetailsContent(
                repository = it,
                navigateToProfileDetails = navigateToProfileDetails,
                modifier = Modifier.padding(contentPadding),
            )
        }
    }
}

@Composable
private fun RepositoryDetailsContent(
    navigateToProfileDetails: (String, String) -> Unit,
    repository: GitHubRepo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
    ) {
        RepositoryDetails(
            repository = repository,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(bottom = 10.dp),
        )
        Button(
            onClick = {
                navigateToProfileDetails(
                    repository.owner.login,
                    BASE_URL.format(repository.owner.login),
                )
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(),
        ) {
            Text(
                text = stringResource(R.string.open_owner_repositories_button),
            )
        }
        Button(
            onClick = {
                navigateToProfileDetails(
                    repository.owner.login,
                    repository.owner.htmlUrl,
                )
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(),
        ) {
            Text(
                text = stringResource(R.string.open_owner_profile_button),
            )
        }
    }
}

@Composable
private fun RepositoryDetails(
    repository: GitHubRepo,
    modifier: Modifier = Modifier,
) {
    val paddingModifier = Modifier.padding(top = 10.dp)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.owner_label),
                    fontSize = 20.sp,
                    color = repositoryDetailsContentColor,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        modifier =
                            Modifier
                                .size(60.dp)
                                .padding(end = 5.dp),
                        model = repository.owner.avatarUrl,
                        placeholder = painterResource(R.drawable.placeholder_owner),
                        contentDescription = stringResource(R.string.owner_image_description),
                    )
                    Text(
                        text = repository.owner.login,
                        fontSize = 16.sp,
                        color = repositoryDetailsContentColor,
                    )
                }
            },
            modifier = paddingModifier,
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.description_label),
                    fontSize = 20.sp,
                    color = repositoryDetailsContentColor,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                repository.description?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = repositoryDetailsContentColor,
                    )
                }
            },
            modifier = paddingModifier,
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.opened_issues_label),
                    fontSize = 20.sp,
                    color = repositoryDetailsContentColor,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = repositoryDetailsContentColor,
                        modifier =
                            Modifier
                                .size(24.dp)
                                .padding(end = 5.dp),
                    )
                    Text(
                        text = repository.openIssuesCount.toString(),
                        fontSize = 16.sp,
                        color = repositoryDetailsContentColor,
                    )
                }
            },
            modifier = paddingModifier,
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.watchers_label),
                    fontSize = 20.sp,
                    color = repositoryDetailsContentColor,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RemoveRedEye,
                        contentDescription = null,
                        tint = repositoryDetailsContentColor,
                        modifier =
                            Modifier
                                .size(24.dp)
                                .padding(end = 5.dp),
                    )
                    Text(
                        text = repository.watchersCount.toString(),
                        fontSize = 16.sp,
                        color = repositoryDetailsContentColor,
                    )
                }
            },
            modifier = paddingModifier,
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.stars_label),
                    fontSize = 20.sp,
                    color = repositoryDetailsContentColor,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = repositoryDetailsContentColor,
                        modifier =
                            Modifier
                                .size(24.dp)
                                .padding(end = 5.dp),
                    )
                    Text(
                        text = repository.openIssuesCount.toString(),
                        fontSize = 16.sp,
                        color = repositoryDetailsContentColor,
                    )
                }
            },
            modifier = paddingModifier,
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.private_label),
                    fontSize = 20.sp,
                    color = repositoryDetailsContentColor,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = repositoryDetailsContentColor,
                        modifier =
                            Modifier
                                .size(24.dp)
                                .padding(end = 5.dp),
                    )
                    Text(
                        text =
                            if (repository.private) {
                                stringResource(R.string.private_positive)
                            } else {
                                stringResource(R.string.private_negative)
                            },
                        fontSize = 16.sp,
                        color = repositoryDetailsContentColor,
                    )
                }
            },
            modifier = paddingModifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepositoryDetailsTopAppBar(
    title: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                color = headerTextColor,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_to_repositories_description),
                    tint = headerTextColor,
                )
            }
        },
    )
}

@Preview
@Composable
private fun RepositoryDetailsContentPreview() {
    val mockOwner =
        Owner(
            id = 12345,
            login = "mock_owner",
            avatarUrl = "https://avatars.githubusercontent.com/u/12345?v=4",
            htmlUrl = "https://github.com/mock_owner",
            reposUrl = "",
        )

    val mockRepository =
        GitHubRepo(
            id = 67890,
            name = "Mock Repository",
            owner = mockOwner,
            description = "This is a mock repository used for preview purposes.",
            starGazersCount = 150,
            watchersCount = 80,
            openIssuesCount = 10,
            private = false,
        )

    GitHubAppTheme {
        RepositoryDetailsContent(
            repository = mockRepository,
            navigateToProfileDetails = { _, _ ->
            },
        )
    }
}

@Preview
@Composable
private fun RepositoryDetailsTopAppBarPreview() {
    RepositoryDetailsTopAppBar(title = "Kotlin", navigateBack = {})
}
