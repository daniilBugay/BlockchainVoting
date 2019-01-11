package technology.desoft.blockchainvoting.model.network.vote

import kotlinx.coroutines.Deferred

interface VoteRepository {
    fun getVotes(optionId: Long): Deferred<List<Vote>>
    fun addVote(optionId: Long)
}