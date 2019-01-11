package technology.desoft.blockchainvoting.model.network.polls

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface PollApi {
    @GET("polls")
    fun getPolls(@Query("auth_token") token: String): Deferred<Response<List<Poll>>>

    @GET("polls/{id}/options")
    fun getOptions(
        @Path("id") pollId: Long,
        @Query("auth_token") token: String
    ): Deferred<Response<List<PollOption>>>

    @DELETE("polls/{id}")
    fun removePoll(@Path("id") id: Long, @Query("auth_token") token: String): Deferred<Response<Any>>

    @POST("polls")
    fun createPoll(
        @Query("auth_token") token: String,
        @Body createPollView: CreatePollView
    ): Deferred<Response<Poll>>

    @POST("polls/{id}/options")
    fun createOption(
        @Path("id") pollId: Long,
        @Query("auth_token") token: String,
        @Body createPollOptionView: CreatePollOptionView
    ): Deferred<Response<Any>>
}