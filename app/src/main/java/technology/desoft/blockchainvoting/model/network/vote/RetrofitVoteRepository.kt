package technology.desoft.blockchainvoting.model.network.vote

import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Retrofit
import technology.desoft.blockchainvoting.model.network.user.Token

class RetrofitVoteRepository(retrofit: Retrofit): VoteRepository {
    private val api = retrofit.create(VoteApi::class.java)

    private lateinit var token: Token

    override fun setToken(token: Token) {
        this.token = token
    }

    override fun addVote(pollId: Long, optionId: Long): Deferred<AddVoteResult> {
        return GlobalScope.async {
            val response = api.addVote(pollId, optionId, token.tokenString).await()
            AddVoteResult(response.code() == 406)
        }
    }
}