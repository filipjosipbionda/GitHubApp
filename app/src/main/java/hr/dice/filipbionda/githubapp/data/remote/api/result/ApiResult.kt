package hr.dice.filipbionda.githubapp.data.remote.api.result

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Error(val message: String?, val statusCode: Int) : ApiResult<Nothing>()

    data class NetworkError(val message: String) : ApiResult<Nothing>()

    data object Loading : ApiResult<Nothing>()
}
