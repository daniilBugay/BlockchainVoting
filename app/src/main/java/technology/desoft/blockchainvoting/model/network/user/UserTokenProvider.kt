package technology.desoft.blockchainvoting.model.network.user

interface UserTokenProvider {
    var token: Token
    var userId: Long?

    fun getSavedEmail(): String?
    fun getSavedPassword(): String?
    fun saveEmail(email: String)
    fun savePassword(password: String)
    fun clear()
}