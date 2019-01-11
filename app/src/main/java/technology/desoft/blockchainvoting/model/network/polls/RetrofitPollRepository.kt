package technology.desoft.blockchainvoting.model.network.polls

import kotlinx.coroutines.*
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
        return GlobalScope.launch { api.removePoll(id, token.tokenString).await() }
    }

    override fun createPoll(createPollView: CreatePollView, pollOptions: List<CreatePollOptionView>): Job {
        return GlobalScope.launch {
            val response = api.createPoll(token.tokenString, createPollView).await()
            val poll = response.body()
            if (response.isSuccessful && poll != null)
                pollOptions.forEach { api.createOption(poll.id, token.tokenString, it).await() }
        }
    }
}