package hr.dice.filipbionda.githubapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import hr.dice.filipbionda.githubapp.ui.AppNavHost
import hr.dice.filipbionda.githubapp.ui.theme.GitHubAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubAppTheme {
                AppNavHost()
            }
        }
    }
}
