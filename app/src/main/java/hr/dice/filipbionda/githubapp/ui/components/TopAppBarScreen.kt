package hr.dice.filipbionda.githubapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopAppBarScreen(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = topBar,
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
        ) {
            content()
        }
    }
}
