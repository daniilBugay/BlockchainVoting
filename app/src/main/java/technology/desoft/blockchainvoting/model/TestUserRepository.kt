package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.*

class TestUserRepository: UserRepository {

    override fun login(email: String, password: String): Deferred<Token?> {
        return GlobalScope.async {
            delay(1500)
            if (email == "test" && password == "test")
                Token("ok")
            else
                null
        }
    }

    override fun registration(email: String, password: String, confirmPassword: String): Deferred<User?> {
        return GlobalScope.async {
            delay(1500)
            if (password == confirmPassword)
            User(0, email, Calendar.getInstance().timeInMillis, 0)
            else
                null
        }
    }

    override fun getUsers(): Deferred<List<User>?> {
        return GlobalScope.async {
            List(10){
                User(it.toLong(), "user$it@desoft.technology", 0, 0)
            }
        }
    }
}