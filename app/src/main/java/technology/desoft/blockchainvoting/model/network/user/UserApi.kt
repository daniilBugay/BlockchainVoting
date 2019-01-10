package technology.desoft.blockchainvoting.model.network.user

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface UserApi {
    @POST("users/sign_in")
    fun login(@Body emailAndPassword: EmailAndPassword): Deferred<Response<Token>>

    @POST("users")
    fun registration(@Body emailAndPassword: EmailAndPassword): Deferred<Response<UserAndTokenView>>

    @GET("users")
    fun getUsers(@Query("auth_token") token: String): Deferred<Response<List<User>>>
}