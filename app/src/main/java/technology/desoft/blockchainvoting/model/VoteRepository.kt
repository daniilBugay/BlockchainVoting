package technology.desoft.blockchainvoting.model

interface VoteRepository {
    fun getVotes(optionId: Long): List<Vote>
    fun addVote(optionId: Long)
}