package technology.desoft.blockchainvoting.model.network.user

import android.content.SharedPreferences

class PreferencesUserTokenProvider(private val sharedPreferences: SharedPreferences):
    UserTokenProvider {
    private var savedToken: Token? = null

    override var token: Token
        get() = savedToken ?: throw IllegalStateException("Token is null")
        set(value){
            savedToken = value
        }

    override var userId: Long? = null

    private companion object {
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
    }

    override fun getSavedEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    override fun getSavedPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    override fun saveEmail(email: String) {
        sharedPreferences.edit()
            .putString(KEY_EMAIL, email)
            .apply()
    }

    override fun savePassword(password: String) {
        sharedPreferences.edit()
            .putString(KEY_PASSWORD, password)
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit()
            .remove(KEY_PASSWORD)
            .remove(KEY_EMAIL)
            .apply()

        savedToken = null
        userId = null
    }

}