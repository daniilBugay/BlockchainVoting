package technology.desoft.blockchainvoting.model.network.polls

import com.google.gson.annotations.SerializedName
import java.util.*

data class CreatePollView(
    val theme: String,
    val description: String,
    @SerializedName("ends_at")
    val endsAt: Date
)