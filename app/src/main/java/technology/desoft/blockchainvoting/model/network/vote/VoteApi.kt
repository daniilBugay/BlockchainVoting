package technology.desoft.blockchainvoting.model.network.vote

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VoteApi {
    @POST("polls/{pollId}/options/{optionId}/votes")
    fun addVote(
        @Path("pollId") pollId: Long,
        @Path("optionId") optionId: Long,
        @Query("auth_token") token: String
    ): Deferred<Response<Any>>
}