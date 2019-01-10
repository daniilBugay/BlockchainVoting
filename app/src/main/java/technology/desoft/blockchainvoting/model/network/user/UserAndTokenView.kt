package technology.desoft.blockchainvoting.model.network.user

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserAndTokenView(
    val id: Long,
    val email: String,
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("updated_at")
    val updatedAt: Date,
    @SerializedName("auth_token")
    val token: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean
)