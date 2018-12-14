package technology.desoft.blockchainvoting

import android.app.Application
import android.preference.PreferenceManager
import technology.desoft.blockchainvoting.model.*
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

        mainRouter = SimpleRouter()
        pollRepository = TestPollRepository()
        userRepository = TestUserRepository()
        userProvider = TestUserTokenProvider(
            PreferenceManager.getDefaultSharedPreferences(this)
        )
        voteRepository = TestVoteRepository(userProvider)
    }
}