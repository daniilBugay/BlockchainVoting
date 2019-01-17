package technology.desoft.blockchainvoting.ui.notification

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenPostApi {
    class UserIdAndToken(val userId: Long, val token: String)

    @POST("")
    fun postToken(@Body userIdAndToken: UserIdAndToken): Deferred<Response<Any>>
}