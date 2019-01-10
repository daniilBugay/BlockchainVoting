package technology.desoft.blockchainvoting.model.network.user

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    val id: Long,
    val email: String,
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("update_at")
    val updatedAt: Date
)