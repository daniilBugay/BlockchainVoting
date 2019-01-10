package technology.desoft.blockchainvoting.model.network.polls

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PollApi {
    @GET("polls")
    fun getPolls(@Query("auth_token") token: String): Deferred<Response<List<Poll>>>

    @GET("polls/{id}/options")
    fun getOptions(
        @Path("id") pollId: Long,
        @Query("auth_token") token: String
    ): Deferred<Response<List<PollOption>>>

    @DELETE("polls/{id}")
    fun removePoll(@Path("id") id: Long, @Query("auth_token") token: String): Job
}