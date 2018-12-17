package technology.desoft.blockchainvoting.model

interface UserTokenProvider {
    var token: Token
    fun setUserId(token: Token)
    fun getUserId(): Long?

    fun getSavedEmail(): String?
    fun getSavedPassword(): String?
    fun saveEmail(email: String)
    fun savePassword(password: String)
}