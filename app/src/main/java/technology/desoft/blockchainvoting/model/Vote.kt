package technology.desoft.blockchainvoting.model

data class Vote(
    val id: Long,
    val pollOptionId: Long,
    val userId: Long,
    val createdAt: Long,
    val updatedAt: Long
)