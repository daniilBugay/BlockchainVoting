package technology.desoft.blockchainvoting.model.network.polls

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import retrofit2.Retrofit
import technology.desoft.blockchainvoting.model.network.user.Token

class RetrofitPollRepository(retrofit: Retrofit): PollRepository {
    private val api = retrofit.create(PollApi::class.java)

    private lateinit var token: Token

    override fun setToken(token: Token){
        this.token = token
    }

    override fun getPolls(): Deferred<List<Poll>?> {
        return GlobalScope.async {
            val response = api.getPolls(token.tokenString).await()

            if (!response.isSuccessful)
                null
            else
                response.body()
        }
    }

    override fun getOptions(pollId: Long): Deferred<List<PollOption>?> {
        return GlobalScope.async {
            val response = api.getOptions(pollId, token.tokenString).await()
            if (!response.isSuccessful)
                null
            else
                response.body()
        }
    }

    override fun removePoll(id: Long): Job {
        return api.removePoll(id, token.tokenString)
    }
}