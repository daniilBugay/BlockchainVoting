package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import technology.desoft.blockchainvoting.model.network.VoteRepository
import technology.desoft.blockchainvoting.model.network.user.UserTokenProvider
import java.util.*

class TestVoteRepository(private val userTokenProvider: UserTokenProvider) :
    VoteRepository {
    private val hashMap = mutableMapOf<Long, MutableList<Vote>>()

    private var id = 0L

    override fun getVotes(optionId: Long) = GlobalScope.async {
        hashMap[optionId]?.toList() ?: emptyList()
    }

    override fun addVote(optionId: Long) {
        userTokenProvider.token.let {
            if (hashMap[optionId] == null)
                hashMap[optionId] = mutableListOf()
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