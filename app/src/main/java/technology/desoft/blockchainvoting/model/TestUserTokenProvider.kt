package technology.desoft.blockchainvoting.model

import android.content.SharedPreferences

class TestUserTokenProvider(private val sharedPreferences: SharedPreferences): UserTokenProvider {
    override lateinit var token: Token

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

}