package hr.dice.filipbionda.githubapp.data.remote.di

import hr.dice.filipbionda.githubapp.data.remote.api.routes.ApiRoutes
import hr.dice.filipbionda.githubapp.data.remote.api.tokens.ApiTokens
import hr.dice.filipbionda.githubapp.data.remote.repository.ApiRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteDataModule =
    module {
        single {
            HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            ignoreUnknownKeys = true
                            isLenient = true
                        },
                    )
                }

                install(DefaultRequest) {
                    url(ApiRoutes.BASE_URL)
                }

                install(Auth) {
                    bearer {
                        loadTokens {
                            BearerTokens(ApiTokens.bearerToken, refreshToken = ApiTokens.refreshToken)
                        }
                    }
                }

                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        }

        single {
            ApiRepository(get(), get())
        }
    }
