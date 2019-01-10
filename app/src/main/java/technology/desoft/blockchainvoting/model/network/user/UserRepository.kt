package technology.desoft.blockchainvoting.model.network.user

import kotlinx.coroutines.Deferred

interface UserRepository {
    fun setToken(token: Token)

    fun login(email: String, password: String): Deferred<Token?>
    fun registration(email: String, password: String, confirmPassword: String): Deferred<UserAndToken?>
    fun getUsers(): Deferred<List<User>?>
}