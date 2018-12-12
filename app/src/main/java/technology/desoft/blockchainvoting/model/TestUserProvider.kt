package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class TestUserProvider: UserProvider {
    override fun login(email: String, password: String): Deferred<Token?> {
        return GlobalScope.async {
            if (email == "test" && password == "test")
                Token("ok")
            else
                null
        }
    }

    override fun registration(email: String, password: String, confirmPassword: String): Deferred<User?> {
        return GlobalScope.async {
            if (password == confirmPassword)
            User(0, email, Calendar.getInstance().timeInMillis, 0)
            else
                null
        }
    }
}