package technology.desoft.blockchainvoting.model

data class PollOption(
    val id: Long,
    val content: String,
    val createdAt: Long,
    val pollId: Long,
    val updatedAt: Long
)