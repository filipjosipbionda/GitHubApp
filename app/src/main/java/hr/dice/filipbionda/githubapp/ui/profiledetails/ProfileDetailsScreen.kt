package hr.dice.filipbionda.githubapp.ui.profiledetails

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import hr.dice.filipbionda.githubapp.R
import hr.dice.filipbionda.githubapp.ui.components.TopAppBarScreen
import hr.dice.filipbionda.githubapp.ui.theme.repositoryDetailsContentColor

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ProfileDetailsScreen(
    ownerName: String,
    url: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBarScreen(
        modifier = modifier,
        topBar = {
            ProfileDetailsTopAppBar(
                title = ownerName,
                onNavigateBack = navigateBack,
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                WebView(it).apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                }
            },
            update = {
                it.loadUrl(url)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileDetailsTopAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_to_repository_details_description),
                    tint = repositoryDetailsContentColor,
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        },
    )
}

@Preview
@Composable
private fun ProfileDetailsScreenPreview() {
    ProfileDetailsScreen(
        ownerName = "ChrisMr",
        url = "https://github.com/TheStalwart/osu.lv",
        navigateBack = {
        },
    )
}

@Preview
@Composable
private fun ProfileDetailsTopAppBarPreview() {
    ProfileDetailsTopAppBar(
        title = "ChrisMR",
        onNavigateBack = {},
        modifier = Modifier.fillMaxWidth(),
    )
}
