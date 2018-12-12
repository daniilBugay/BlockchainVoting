package technology.desoft.blockchainvoting.model

import kotlinx.coroutines.Deferred

interface UserRepository {
    fun login(email: String, password: String): Deferred<Token?>
    fun registration(email: String, password: String, confirmPassword: String): Deferred<User?>
    fun getUsers(): Deferred<List<User>?>
}