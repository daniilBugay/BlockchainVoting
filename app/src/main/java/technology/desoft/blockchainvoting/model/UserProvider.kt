package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred

interface UserProvider {
    fun login(email: String, password: String): Deferred<Token?>
    fun registration(email: String, password: String, confirmPassword: String): Deferred<User?>
}