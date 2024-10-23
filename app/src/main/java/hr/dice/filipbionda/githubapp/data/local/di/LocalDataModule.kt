package hr.dice.filipbionda.githubapp.data.local.di

import androidx.room.Room
import hr.dice.filipbionda.githubapp.data.local.database.AppDatabase
import hr.dice.filipbionda.githubapp.data.local.repository.EntryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

const val APP_DATABASE_NAME = "entry_database"

val localDataModule =
    module {
        single {
            Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                APP_DATABASE_NAME,
            ).build()
        }

        single {
            get<AppDatabase>().entryDao()
        }

        singleOf(::EntryRepository)
    }
