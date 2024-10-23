package hr.dice.filipbionda.githubapp.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.dice.filipbionda.githubapp.R
import hr.dice.filipbionda.githubapp.ui.components.TopAppBarScreen
import hr.dice.filipbionda.githubapp.ui.theme.searchBackgroundColor
import hr.dice.filipbionda.githubapp.ui.theme.searchInputHintTextColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onSearchRepositories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()

    var openAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBarScreen(
        modifier = modifier,
        topBar = {
            HomeScreenTopAppBar(
                onClear = { openAlertDialog = !openAlertDialog },
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) {
        HomeScreenContent(
            onSearchRepositories = onSearchRepositories,
        )
        if (openAlertDialog) {
            AlertDialog(
                text = {
                    Text(
                        text = stringResource(R.string.alertdialog_message),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 35.sp,
                        maxLines = 2,
                        modifier = Modifier.padding(5.dp),
                    )
                },
                onDismissRequest = { openAlertDialog = !openAlertDialog },
                confirmButton = {
                    TextButton(
                        onClick = {
                            homeScreenViewModel.clearSearchEntries()
                            openAlertDialog = !openAlertDialog
                        },
                    ) {
                        Text(stringResource(R.string.alertdialog_confirm_text))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openAlertDialog = !openAlertDialog
                        },
                    ) {
                        Text(stringResource(R.string.alertdialog_dismiss_text))
                    }
                },
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    onSearchRepositories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current.orientation
    val inLandscape = configuration == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(if (!inLandscape) 200.dp else 20.dp))
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = null,
            modifier = Modifier.size(if (!inLandscape) 180.dp else 120.dp),
        )
        Spacer(modifier = Modifier.height(60.dp))
        ElevatedCard(
            modifier =
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = if (!inLandscape) 60.dp else 200.dp),
            shape = RoundedCornerShape(10.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = searchBackgroundColor,
                    contentColor = searchInputHintTextColor,
                ),
            onClick = onSearchRepositories,
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 10.dp, end = 26.dp),
                )
                Text(text = stringResource(id = R.string.search_repositories), fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenTopAppBar(
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "") },
        actions = {
            IconButton(
                onClick = { expanded = !expanded },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    stringResource(R.string.topappbar_menu_description),
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(R.string.menu_clear_entries_text),
                        )
                    },
                    onClick = {
                        expanded = !expanded
                        onClear()
                    },
                )
            }
        },
    )
}

@Preview
@Composable
private fun HomeScreenAppBarPreview() {
    Surface {
        HomeScreenTopAppBar({}, Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun HomeScreenContentPreview() {
    Surface {
        HomeScreenContent(onSearchRepositories = { })
    }
}

@Preview(
    name = "Landscape Preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=800dp,height=340dp",
    showBackground = true,
)
@Composable
private fun HomeScreenLandscapePreview() {
    HomeScreenContent(onSearchRepositories = {})
}
