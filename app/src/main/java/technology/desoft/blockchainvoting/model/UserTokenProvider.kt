package technology.desoft.blockchainvoting.model

interface UserTokenProvider {
    var token: Token
    fun getSavedEmail(): String?
    fun getSavedPassword(): String?
    fun saveEmail(email: String)
    fun savePassword(password: String)
}