package technology.desoft.blockchainvoting.model.network.polls

data class PollOption(
    val id: Long,
    val content: String,
    val pollId: Long,
    val interest: Double?
)