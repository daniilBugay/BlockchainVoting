package technology.desoft.blockchainvoting.model.network.vote

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import technology.desoft.blockchainvoting.model.network.user.Token

class RetrofitVoteRepository(retrofit: Retrofit): VoteRepository {
    private val api = retrofit.create(VoteApi::class.java)

    private lateinit var token: Token

    override fun setToken(token: Token) {
        this.token = token
    }

    override fun addVote(pollId: Long, optionId: Long): Job {
        return GlobalScope.launch {
            val response = api.addVote(pollId, optionId, token.tokenString).await()
            if (!response.isSuccessful)
                throw HttpException(response)
        }
    }
}