package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import technology.desoft.blockchainvoting.model.network.user.Token
import technology.desoft.blockchainvoting.model.network.user.User
import technology.desoft.blockchainvoting.model.network.user.UserAndToken
import technology.desoft.blockchainvoting.model.network.user.UserRepository
import java.util.*

class TestUserRepository: UserRepository {

    override fun setToken(token: Token) {

    }

    override fun login(email: String, password: String): Deferred<Token?> {
        return GlobalScope.async {
            delay(1500)
            if (email == "test" && password == "test")
                Token("ok", true)
            else
                null
        }
    }

    override fun registration(email: String, password: String, confirmPassword: String): Deferred<UserAndToken?> {
        return GlobalScope.async {
            delay(1500)
            if (password == confirmPassword)
                UserAndToken(
                    User(
                        0,
                        email,
                        Calendar.getInstance().time,
                        Calendar.getInstance().time
                    ),
                    Token("test", true)
                )
            else
                null
        }
    }

    override fun getUsers(): Deferred<List<User>?> {
        return GlobalScope.async {
            List(10){
                User(
                    it.toLong(),
                    "user$it@desoft.technology",
                    Calendar.getInstance().time,
                    Calendar.getInstance().time
                )
            }
        }
    }
}