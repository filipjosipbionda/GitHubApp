package hr.dice.filipbionda.githubapp.data.remote.repository

import android.content.Context
import hr.dice.filipbionda.githubapp.R
import hr.dice.filipbionda.githubapp.data.model.SearchResult
import hr.dice.filipbionda.githubapp.data.remote.api.result.ApiResult
import hr.dice.filipbionda.githubapp.data.remote.api.routes.ApiRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiRepository(private val client: HttpClient, private val context: Context) {
    suspend fun searchRepositoriesByName(
        query: String,
        page: Int,
        perPage: Int,
    ): ApiResult<SearchResult> =
        withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse =
                    client.get(ApiRoutes.SEARCH_REPOSITORIES) {
                        parameter("q", query)
                        parameter("page", page)
                        parameter("per_page", perPage)
                        headers {
                            append(HttpHeaders.Accept, "application/json")
                        }
                    }

                return@withContext when (response.status) {
                    HttpStatusCode.OK -> {
                        val searchResult: SearchResult = response.body()
                        ApiResult.Success(searchResult)
                    }

                    HttpStatusCode.BadRequest -> {
                        ApiResult.Error(
                            context.getString(R.string.error_bad_request),
                            response.status.value,
                        )
                    }

                    HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> {
                        ApiResult.Error(
                            context.getString(R.string.error_unauthorized),
                            response.status.value,
                        )
                    }

                    HttpStatusCode.NotFound -> {
                        ApiResult.Error(
                            context.getString(R.string.error_not_found),
                            response.status.value,
                        )
                    }

                    HttpStatusCode.InternalServerError -> {
                        ApiResult.Error(
                            context.getString(R.string.error_server),
                            response.status.value,
                        )
                    }

                    else -> {
                        ApiResult.Error(
                            context.getString(R.string.error_unknown),
                            response.status.value,
                        )
                    }
                }
            } catch (e: UnknownHostException) {
                return@withContext ApiResult.NetworkError(context.getString(R.string.network_error_message))
            } catch (e: SocketTimeoutException) {
                return@withContext ApiResult.NetworkError(context.getString(R.string.network_error_message))
            } catch (e: Exception) {
                return@withContext ApiResult.Error(context.getString(R.string.error_content_placeholder_message), -100)
            }
        }
}
