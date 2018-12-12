package technology.desoft.blockchainvoting.model

data class Poll(
    val id: Long,
    val theme: String,
    val description: String,
    val createdAt: Long,
    val endsAt: Long,
    val updatedAt: Long,
    val userId: Long
) {
    fun findAuthor(users: List<User>) = users.find { userId == it.id }
}