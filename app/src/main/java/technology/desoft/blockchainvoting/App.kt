package technology.desoft.blockchainvoting

import android.app.Application
import technology.desoft.blockchainvoting.model.PollRepository
import technology.desoft.blockchainvoting.model.TestPollRepository
import technology.desoft.blockchainvoting.model.TestUserRepository
import technology.desoft.blockchainvoting.model.UserRepository
import technology.desoft.blockchainvoting.navigation.Router
import technology.desoft.blockchainvoting.navigation.SimpleRouter
import technology.desoft.blockchainvoting.presentation.view.MainView

class App: Application() {
    lateinit var mainRouter: Router<MainView>
    lateinit var pollRepository: PollRepository
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()

        mainRouter = SimpleRouter()
        pollRepository = TestPollRepository()
        userRepository = TestUserRepository()
    }
}