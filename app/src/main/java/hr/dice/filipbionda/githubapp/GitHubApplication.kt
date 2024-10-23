package hr.dice.filipbionda.githubapp

import android.app.Application
import hr.dice.filipbionda.githubapp.data.local.di.localDataModule
import hr.dice.filipbionda.githubapp.data.remote.di.remoteDataModule
import hr.dice.filipbionda.githubapp.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GitHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GitHubApplication)
            modules(localDataModule, remoteDataModule, viewModelModule)
        }
    }
}
