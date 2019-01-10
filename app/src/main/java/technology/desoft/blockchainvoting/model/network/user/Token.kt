package technology.desoft.blockchainvoting.model.network.user

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("auth_token")
    val tokenString: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean
)