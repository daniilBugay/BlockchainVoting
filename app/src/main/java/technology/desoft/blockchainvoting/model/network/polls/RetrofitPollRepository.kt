package technology.desoft.blockchainvoting.model.network.polls

import kotlinx.coroutines.*
import retrofit2.Retrofit
import technology.desoft.blockchainvoting.model.network.user.Token

class RetrofitPollRepository(retrofit: Retrofit): PollRepository {
    private val api = retrofit.create(PollApi::class.java)

    private var token: Token? = null

    override fun setToken(token: Token){
        this.token = token
    }

    override fun getPolls(): Deferred<List<Poll>?> {
        return GlobalScope.async {
            try {
                val currentToken = token ?: return@async null
                val response = api.getPolls(currentToken.tokenString).await()

                if (!response.isSuccessful)
                    null
                else
                    response.body()
            } catch (e: Throwable){
                null
            }
        }
    }

    override fun getOptions(pollId: Long): Deferred<List<PollOption>?> {
        return GlobalScope.async {
            try {
                val currentToken = token ?: return@async null
                val response = api.getOptions(pollId, currentToken.tokenString).await()
                if (!response.isSuccessful)
                    null
                else
                    response.body()
            } catch (e: Throwable){
                null
            }
        }
    }

    override fun removePoll(id: Long): Job {
        return GlobalScope.launch {
            val currentToken = token ?: return@launch
            api.removePoll(id, currentToken.tokenString).await()
        }
    }

    override fun createPoll(createPollView: CreatePollView, pollOptions: List<CreatePollOptionView>): Job {
        return GlobalScope.launch {
            try {
                val currentToken = token ?: return@launch
                val response = api.createPoll(currentToken.tokenString, createPollView).await()
                val poll = response.body()
                if (response.isSuccessful && poll != null)
                    pollOptions.forEach { api.createOption(poll.id, currentToken.tokenString, it).await() }
            } catch (e: Throwable){

            }
        }
    }
}