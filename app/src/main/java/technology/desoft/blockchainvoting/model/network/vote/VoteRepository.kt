package technology.desoft.blockchainvoting.model.network.vote

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import technology.desoft.blockchainvoting.model.network.user.Token

interface VoteRepository {
    fun setToken(token: Token)
    fun addVote(pollId: Long, optionId: Long): Deferred<AddVoteResult>
}