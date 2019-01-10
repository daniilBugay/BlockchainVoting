package technology.desoft.blockchainvoting

import android.app.Application
import android.preference.PreferenceManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import technology.desoft.blockchainvoting.model.*
import technology.desoft.blockchainvoting.model.network.BASE_URL
import technology.desoft.blockchainvoting.model.network.polls.PollRepository
import technology.desoft.blockchainvoting.model.network.user.UserRepository
import technology.desoft.blockchainvoting.model.network.VoteRepository
import technology.desoft.blockchainvoting.model.network.polls.RetrofitPollRepository
import technology.desoft.blockchainvoting.model.network.user.RetrofitUserRepository
import technology.desoft.blockchainvoting.model.network.user.UserTokenProvider
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.SimpleRouter
import technology.desoft.blockchainvoting.presentation.view.MainView

class App: Application() {
    lateinit var mainRouter: Router<MainView>
    lateinit var pollRepository: PollRepository
    lateinit var userRepository: UserRepository
    lateinit var voteRepository: VoteRepository
    lateinit var userProvider: UserTokenProvider

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("$BASE_URL/")
            .build()

        userRepository = RetrofitUserRepository(retrofit)
        pollRepository = RetrofitPollRepository(retrofit)
        mainRouter = SimpleRouter()
        userProvider = TestUserTokenProvider(
            PreferenceManager.getDefaultSharedPreferences(this)
        )


        voteRepository = TestVoteRepository(userProvider)
    }
}