package technology.desoft.blockchainvoting.model

import java.util.*

class TestVoteRepository(private val userTokenProvider: UserTokenProvider) : VoteRepository {
    private val hashMap = mutableMapOf<Long, MutableList<Vote>>()

    private var id = 0L

    override fun getVotes(optionId: Long) = hashMap[optionId]?.toList() ?: emptyList()

    override fun addVote(optionId: Long) {
        userTokenProvider.token.let {
            hashMap[optionId]?.add(
                Vote(
                    id++,
                    optionId,
                    0,
                    Calendar.getInstance().timeInMillis,
                    0
                )
            )
        }
    }
}