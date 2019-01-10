package technology.desoft.blockchainvoting.model.network.user

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Retrofit

class RetrofitUserRepository(retrofit: Retrofit): UserRepository {
    private val api = retrofit.create(UserApi::class.java)

    private lateinit var token: Token

    override fun setToken(token: Token){
        this.token = token
    }

    override fun getUsers(): Deferred<List<User>?> {
        return GlobalScope.async {
            val response = api.getUsers(token.tokenString).await()
            if (response.isSuccessful)
                response.body()
            else
                null

        }
    }

    override fun login(email: String, password: String): Deferred<Token?> {
        return GlobalScope.async {
            val response = api.login(EmailAndPassword(email, password)).await()
            if (response.isSuccessful)
                response.body()
            else
                null
        }
    }

    override fun registration(email: String, password: String, confirmPassword: String): Deferred<UserAndToken?> {
        if (password != confirmPassword) return GlobalScope.async { null }

        return GlobalScope.async {
            val response = api.registration(EmailAndPassword(email,password)).await()
            if (!response.isSuccessful) return@async null
            else {
                val body = response.body() ?: return@async null
                val user = User(
                    body.id,
                    body.email,
                    body.createdAt,
                    body.updatedAt
                )
                val token = Token(body.token, body.isAdmin)
                UserAndToken(user, token)
            }
        }
    }

}