package technology.desoft.blockchainvoting.model.network

import kotlinx.coroutines.Deferred
import technology.desoft.blockchainvoting.model.Vote

interface VoteRepository {
    fun getVotes(optionId: Long): Deferred<List<Vote>>
    fun addVote(optionId: Long)
}