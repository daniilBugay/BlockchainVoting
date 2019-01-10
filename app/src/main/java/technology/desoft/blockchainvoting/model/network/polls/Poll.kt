package technology.desoft.blockchainvoting.model.network.polls

import com.google.gson.annotations.SerializedName
import technology.desoft.blockchainvoting.model.network.user.User
import java.util.*

data class Poll(
    val id: Long,
    val theme: String,
    val description: String,
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("ends_at")
    val endsAt: Date,
    @SerializedName("user_id")
    val userId: Long
) {
    fun findAuthor(users: List<User>) = users.find { userId == it.id }
}